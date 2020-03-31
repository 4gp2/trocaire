import { NewUserDetails, StoredPatient } from '../firebase/types';

interface StandardResponse {
  error: boolean;
  msg?: string;
}

export type NewUserResponse = StandardResponse & {
  details: NewUserDetails;
};

export type GetPatientResponse = StandardResponse & {
  patient: StoredPatient;
};

export type GetVillagesResponse = StandardResponse & {
  villages: string[];
};

export interface GraphDataBreakdown {
  byVillage: boolean;
  villagePatients?: StoredPatient[];
  allPatients?: { [key: string]: StoredPatient[] };
}

export type GraphDataResponse = StandardResponse & GraphDataBreakdown;
