import * as express from 'express';
import * as morgan from 'morgan';
import * as passport from 'passport';
import * as bParser from 'body-parser';
import * as session from 'express-session';
import { Strategy } from 'passport-local';
import { Express, Request, Response } from 'express-serve-static-core';

import { UserModel, User } from '../database/database';
import { verifyPassword } from '../utils/utils';

passport.serializeUser((user: User, done) => {
  done(null, { _id: user._id });
});

passport.deserializeUser(async (user: User, done) => {
  try {
    const u: User = await UserModel.findById(user._id).exec();
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
}, async (id, pass, done) => {
  try {
    const u = await UserModel.findById(id).exec();
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

const loginGet = (_req: Request, res: Response) => {
  res.render('login');
};

const initRoutes = (app: Express): void => {
  app.get('/', hello);
  app.get('/login', loginGet);

  app.post('/login', passport.authenticate('login', {
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
