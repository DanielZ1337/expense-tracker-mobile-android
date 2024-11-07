import { serve } from "@hono/node-server";
import { Hono } from "hono";
import { authController } from "./auth.js";
import { dbMiddleware } from "./middlewares/db.js";
import { config } from "dotenv";
import { authMiddleware } from "./middlewares/auth.js";

config();

const app = new Hono()
  .basePath("/api")
  .use(dbMiddleware)
  .get("/", (c) => {
    return c.json({ message: "Hello World" });
  })
  .route("/auth", authController)
  .use(authMiddleware)
  .get("/test", async (c) => {
    return c.json(c.get("user"));
  });

const port = 3000;
console.log(`Server is running on http://localhost:${port}`);

serve({
  fetch: app.fetch,
  port,
});
