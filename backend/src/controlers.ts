import Patient from './database';
import { Request, Response } from 'express';

export let allPatients = (req: Request, res: Response) => {
  Patient.find((err: any, patients: any) => {
    if (err) {
      res.send(err);
    } else {
      res.send(patients);
    }
  });
};

export let getPatient = (req: Request, res: Response) => {
  Patient.findById(req.params.id, (err: any, patient: any) => {
    if (err) {
      res.send(err);
    } else {
      res.send(patient);
    }
  });
};

export let addPatient = (req: Request, res: Response) => {
  const patient = new Patient(req.body);
  patient.save((err: any) => {
    if (err) {
      res.send(err);
    } else {
      res.send(patient);
    }
  });
};
