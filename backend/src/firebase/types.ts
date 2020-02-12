export class NewUserDetails {
  uid: string;
  email: string;
  initialPassword: string;
  isAdmin: boolean;
}

export class AuthToken {
  uid: string;
  token: string;
}

export class UserClaims {
  isAdmin: boolean;
}

export class NextID {
  next: number;
}

export interface Diagnosis {
  patients: Patient[];
}

export interface Patient {
  details: Details;
  symptoms: Symptoms;
}

export interface Details {
  name: string;
  dob: string; // YYYY-MM-DD
  village: string;
  sex: string;
  latitude: number;
  longitude: number;
  date: Date;
}

export interface Symptoms {
  nausea: boolean;
  diarrhea: boolean;
  highHeartRate: boolean;
  dehydration: boolean;
  muscleAches: boolean;
  dryCough: boolean;
  soreThroat: boolean;
  headache: boolean;
  rash: boolean;
  redEyes: boolean;
  temperature: number;
  painType: string;
  painDiscomfortLevel: number;
  painLocation: { [key: string]: boolean };
  rashType: string;
  itchyRash: boolean;
  rashLocationFront: { [key: string]: boolean };
  rashLocationBack: { [key: string]: boolean };
}
