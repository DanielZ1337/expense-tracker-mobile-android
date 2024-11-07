import { Hono } from "hono";
import { compare, hash } from "bcrypt";
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
  .post("/login", async (c) => {
    const { email, password } = await c.req.json();

    if (!email || !password) {
      return c.json({ message: "Email and password are required" }, 400);
    }

    const { db } = c.var;

    const user = await db.user.findFirst({
      where: {
        email,
      },
    });

    if (!user) {
      return c.json({ message: "Invalid email or password" }, 401);
    }

    const isPasswordValid = await compare(password, user.password);
    if (!isPasswordValid) {
      return c.json({ message: "Invalid email or password" }, 401);
    }

    const token = await db.token.create({
      data: {
        user: {
          connect: {
            id: user.id,
          },
        },
        token: crypto.randomUUID(),
      },
    });

    return c.json({ message: "Logged in successfully", token });
  });
