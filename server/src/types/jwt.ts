import type { User } from "@prisma/client";

export type JwtPayload = {
  userId: number;
  email: string;
  name: string;
};
