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
  token: string;
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
  sweating: boolean;
  bloodyStool: boolean;
  mouthSpots: boolean;
  stiffLimbs: boolean;
  temperature: number;
  pain: Pain;
  rash: Rash;
}

export interface Pain {
  painDiscomfortLevel: number;
  painLocation: SymptomLocation;
}

export interface Rash {
  hasRash: boolean;
  rashType: string;
  rashLocationFront: SymptomLocation;
  rashLocationBack: SymptomLocation;
}

export interface SymptomLocation {
  head: boolean;
  neck: boolean;
  leftShoulder: boolean;
  rightShoulder: boolean;
  leftArm: boolean;
  rightArm: boolean;
  leftHand: boolean;
  rightHand: boolean;
  centerChest: boolean;
  leftSide: boolean;
  rightSide: boolean;
  genitals: boolean;
  leftHip: boolean;
  rightHip: boolean;
  leftKnee: boolean;
  rightKnee: boolean;
  leftFoot: boolean;
  rightFoot: boolean;
}
