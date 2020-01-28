import { Request, Response } from 'express-serve-static-core';

export const hello = (_req: Request, res: Response): void => {
  res.send('Hello there');
};
