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

import { UserClaims } from './firebase/types';
import {
  createNewCookie,
  verifyCookie,
  getUser,
} from './firebase/firebase';

const hello = (_req: Request, res: Response): void => {
  res.render('home', { layout: false });
};

const loginGet = (_req: Request, res: Response): void =>
  res.render('login', { layout: false });

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
  app.get('/', isAdminLoggedIn, hello);
  app.get('/login', loginGet);

  app.post('/session', newSessionCookie);
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
