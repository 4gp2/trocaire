import * as express from 'express';
import * as morgan from 'morgan';
import { Express, Request, Response } from 'express-serve-static-core';

const hello = (_req: Request, res: Response): void => {
  res.send('Hello there');
};

const initRoutes = (app: Express): void => {
  app.get('/', hello);
};

export const initServer = (): Express => {
  const app = express();
  app.use(morgan('dev'));
  initRoutes(app);
  return app;
};
