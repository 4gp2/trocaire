import { listen, Server } from 'socket.io';
import { EventEmitter } from 'events';
import * as http from 'http';

let io: Server;
const emitter = new EventEmitter();

export const initSocket = (server: http.Server): void => {
  io = listen(server);

  io.on('connection', sock => {
    sock.on('message', msg => console.log(`message: ${msg}`));
    sock.on('other', msg => console.log(`other: ${msg}`));

    // For server to client broadcasts only.
    emitter.on('broadcast', data => {
      sock.broadcast.emit(data);
      console.log(`Broadcasted: ${data}`);
    });
  });
};

export const broadcastEvent = (data: any): void => {
  emitter.emit('broadcast', data);
};
