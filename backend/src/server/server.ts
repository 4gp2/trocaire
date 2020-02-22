import * as express from 'express';
import * as morgan from 'morgan';
import * as exhbs from 'express-handlebars';
import * as bodyParser from 'body-parser';
import * as cookieParser from 'cookie-parser';
import {
  Express,
  Request,
  Response,
  NextFunction,
} from 'express-serve-static-core';

import { UserClaims, DiagnosisUpload } from '../firebase/types';
import { NewUserResponse } from './types';
import {
  createNewCookie,
  verifyCookie,
  getUser,
  addNewUser,
  getUserFromToken,
  storePatientData,
} from '../firebase/firebase';

const dashboard = (_req: Request, res: Response): void =>
  res.render('admin_main', { layout: false });

const datavis = (_req: Request, res: Response): void =>
  res.render('dash', { layout: false });

const login = (_req: Request, res: Response): void =>
  res.render('login', {
    layout: false,
    emailExtension: process.env.EMAIL_EXTENSION,
  });

const login2 = (_req: Request, res: Response): void =>
  res.render('login2', { layout: false });

const createNewUser = async (req: Request, res: Response): Promise<void> => {
  const details = await addNewUser(req.query.admin === 'true');
  if (!details) {
    res.json({ error: true } as NewUserResponse);
    return;
  }
  res.json({ error: false, details } as NewUserResponse);
};

const newSessionCookie = async (req: Request, res: Response): Promise<void> => {
  const expiresIn = 60 * 60 * 24 * 5 * 1000;
  const session = await createNewCookie(req.body.token, expiresIn);
  if (!session) {
    res.sendStatus(401);
    return;
  }
  res.cookie('session', session, {
    maxAge: expiresIn,
    httpOnly: true,
    secure: false,
  });
  res.redirect('/');
};

const uploadDiagnosis = async (req: Request, res: Response): Promise<void> => {
  const user = await getUserFromToken(req.body.token);
  if (!user) {
    res.sendStatus(401);
  }

  try {
    const data = JSON.parse(req.body.data) as DiagnosisUpload;
    data.patients.forEach(async (p) => await storePatientData(p));
    res.sendStatus(201);
  } catch (e) {
    res.sendStatus(400);
  }
};

const isAdminLoggedIn =
  async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    if (!req.cookies.session) {
      return res.redirect('/login');
    }

    const decoded = await verifyCookie(req.cookies.session);
    if (!decoded) {
      return res.redirect('/login');
    }

    const user = await getUser(decoded.uid);
    if (!user) {
      return res.redirect('/login');
    }

    const claims = user.customClaims as UserClaims;
    if (claims && claims.isAdmin) {
      return next();
    }

    return res.redirect('/login');
  };

const initRoutes = (app: Express): void => {
  app.get('/', isAdminLoggedIn, dashboard);
  app.get('/dash', datavis);
  app.get('/login', login);
  app.get('/login2', login2);
  app.get('/api/newuser', isAdminLoggedIn, createNewUser);

  app.post('/api/session', newSessionCookie);
  app.post('/api/upload', uploadDiagnosis);
};

export const initServer = (): Express => {
  const app = express();

  app.engine('.hbs', exhbs({ extname: '.hbs' }));
  app.set('view engine', '.hbs');

  app.use(morgan('dev'));
  app.use(bodyParser.urlencoded({ extended: false }));
  app.use(bodyParser.json());
  app.use(cookieParser());
  app.use(express.static('static'));

  initRoutes(app);
  return app;
};
