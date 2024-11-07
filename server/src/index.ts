import { serve } from "@hono/node-server";
import { Hono } from "hono";
import { authController } from "./auth.js";
import { dbMiddleware } from "./middlewares/db.js";

const app = new Hono()
  .basePath("/api")
  .use(dbMiddleware)
  .get("/", (c) => {
    return c.json({ message: "Hello World" });
  })
  .route("/auth", authController);

const port = 3000;
console.log(`Server is running on http://localhost:${port}`);

serve({
  fetch: app.fetch,
  port,
});
