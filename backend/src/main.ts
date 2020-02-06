import * as http from 'http';
import { config } from 'dotenv';

import { initServer } from './server/server';
import { initSocket } from './socket/socket';

const bootstrap = async () => {
  config();

  const server = http.createServer(initServer());
  initSocket(server);

  server.listen(process.env.PORT, () =>
    console.log(`Server listening on port ${process.env.PORT}`));
};

bootstrap();
