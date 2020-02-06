import { connect } from 'mongoose';
import { Typegoose, prop } from 'typegoose';

connect(
  `mongodb://${process.env.DB_URL}/${process.env.DB_NAME}`,
  {
    useNewUrlParser: true,
    user: process.env.DB_USER,
    pass: process.env.DB_PASS,
  },
);

export class User extends Typegoose {
  @prop({ required: true, unique: true })
  _id!: string;

  @prop({ required: true })
  passwordHash!: string;

  @prop({ required: true })
  role!: string;
}

export const UserModel = new User().getModelForClass(User);
