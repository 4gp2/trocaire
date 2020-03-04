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
  isAdmin: Pick<NewUserDetails, 'isAdmin'>;
}

export class NextID {
  next: number;
}

export interface DiagnosisUpload {
  patients: Patient[];
}

export interface StoredPatient {
  firstName: string;
  lastName: string;
  dob: Date;
  village: string;
  sex: string;
  diagnoses: StoredDiagnosis[];
}

export type PatientIdentifier =
  Pick<StoredPatient, 'firstName' | 'lastName' | 'dob'>;

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
