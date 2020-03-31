import { listen, Server } from 'socket.io';
import { EventEmitter } from 'events';
import * as http from 'http';

let io: Server;
const emitter = new EventEmitter();

export const initSocket = (server: http.Server): void => {
  io = listen(server);

  io.on('connection', sock =>
    emitter.on('data', (data: string) => sock.emit(data)));
};

export const broadcastNewDataEvent = (): void => {
  emitter.emit('data', '');
};
