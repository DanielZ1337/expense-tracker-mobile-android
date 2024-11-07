import type { User, Token } from "@prisma/client";
import type { DbEnv } from "../middlewares/db.js";

export type HonoContext = DbEnv & {
  Variables: {
    user:
      | (User & {
          tokens: Token[];
        })
      | null;
  };
};
