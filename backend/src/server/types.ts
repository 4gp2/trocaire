import { NewUserDetails } from '../firebase/types';

export class NewUserResponse {
  error: boolean;
  details?: NewUserDetails;
}
