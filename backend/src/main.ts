import { config } from 'dotenv';
import * as express from 'express';
import { Express } from 'express-serve-static-core';
import { hello } from './routes';

const addRoutes = (app: Express) => {
  app.get('/', hello);
};

const bootstrap = () => {
  config();

  const app = express();
  addRoutes(app);

  app.listen(process.env.PORT, () =>
    console.log(`Express listening on port ${process.env.PORT}`));
};

bootstrap();
