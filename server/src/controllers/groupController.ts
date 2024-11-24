import { Hono } from "hono";
import type { HonoContext } from "../types/hono-context.js";
import { groupMemberMiddleware } from "../middlewares/group-member.js";

export const groupController = new Hono<HonoContext>()
  .get("/", async (c) => {
    const { db, user } = c.var;

    const groups = await db.group.findMany({
      where: {
        members: {
          some: {
            userId: user.id,
          },
        },
      },
      include: {
        members: true,
        expenses: true,
      },
    });

    return c.json(groups);
  })
  .post("/", async (c) => {
    const { db, user } = c.var;

    const { name, description, participantEmails } = await c.req.json<{
      name: string;
      description: string | undefined;
      participantEmails: string[] | undefined;
    }>();

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
    });

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
    });

    const newGroup = await db.group.findFirst({
      where: {
        id: group.id,
      },
      include: {
        members: true,
        expenses: true,
      },
    });

    return c.json(newGroup);
  })
  .use("/:groupId", async (c, next) => {
    const groupIdMiddlewareHandler = groupMemberMiddleware(
      Number.parseInt(c.req.param("groupId")),
    );

    return groupIdMiddlewareHandler(c, next);
  })
  .get("/:groupId", async (c) => {
    const { db, user } = c.var;

    const group = await db.group.findFirst({
      where: {
        id: Number.parseInt(c.req.param("groupId")),
        members: {
          some: {
            userId: user.id,
          },
        },
      },
      include: {
        members: {
          include: {
            user: true,
          },
        },
        expenses: true,
      },
    });

    return c.json(group);
  })
  .delete("/:groupId", async (c) => {
    const { db, user } = c.var;

    const group = await db.group.findFirst({
      where: {
        id: Number.parseInt(c.req.param("groupId")),
        members: {
          some: {
            userId: user.id,
          },
        },
      },
    });

    if (!group) {
      return c.json({ message: "You are not a member of this group" }, 401);
    }

    await db.group.delete({
      where: {
        id: Number.parseInt(c.req.param("groupId")),
      },
    });

    return c.json({ message: "Group deleted successfully" });
  })
  .post("/:groupId/members", async (c) => {
    const { db, user } = c.var;

    const groupId = Number.parseInt(c.req.param("groupId"));

    const { userIds } = await c.req.json<{
      userIds: number[];
    }>();

    const group = await db.group.findFirst({
      where: {
        id: groupId,
        members: {
          some: {
            userId: user.id,
          },
        },
      },
    });

    if (!group) {
      return c.json({ message: "You are not a member of this group" }, 401);
    }

    const members = await db.groupMember.createMany({
      data: userIds.map((userId) => ({
        groupId,
        userId,
      })),
    });

    return c.json(members);
  })
  .delete("/:groupId/members", async (c) => {
    const { db, user } = c.var;

    const groupId = Number.parseInt(c.req.param("groupId"));

    const { userEmails } = await c.req.json<{
      userEmails: string[];
    }>();

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
    });

    const group = await db.group.findFirst({
      where: {
        id: groupId,
        members: {
          some: {
            userId: user.id,
          },
        },
      },
    });

    if (!group) {
      return c.json({ message: "You are not a member of this group" }, 401);
    }

    const members = await db.groupMember.deleteMany({
      where: {
        groupId,
        userId: {
          in: removedMembers.map((member) => member.id),
        },
      },
    });

    return c.json(members);
  })
  .put("/:groupId/members", async (c) => {
    const { db, user } = c.var;

    const groupId = Number.parseInt(c.req.param("groupId"));

    const { userEmails } = await c.req.json<{
      userEmails: string[];
    }>();

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
    });

    const group = await db.group.findFirst({
      where: {
        id: groupId,
        members: {
          some: {
            userId: user.id,
          },
        },
      },
    });

    if (!group) {
      return c.json({ message: "You are not a member of this group" }, 401);
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
    });

    return c.json(members);
  })
  .get("/:groupId/debts", async (c) => {
    const { db, user } = c.var;
    const groupId = Number.parseInt(c.req.param("groupId"));

    // Check if the user is a member of the group
    const isMember = await db.groupMember.findFirst({
      where: { groupId, userId: user.id },
    });

    if (!isMember) {
      return c.json({ message: "You are not a member of this group" }, 401);
    }

    // Fetch expenses and participants
    const expenses = await db.expense.findMany({
      where: {
        groupExpenses: { some: { groupId } },
      },
      include: {
        payer: true,
        participants: {
          include: {
            user: true,
          },
        },
      },
    });

    // Calculate net balance per user
    const userBalances: { [userId: number]: number } = {};
    const transactions: {
      expenseId: number;
      title: string;
      amount: number;
      date: Date;
      payer: string;
      netBalance: number;
    }[] = [];

    for (const expense of expenses) {
      for (const participant of expense.participants) {
        const userId = participant.userId;
        const net = participant.paidAmount - participant.owedAmount;

        userBalances[userId] = (userBalances[userId] || 0) + net;

        if (userId === user.id) {
          transactions.push({
            expenseId: expense.id,
            title: expense.title,
            amount: expense.amount,
            date: expense.date,
            payer: expense.payer.name,
            netBalance: net,
          });
        }
      }
    }

    // Prepare response
    const balance = userBalances[user.id] || 0;
    const status = balance >= 0 ? "owed" : "owing";

    return c.json({
      userId: user.id,
      totalBalance: Math.abs(balance),
      netBalance: balance,
      expenses: expenses.map((expense) => ({
        id: expense.id,
        title: expense.title,
        amount: expense.amount,
        date: expense.date,
        payer: expense.payer.name,
        participants: expense.participants.map((participant) => ({
          userId: participant.userId,
          name: participant.user.name,
          email: participant.user.email,
          paidAmount: participant.paidAmount,
          owedAmount: participant.owedAmount,
          isPaid: participant.paidAmount > participant.owedAmount,
        })),
        isPaid: expense.participants.some(
          (participant) => participant.paidAmount > 0,
        ),
      })),
      status,
      transactions,
    });
  })
  .get("/:groupId/expenses", async (c) => {
    const { db, user } = c.var;

    const groupId = Number.parseInt(c.req.param("groupId"));

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
    });

    if (!group) {
      return c.json({ message: "You are not a member of this group" }, 401);
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
                owedAmount: true,
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
    });

    return c.json(expenses);
  })
  .post("/:groupId/expenses", async (c) => {
    const { db } = c.var;
    const groupId = Number.parseInt(c.req.param("groupId"));

    const { amount, date, payerEmail, title, participantEmails } =
      await c.req.json<{
        amount: number;
        date: string | undefined;
        payerEmail: string;
        title: string;
        participantEmails?: string[];
      }>();

    // Fetch payer and participants
    const payer = await db.user.findFirst({ where: { email: payerEmail } });
    const participants = participantEmails
      ? await db.user.findMany({ where: { email: { in: participantEmails } } })
      : await db.groupMember
          .findMany({ where: { groupId }, include: { user: true } })
          .then((members) => members.map((m) => m.user));

    if (!payer || !participants.length) {
      return c.json({ message: "Payer or participants not found" }, 400);
    }

    // Calculate owed amount per participant
    const splitAmount = Math.round(amount / participants.length);

    const expense = await db.groupExpense.create({
      data: {
        expense: {
          create: {
            amount,
            date: date ?? new Date(),
            title,
            payer: { connect: { id: payer.id } },
            attachments: {
              createMany: {
                data: [],
              },
            },
            participants: {
              createMany: {
                data: participants.map((participant) => ({
                  userId: participant.id,
                  paidAmount: participant.id === payer.id ? amount : 0,
                  owedAmount: splitAmount,
                })),
              },
            },
          },
        },
        group: { connect: { id: groupId } },
      },
    });

    return c.json(expense);
  })
  .get("/:groupId/expenses/:expenseId", async (c) => {
    const { db, user } = c.var;

    const groupId = Number.parseInt(c.req.param("groupId"));

    const expense = await db.groupExpense.findFirst({
      where: {
        id: Number.parseInt(c.req.param("expenseId")),
        groupId,
      },
      include: {
        expense: true,
      },
    });

    if (!expense) {
      return c.json({ message: "Expense not found" }, 401);
    }

    return c.json(expense);
  })
  .delete("/:groupId/expenses/:expenseId", async (c) => {
    return c.json({ message: "Not implemented" });
  })
  .put("/:groupId/expenses/:expenseId/pay", async (c) => {
    const { db, user } = c.var;
    const groupId = Number.parseInt(c.req.param("groupId"));
    const expenseId = Number.parseInt(c.req.param("expenseId"));

    const { amount } = await c.req.json<{ amount: number }>();

    // Fetch the expense, ensuring it belongs to the group
    const expense = await db.expense.findFirst({
      where: {
        id: expenseId,
        groupExpenses: {
          some: {
            groupId: groupId,
          },
        },
      },
      include: {
        participants: true,
      },
    });

    if (!expense) {
      return c.json({ message: "Expense not found" }, 404);
    }

    if (amount > expense.amount) {
      return c.json({ message: "Amount exceeds expense amount" }, 400);
    }

    // Verify that the user is a participant of the expense
    const expenseParticipant = await db.expenseParticipants.findFirst({
      where: {
        expenseId,
        userId: user.id,
      },
    });

    if (!expenseParticipant) {
      return c.json(
        { message: "You are not a participant of this expense" },
        403,
      );
    }

    // Update the paid amount
    await db.expenseParticipants.update({
      where: { id: expenseParticipant.id },
      data: { paidAmount: amount },
    });

    return c.json({ message: "Payment updated successfully" });
  })
  .post("/:groupId/expenses/:expenseId/reminder", async (c) => {
    const { db, user } = c.var;
    const groupId = Number.parseInt(c.req.param("groupId"));
    const expenseId = Number.parseInt(c.req.param("expenseId"));

    // Fetch the expense, ensuring it belongs to the group
    const expense = await db.expense.findFirst({
      where: {
        id: expenseId,
        groupExpenses: {
          some: {
            groupId: groupId,
          },
        },
      },
      include: {
        participants: {
          include: {
            user: true,
          },
        },
        payer: true,
      },
    });

    if (!expense) {
      return c.json({ message: "Expense not found" }, 404);
    }

    // Verify the requester is the payer
    if (expense.payerId !== user.id) {
      return c.json(
        { message: "Only the payer can send reminders for this expense" },
        403,
      );
    }

    // Identify participants who owe money
    const owingParticipants = expense.participants.filter(
      (participant) => participant.owedAmount > participant.paidAmount,
    );

    if (owingParticipants.length === 0) {
      return c.json({ message: "No participants owe money for this expense" });
    }

    // Create reminders for owing participants
    const reminders = await Promise.all(
      owingParticipants.map((participant) =>
        db.notification.create({
          data: {
            senderId: user.id,
            recipientId: participant.userId,
            expenseId: expense.id,
            message: `Reminder: You owe ${participant.owedAmount - participant.paidAmount} for the expense "${expense.title}".`,
          },
        }),
      ),
    );

    return c.json({
      message: "Reminders sent successfully",
      reminders,
    });
  });
