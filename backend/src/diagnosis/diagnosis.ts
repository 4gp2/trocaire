import { Symptoms } from '../firebase/types';
import { Disease, DiseaseRank } from './types';

const hasPain = ({ muscleAches, pain }: Symptoms): boolean =>
  muscleAches || pain.painDiscomfortLevel > 0 &&
  Object.values(pain.painLocation).some(e => e);

const rank = (dcounts: number[]): DiseaseRank[] =>
  dcounts
    .reduce((acc, numSymptoms, ind) =>
      acc.concat([{ disease: Disease[ind], numSymptoms }]),
      [] as DiseaseRank[])
    .sort((a, b) => b.numSymptoms - a.numSymptoms);

export const matchDiseases = (s: Symptoms): DiseaseRank[] => {
  const d = Array(Object.keys(Disease).length / 2).fill(0);
  if (s.bloodyStool) d[Disease.Malaria]++;
  if (s.dehydration) d[Disease.Cholera]++;
  if (s.diarrhea) {
    d[Disease.Cholera]++;
    d[Disease.Malaria]++;
  }
  if (s.dryCough) {
    d[Disease.Measles]++;
    d[Disease['COVID-19']]++;
  }
  if (s.headache) {
    d[Disease.Malaria]++;
    d[Disease.Polio]++;
  }
  if (s.highHeartRate) d[Disease.Cholera]++;
  if (s.mouthSpots) d[Disease.Measles]++;
  if (s.muscleAches) {
    d[Disease.Measles]++;
    d[Disease.Polio]++;
    d[Disease.Malaria]++;
  }
  if (s.nausea) {
    d[Disease.Cholera]++;
    d[Disease.Polio]++;
    d[Disease.Malaria]++;
  }
  if (hasPain(s)) {
    d[Disease.Cholera]++;
    d[Disease.Polio]++;
    d[Disease.Malaria]++;
  }
  if (s.rash.hasRash) d[Disease.Measles]++;
  if (s.soreThroat) {
    d[Disease.Measles]++;
    d[Disease.Polio]++;
  }
  if (s.stiffLimbs) d[Disease.Polio]++;
  if (s.sweating) d[Disease.Malaria]++;
  if (s.temperature > 37.5) {
    d[Disease['COVID-19']]++;
    d[Disease.Measles]++;
    d[Disease.Polio]++;
    d[Disease.Malaria]++;
  }
  return rank(d);
};
