import * as express from 'express';
import * as morgan from 'morgan';
import * as passport from 'passport';
import * as bParser from 'body-parser';
import * as session from 'express-session';
import { Strategy } from 'passport-local';
import { Express, Request, Response } from 'express-serve-static-core';

import { User } from './database/types';
import { fetchUser, storeToken } from './database/database';
import { verifyPassword, generateAppAuthToken } from './auth';

passport.serializeUser((user: User, done): void =>
  done(null, { _id: user._id }));

passport.deserializeUser(async (user: User, done): Promise<void> => {
  try {
    const u = await fetchUser(user._id);
    if (u) {
      done(null, u._id);
    }
    done(null, false);
  } catch (e) {
    done(e);
  }
});

passport.use('login', new Strategy({
  usernameField: 'id',
  passwordField: 'password',
}, async (id, pass, done): Promise<void> => {
  try {
    const u = await fetchUser(id);
    if (!u) {
      return done(null, false);
    }
    if (await verifyPassword(u.passwordHash, pass)) {
      return done(null, u);
    }
    return done(null, false);
  } catch (e) {
    return done(e);
  }
}));

const hello = (_req: Request, res: Response): void => {
  res.send('Hello there');
};

const loginGet = (_req: Request, res: Response): void => res.render('login');

const workerAuthToken = async (req: Request, res: Response): Promise<void> => {
  const u = await fetchUser(req.body.id);
  if (!u) {
    res.sendStatus(401);
    return;
  }

  const token = await generateAppAuthToken(u._id);
  storeToken(u._id, token);
  res.send(token);
};

const initRoutes = (app: Express): void => {
  app.get('/', hello);
  app.get('/login', loginGet);

  app.post('/auth', workerAuthToken);
  app.post('/login', passport.authenticate('local', {
    successRedirect: '/',
    failureRedirect: '/login',
  }));
};

export const initServer = (): Express => {
  const app = express();

  app.set('views', `${__dirname}/views`);
  app.use(bParser.urlencoded({ extended: true }));
  app.use(bParser.json());
  app.use(morgan('dev'));
  app.use(session({
    secret: process.env.SESSION_SECRET,
    resave: false,
    saveUninitialized: false,
  }));
  app.use(passport.initialize());
  app.use(passport.session());

  initRoutes(app);
  return app;
};
