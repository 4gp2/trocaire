import * as argon2 from 'argon2';

export const hashPassword = async (passwd: string): Promise<string | null> => {
  try {
    const hash = await argon2.hash(passwd, { type: argon2.argon2id });
    return hash;
  } catch (err) {
    return null;
  }
};

export const verifyPassword =
  async (hash: string, pass: string): Promise<boolean> => {
    try {
      if (await argon2.verify(hash, pass, { type: argon2.argon2id })) {
        return true;
      }
      return false;
    } catch (e) {
      return false;
    }
  };
