export const patientDocPath =
  (lastName: string, firstName: string, dob: Date): string =>
    `patients/${lastName},${firstName},${dob.toISOString().slice(0, 10)}`;

export const userEmail = (id: string): string =>
  `${id}@${process.env.EMAIL_EXTENSION}`;
