export class User {
  _rev?: string;
  _id!: string;
  passwordHash!: string;
  role!: string;
}

export class AuthToken {
  _id!: string;
  _rev?: string;
  token!: string;
}
