import { connect, Schema, model } from 'mongoose';
import { getModelForClass, prop } from '@typegoose/typegoose';

export const initDatabase = (): void => {
  connect(`mongodb://${process.env.DB_URL}`, {
    useNewUrlParser: true,
    user: process.env.DB_USER,
    pass: process.env.DB_PASS,
    dbName: process.env.DB_NAME,
    useUnifiedTopology: true,
  });
};

export class User {
  @prop({ required: true, unique: true })
  uid!: string;

  @prop({ required: true })
  passwordHash!: string;

  @prop({ required: true })
  role!: string;
}

export const UserModel = getModelForClass(User);

class AuthToken {
  @prop({ required: true, unique: true })
  uid!: string;

  @prop({ required: true })
  token!: string;
}

const AuthTokenModel = getModelForClass(AuthToken);

export const storeToken = async (uid: string, token: string): Promise<void> => {
  await AuthTokenModel.findOneAndUpdate(
    { uid },
    { uid, token },
    { upsert: true },
  );
};

export const fetchToken = async (uid: string): Promise<string | null> => {
  try {
    const model = await AuthTokenModel.findOne({ uid });
    if (!model) {
      return null;
    }
    return model.token;
  } catch (e) {
    return null;
  }
};

export const patientSchema = new Schema({
  name: { type: String, required: true },
  age: { type: Number, required: true },
  village: { type: String, required: true },
  symptoms: { type: Array, required: true } 
});

const Patient = model('Patient', patientSchema);
export default Patient;