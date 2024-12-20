import { createMiddleware } from "hono/factory";
import { db } from "../lib/db.js";

export type DbEnv = {
  Variables: {
    db: typeof db;
  };
};

export const dbMiddleware = createMiddleware<DbEnv>(async (c, next) => {
  c.set("db", db);
  await next();
});
