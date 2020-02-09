import * as http from 'http';
import { config } from 'dotenv';

import { initServer } from './server';
import { initSocket } from './socket';
import { initPaseto } from './auth';

const bootstrap = async (): Promise<void> => {
  config();

  const server = http.createServer(initServer());
  initSocket(server);
  initPaseto();

  server.listen(process.env.PORT, (): void =>
    console.log(`Server listening on port ${process.env.PORT}`));
};

bootstrap();
