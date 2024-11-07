import { createMiddleware } from "hono/factory";
import type { HonoContext } from "../types/hono-context.js";

export const authMiddleware = createMiddleware<HonoContext>(async (c, next) => {
  const authorization = c.req.header("Authorization");

  if (!authorization) {
    return c.json({ message: "Unauthorized" }, 401);
  }

  const [type, token] = authorization.split(" ");

  if (type !== "Bearer") {
    return c.json({ message: "Unauthorized" }, 401);
  }

  const { db } = c.var;

  const user = await db.user.findFirst({
    where: {
      tokens: {
        some: {
          token,
        },
      },
    },
    include: {
      tokens: true,
    },
  });

  if (!user) {
    return c.json({ message: "Unauthorized" }, 401);
  }

  c.set("user", user);

  await next();
});
