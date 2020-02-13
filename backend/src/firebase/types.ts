export class NewUserDetails {
  uid: Required<string>;
  email: Required<string>;
  initialPassword: Required<string>;
  isAdmin: Required<boolean>;
}

export class AuthToken {
  uid: string;
  token: string;
}

export class UserClaims {
  isAdmin: Pick<NewUserDetails, 'isAdmin'>;
}

export class NextID {
  next: number;
}

export interface DiagnosisUpload {
  patients: Patient[];
}

export interface StoredPatient {
  firstName: Required<string>;
  lastName: Required<string>;
  dob: Required<Date>;
  village: Required<string>;
  sex: Required<string>;
  diagnoses: Required<StoredDiagnosis[]>;
}

export type StoredDiagnosis =
  Pick<Patient, 'latitude' | 'longitude' | 'date' | 'symptoms'>;

export interface Patient {
  firstName: string;
  lastName: string;
  dob: Date;
  village: string;
  sex: string;
  latitude: number;
  longitude: number;
  date: Date;
  symptoms: Symptoms;
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
