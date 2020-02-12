export class AuthToken {
  uid: string;
  token: string;
}

export class Patient {
  id!: string;
  firstName!: string;
  lastName!: string;
  age!: number;
  village!: string;
  symptoms!: string[];
}

export class UserClaims {
  isAdmin: boolean;
}
