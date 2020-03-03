import { NewUserDetails, StoredPatient } from '../firebase/types';

export class NewUserResponse {
  error: boolean;
  msg?: string;
  details?: NewUserDetails;
}

export class GetPatientResponse {
  error: boolean;
  msg?: string;
  patient?: StoredPatient;
}
