import * as paseto from 'paseto.js';
import * as argon2 from 'argon2';

const sk = new paseto.SymmetricKey(new paseto.V2());

export const initPaseto = async (): Promise<void> =>
  await sk.inject(Buffer.from(process.env.AUTH_TOKEN_KEY, 'base64'));

export const hashPassword = async (passwd: string): Promise<string | null> => {
  try {
    return await argon2.hash(passwd, { type: argon2.argon2id });
  } catch (err) {
    return null;
  }
};

export const verifyPassword =
  async (hash: string, pass: string): Promise<boolean> => {
    try {
      return await argon2.verify(hash, pass, { type: argon2.argon2id });
    } catch (e) {
      return false;
    }
  };

export const generateAppAuthToken = async (id: string): Promise<string> =>
  await sk.protocol().encrypt(id, sk);

export const verifyAppAuthToken =
  async (id: string, token: string): Promise<boolean> =>
    id === await sk.protocol().decrypt(token, sk);
