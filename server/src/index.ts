import { serve } from '@hono/node-server'
import { Hono } from 'hono'
import { authController } from './controllers/authController.js'
import { dbMiddleware } from './middlewares/db.js'
import { config } from 'dotenv'
import { authMiddleware } from './middlewares/auth.js'
import { userController } from './controllers/userController.js'
import { groupController } from './controllers/groupController.js'

config()

const app = new Hono()
	.basePath('/api')
	.use(dbMiddleware)
	.route('/auth', authController)
	.use(authMiddleware)
	.route('/users', userController)
	.route('/groups', groupController)

const port = 3000
console.log(`Server is running on http://localhost:${port}`)

serve({
	fetch: app.fetch,
	port,
})
