import { Hono } from "hono";
import { hash } from "bcrypt";
import type { HonoContext } from "./types/hono-context.js";

export const authController = new Hono<HonoContext>()
  .post("/register", async (c) => {
    const { db } = c.var;
    const { email, password, name, passwordConfirmation } = await c.req.json();

    if (!email || !password || !name || !passwordConfirmation) {
      return c.json({ message: "Email and password are required" }, 400);
    }

    if (password !== passwordConfirmation) {
      return c.json({ message: "Passwords do not match" }, 400);
    }

    const hashedPassword = await hash(password, 10);

    const user = await db.user.create({
      data: {
        email,
        name,
        password: hashedPassword,
      },
    });

    return c.json({ message: "Registered successfully", user });
  })
  .get("/login", (c) => {
    return c.json({ message: "login" });
  });
