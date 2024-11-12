import { Hono } from 'hono'
import type { HonoContext } from '../types/hono-context.js'

export const userController = new Hono<HonoContext>()
	.get('/profile', async (c) => {
		const { db, user } = c.var

		const users = await db.user.findFirst({
			where: {
				id: user.id,
			},
			select: {
				id: true,
				name: true,
				email: true,
			},
		})

		return c.json(users)
	})
	.put('/profile', async (c) => {
		const { db, user } = c.var

		const userFields = await c.req.json()

		const updatedUser = await db.user.update({
			where: {
				id: user.id,
			},
			data: {
				...userFields,
			},
		})

		return c.json(updatedUser)
	})
