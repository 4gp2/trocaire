import * as http from 'http';
import { config } from 'dotenv';

import { initServer } from './server';
import { initSocket } from './socket';
import { initFirebase } from './firebase/firebase';

const bootstrap = async (): Promise<void> => {
  config();

  const server = http.createServer(initServer());
  initSocket(server);
  initFirebase();

  server.listen(process.env.PORT, (): void =>
    console.log(`Server listening on port ${process.env.PORT}`));
};

bootstrap();
