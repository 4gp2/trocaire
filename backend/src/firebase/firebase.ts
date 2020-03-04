import { readFileSync } from 'fs';
import { generate } from 'generate-password';
import { auth, firestore, initializeApp, credential } from 'firebase-admin';

import { patientDocPath, userEmail } from './utils';
import {
  NewUserDetails,
  NextID,
  Patient,
  StoredPatient,
  StoredDiagnosis,
  PatientIdentifier,
} from './types';

export const initFirebase = (): void => {
  const s = JSON.parse(readFileSync(process.env.SERVICE_ACCOUNT_FILE, 'utf-8'));
  initializeApp({
    credential: credential.cert(s),
    databaseURL: process.env.DB_URL,
  });
};

export const addNewUser =
  async (isAdmin: boolean): Promise<NewUserDetails | null> => {
    const ref = firestore().doc('.metadata/ids');
    const doc = await ref.get();
    const initPass = generate({
      length: parseInt(process.env.INITIAL_PASSWORD_LENGTH, 10),
      numbers: true,
    });
    let useID = 0;

    if (doc.exists) {
      const data = doc.data() as NextID;
      useID = data.next;
    } else {
      await ref.set({ next: 0 });
    }

    const idString = useID.toString();
    if (await addUser(idString, initPass, isAdmin)) {
      await ref.update({ next: firestore.FieldValue.increment(1) });
      return {
        uid: idString,
        email: userEmail(idString),
        initialPassword: initPass,
        isAdmin,
      };
    }

    return null;
  };

const addUser =
  async (uid: string, password: string, isAdmin: boolean): Promise<boolean> => {
    try {
      await auth().createUser({ uid, password, email: userEmail(uid) });
      await auth().setCustomUserClaims(uid, { isAdmin });
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

export const revokeCookie = async (cookie: string): Promise<void> => {
  const token = await verifyCookie(cookie);
  if (token) {
    await auth().revokeRefreshTokens(token.uid);
  }
};

export const storePatientData = async (p: Required<Patient>): Promise<void> => {
  const ref = firestore().doc(patientDocPath(p.lastName, p.firstName, p.dob));
  const exists = (await ref.get()).exists;
  if (exists) {
    await ref.update({
      diagnoses: firestore.FieldValue.arrayUnion({
        latitude: p.latitude,
        longitude: p.longitude,
        date: p.date,
        symptoms: p.symptoms,
      } as StoredDiagnosis),
    });
  } else {
    await ref.set({
      firstName: p.firstName,
      lastName: p.lastName,
      dob: p.dob,
      village: p.village,
      sex: p.sex,
      diagnoses: [{
        latitude: p.latitude,
        longitude: p.longitude,
        date: p.date,
        symptoms: p.symptoms,
      }],
    } as StoredPatient);
  }
};

export const getPatientRecord =
  async (iden: Required<PatientIdentifier>): Promise<StoredPatient | null> => {
    const doc = await firestore().doc(patientDocPath(
      iden.lastName,
      iden.firstName,
      iden.dob,
    )).get();

    if (doc.exists) {
      return doc.data() as StoredPatient;
    }
    return null;
  };
