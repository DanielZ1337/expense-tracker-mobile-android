import { Hono } from 'hono'
import type { HonoContext } from '../types/hono-context.js'
import { groupMemberMiddleware } from '../middlewares/group-member.js'

export const groupController = new Hono<HonoContext>()
	.get('/', (c) => {
		const { db, user } = c.var

		const groups = db.group.findMany({
			where: {
				members: {
					some: {
						userId: user.id,
					},
				},
			},
			select: {
				id: true,
				name: true,
				description: true,
			},
		})

		return c.json(groups)
	})
	.post('/', async (c) => {
		const { db, user } = c.var

		const { name, description, participantEmails } = await c.req.json<{
			name: string
			description: string | undefined
			participantEmails: string[] | undefined
		}>()

		const otherParticipants = await db.user.findMany({
			where: {
				email: {
					in: participantEmails,
				},
				id: {
					not: user.id,
				},
			},
			select: {
				id: true,
				name: true,
				email: true,
			},
		})

		const group = await db.group.create({
			data: {
				name,
				description,
				members: {
					createMany: {
						data: [
							{
								userId: user.id,
							},
							...otherParticipants.map((participant) => ({
								userId: participant.id,
							})),
						],
					},
				},
			},
		})

		return c.json(group)
	})
	.use('/:groupId', async (c, next) => {
		const groupIdMiddlewareHandler = groupMemberMiddleware(Number.parseInt(c.req.param('groupId')))

		return groupIdMiddlewareHandler(c, next)
	})
	.get('/:groupId', async (c) => {
		const { db, user } = c.var

		const group = await db.group.findFirst({
			where: {
				id: Number.parseInt(c.req.param('groupId')),
				members: {
					some: {
						userId: user.id,
					},
				},
			},
			select: {
				id: true,
				name: true,
				description: true,
			},
		})

		return c.json(group)
	})
	.delete('/:groupId', async (c) => {
		const { db, user } = c.var

		const group = await db.group.findFirst({
			where: {
				id: Number.parseInt(c.req.param('groupId')),
				members: {
					some: {
						userId: user.id,
					},
				},
			},
		})

		if (!group) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		await db.group.delete({
			where: {
				id: Number.parseInt(c.req.param('groupId')),
			},
		})

		return c.json({ message: 'Group deleted successfully' })
	})
	.post('/:groupId/members', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const { userIds } = await c.req.json<{
			userIds: number[]
		}>()

		const group = await db.group.findFirst({
			where: {
				id: groupId,
				members: {
					some: {
						userId: user.id,
					},
				},
			},
		})

		if (!group) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		const members = await db.groupMember.createMany({
			data: userIds.map((userId) => ({
				groupId,
				userId,
			})),
		})

		return c.json(members)
	})
	.delete('/:groupId/members', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const { userEmails } = await c.req.json<{
			userEmails: string[]
		}>()

		const removedMembers = await db.user.findMany({
			where: {
				email: {
					in: userEmails,
				},
				id: {
					not: user.id,
				},
			},
			select: {
				id: true,
				name: true,
				email: true,
			},
		})

		const group = await db.group.findFirst({
			where: {
				id: groupId,
				members: {
					some: {
						userId: user.id,
					},
				},
			},
		})

		if (!group) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		const members = await db.groupMember.deleteMany({
			where: {
				groupId,
				userId: {
					in: removedMembers.map((member) => member.id),
				},
			},
		})

		return c.json(members)
	})
	.put('/:groupId/members', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const { userEmails } = await c.req.json<{
			userEmails: string[]
		}>()

		const addedMembers = await db.user.findMany({
			where: {
				email: {
					in: userEmails,
				},
				id: {
					not: user.id,
				},
			},
			select: {
				id: true,
				name: true,
				email: true,
			},
		})

		const group = await db.group.findFirst({
			where: {
				id: groupId,
				members: {
					some: {
						userId: user.id,
					},
				},
			},
		})

		if (!group) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		const members = await db.group.update({
			where: {
				id: groupId,
			},
			data: {
				members: {
					createMany: {
						data: addedMembers.map((member) => ({
							userId: member.id,
						})),
					},
				},
			},
		})

		return c.json(members)
	})
	.get('/:groupId/expenses', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const group = await db.group.findFirst({
			where: {
				id: groupId,
				members: {
					some: {
						userId: user.id,
					},
				},
			},
			select: {
				id: true,
				name: true,
				description: true,
			},
		})

		if (!group) {
			return c.json({ message: 'You are not a member of this group' }, 401)
		}

		const expenses = await db.groupExpense.findMany({
			where: {
				groupId,
			},
			select: {
				id: true,
				expense: {
					select: {
						id: true,
						title: true,
						amount: true,
						date: true,
						payer: {
							select: {
								id: true,
								name: true,
								email: true,
							},
						},
						participants: {
							select: {
								user: {
									select: {
										id: true,
										name: true,
										email: true,
									},
								},
								paidAmount: true,
							},
						},
					},
				},
				group: {
					select: {
						name: true,
					},
				},
			},
		})

		return c.json(expenses)
	})
	.post('/:groupId/expenses', async (c) => {
		const { db } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const { amount, date, payerEmail, title } = await c.req.json<{
			amount: number
			date: string | undefined
			payerEmail: string
			title: string
		}>()

		const payer = await db.user.findFirst({
			where: {
				email: payerEmail,
			},
			select: {
				id: true,
				name: true,
				email: true,
			},
		})

		if (!payer) {
			return c.json({ message: 'Payer not found' }, 401)
		}

		const members = await db.groupMember.findMany({
			where: {
				groupId,
			},
		})

		const expense = await db.groupExpense.create({
			data: {
				expense: {
					create: {
						amount,
						date: date ?? new Date(),
						title,
						payer: {
							connect: {
								id: payer.id,
							},
						},
						attachments: {
							createMany: {
								data: [],
							},
						},
						participants: {
							createMany: {
								data: members.map((member) => ({
									userId: member.userId,
									paidAmount: 0,
								})),
							},
						},
					},
				},
				group: {
					connect: {
						id: groupId,
					},
				},
			},
		})

		return c.json(expense)
	})
	.get('/:groupId/expenses/:expenseId', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const expense = await db.groupExpense.findFirst({
			where: {
				id: Number.parseInt(c.req.param('expenseId')),
				groupId,
			},
			include: {
				expense: true,
			},
		})

		if (!expense) {
			return c.json({ message: 'Expense not found' }, 401)
		}

		return c.json(expense)
	})
	.delete('/:groupId/expenses/:expenseId', async (c) => {
		return c.json({ message: 'Not implemented' })
	})
	.put('/:groupId/expenses/:expenseId/pay', async (c) => {
		const { db, user } = c.var

		const groupId = Number.parseInt(c.req.param('groupId'))

		const { amount } = await c.req.json<{
			amount: number
		}>()

		const expense = await db.groupExpense.findFirst({
			where: {
				id: Number.parseInt(c.req.param('expenseId')),
				groupId,
			},
			include: {
				expense: true,
			},
		})

		if (!expense) {
			return c.json({ message: 'Expense not found' }, 401)
		}

		if (amount > expense.expense.amount) {
			return c.json({ message: 'Amount exceeds expense amount' }, 401)
		}

		const expenseParticipants = await db.expenseParticipants.findFirst({
			where: {
				expenseId: expense.id,
				userId: user.id,
			},
		})

		if (!expenseParticipants) {
			return c.json({ message: 'You are not a participant of this expense' }, 401)
		}

		await db.expenseParticipants.update({
			where: {
				id: expenseParticipants.id,
			},
			data: {
				paidAmount: amount,
			},
		})

		return c.json({ message: 'Payment updated successfully' })
	})
