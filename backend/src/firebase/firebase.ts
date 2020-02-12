import { readFileSync } from 'fs';
import {
  auth,
  firestore,
  initializeApp,
  credential,
} from 'firebase-admin';


export const initFirebase = (): void => {
  const service = JSON.parse(readFileSync('trocaire-firebase.json', 'utf-8'));
  initializeApp({
    credential: credential.cert(service),
    databaseURL: process.env.DB_URL,
  });
};

export const addUser =
  async (uid: string, password: string, isAdmin: boolean): Promise<boolean> => {
    try {
      await auth().createUser({
        uid,
        password,
        email: `${uid}@health.trocaire.org`,
      });
      if (isAdmin) {
        await auth().setCustomUserClaims(uid, { isAdmin });
      }
      return true;
    } catch (e) {
      console.error(e);
      return false;
    }
  };

export const getUserFromToken =
  async (token: string): Promise<auth.UserRecord | null> => {
    try {
      const uid = (await auth().verifyIdToken(token)).uid;
      return await auth().getUser(uid);
    } catch (e) {
      return null;
    }
  };

export const getUser = async (uid: string): Promise<auth.UserRecord | null> => {
  try {
    return await auth().getUser(uid);
  } catch (e) {
    return null;
  }
};

export const createNewCookie =
  async (token: string, expiresIn: number): Promise<string | null> => {
    try {
      return await auth().createSessionCookie(token, { expiresIn });
    } catch (e) {
      return null;
    }
  };

export const verifyCookie =
  async (cookie: string): Promise<auth.DecodedIdToken | null> => {
    try {
      return await auth().verifySessionCookie(cookie, true);
    } catch (e) {
      return null;
    }
  };
