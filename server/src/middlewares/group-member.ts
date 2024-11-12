import { createMiddleware } from 'hono/factory'
import type { HonoContext } from '../types/hono-context.js'

export const groupMemberMiddleware = (groupId: number) =>
	createMiddleware<HonoContext>(async (c, next) => {
		const { db, user } = c.var

		const groupMember = await db.groupMember.findFirst({
			where: {
				groupId,
				userId: user.id,
			},
		})

		if (!groupMember) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		await next()
	})
