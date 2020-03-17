import { NewUserDetails, StoredPatient } from '../firebase/types';

export interface NewUserResponse {
  error: boolean;
  msg?: string;
  details?: NewUserDetails;
}

export interface GetPatientResponse {
  error: boolean;
  msg?: string;
  patient?: StoredPatient;
}

export interface RecordsTimePeriodResponse {
  error: boolean;
  msg?: string;
  numRecords?: number;
}
