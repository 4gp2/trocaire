export enum Disease {
  Measles,
  Cholera,
  Polio,
  Malaria,
  'COVID-19',
}

export interface DiseaseRank {
  disease: string;
  numSymptoms: number;
}
