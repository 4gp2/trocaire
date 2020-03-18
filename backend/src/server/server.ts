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
} from 'express';

import {
  NewUserResponse,
  GetPatientResponse,
  GraphDataResponse,
  GraphDataBreakdown,
} from './types';
import { UserClaims, DiagnosisUpload } from '../firebase/types';
import {
  createNewCookie,
  verifyCookie,
  getUser,
  addNewUser,
  getUserFromToken,
  storePatientData,
  revokeCookie,
  getPatientRecord,
  getRecordsAtTimePeriod,
} from '../firebase/firebase';
import { StoredPatient } from '../firebase/types';

const dashboard = (_req: Request, res: Response): void =>
  res.render('admin_main', { layout: false });

const datavis = (_req: Request, res: Response): void =>
  res.render('dash', { layout: false });

const login = (_req: Request, res: Response): void =>
  res.render('login', {
    layout: false,
    emailExtension: process.env.EMAIL_EXTENSION,
  });

const createNewUser = async (req: Request, res: Response): Promise<void> => {
  const details = await addNewUser(req.body.admin);
  if (!details) {
    res.json({ error: true } as NewUserResponse);
    return;
  }
  res.json({ error: false, details } as NewUserResponse);
};

const newSession = async (req: Request, res: Response): Promise<void> => {
  const expiresIn = 60 * 60 * 24 * 7 * 1000;
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

const clearSession = async (req: Request, res: Response): Promise<void> => {
  await revokeCookie(req.cookies.session);
  res.clearCookie('session');
  res.redirect('/login');
};

const uploadDiagnosis = async (req: Request, res: Response): Promise<void> => {
  try {
    const data: DiagnosisUpload = req.body;
    data.patients.forEach(p => {
      p.dob = new Date(p.dob);
    });

    const user = await getUserFromToken(data.token);
    if (!user) {
      res.sendStatus(401);
      return;
    }

    data.patients.forEach(async p => await storePatientData(p));
    res.sendStatus(201);
  } catch (e) {
    res.sendStatus(400);
  }
};

const fetchPatient = async (req: Request, res: Response): Promise<void> => {
  const record = await getPatientRecord({
    lastName: req.body.lastName,
    firstName: req.body.firstName,
    dob: new Date(`${req.body.year}-${req.body.month}-${req.body.day}`),
  });

  if (record) {
    res.json({ error: false, patient: record } as GetPatientResponse);
  }
  res.json({ error: true } as GetPatientResponse);
};

const fetchGraphData = async (req: Request, res: Response): Promise<void> => {
  const start = new Date(req.body.start);
  const end = new Date(req.body.end);
  const records = await getRecordsAtTimePeriod(start, end);
  const bd: GraphDataBreakdown = { byVillage: req.body.village };
  if (req.body.village) {
    const patients = records.filter(p => p.village === req.body.village);
    bd.villagePatients = patients.reduce((pAcc, pCur) => {
      pCur.diagnoses = pCur.diagnoses
        .filter(d => d.possibleDiseases.includes(req.body.disease));
      if (pCur.diagnoses.length > 1) {
        pAcc.push(pCur);
      }
      return pAcc;
    }, [] as StoredPatient[]);
  } else {
    records.forEach(p => {
      p.diagnoses = p.diagnoses
        .filter(d => d.possibleDiseases.includes(req.body.disease));
      if (p.diagnoses.length > 1) {
        bd.allPatients[p.village] = bd.allPatients[p.village]
          ? bd.allPatients[p.village].concat([p])
          : [p];
      }
    });
  }
  if (!bd.allPatients || bd.villagePatients) {
    res.json({ error: true, msg: 'No patients matched' });
    return;
  }
  res.json({
    error: false,
    ...bd,
  } as GraphDataResponse);
};

const isAdminIDTokenValid =
  async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    const user = await getUserFromToken(req.body.token);
    if (!user) {
      res.sendStatus(401);
      return;
    }

    const claims = user.customClaims as UserClaims;
    if (claims && claims.isAdmin) {
      return next();
    }

    res.sendStatus(401);
  };

const isAdminCookieLoggedIn =
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
  app.get('/', isAdminCookieLoggedIn, dashboard);
  app.get('/dash', datavis);
  app.get('/login', login);
  app.get('/logout', clearSession);

  app.post('/api/session', newSession);
  app.post('/api/upload', uploadDiagnosis);
  app.post('/api/graph', isAdminIDTokenValid, fetchGraphData);
  app.post('/api/patient', isAdminIDTokenValid, fetchPatient);
  app.post('/api/newuser', isAdminIDTokenValid, createNewUser);
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
