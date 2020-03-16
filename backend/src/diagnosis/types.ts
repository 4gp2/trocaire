export enum Disease {
  Measles,
  Cholera,
  Polio,
  Malaria,
}

export interface DiseaseRank {
  disease: string;
  numSymptoms: number;
}
