import * as libnano from 'nano';
import { User, AuthToken, Patient } from './types';

let nano: libnano.ServerScope;
let usersDB: libnano.DocumentScope<User>;
let tokensDB: libnano.DocumentScope<AuthToken>;
let patientsDB: libnano.DocumentScope<Patient>;

export const initDatabase = async (): Promise<void> => {
  nano = libnano({
    url: process.env.DB_URL,
    requestDefaults: { jar: true },
  });
  await nano.auth(process.env.DB_USER, process.env.DB_PASS);

  usersDB = nano.use('users');
  tokensDB = nano.use('tokens');
  patientsDB = nano.use('patients');
};

export const storeToken = async (uid: string, token: string): Promise<void> => {
  try {
    const existing = await tokensDB.get(uid);
    await tokensDB.insert({ _id: uid, token, _rev: existing._rev });
  } catch {
    await tokensDB.insert({ _id: uid, token }, uid);
  }
};

export const fetchToken = async (uid: string): Promise<AuthToken | null> => {
  try {
    return await tokensDB.get(uid);
  } catch (e) {
    return null;
  }
};

export const fetchUser = async (uid: string): Promise<User | null> => {
  try {
    return await usersDB.get(uid);
  } catch (e) {
    return null;
  }
};


