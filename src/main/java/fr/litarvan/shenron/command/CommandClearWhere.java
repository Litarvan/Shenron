/*
 * Copyright 2016-2017 Adrien 'Litarvan' Navratil
 *
 * This file is part of Shenron.
 *
 * Shenron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shenron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shenron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.litarvan.shenron.command;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.Dialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import org.jetbrains.annotations.NotNull;

@UserRequires({Permission.MESSAGE_MANAGE})
@BotRequires({Permission.MESSAGE_MANAGE, Permission.MESSAGE_HISTORY})
public class CommandClearWhere implements CommandHandler
{
    private boolean after;

    public CommandClearWhere(boolean after)
    {
        this.after = after;
    }

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        context.getMessage().delete().submit().get();

        List<Message> messages = context.getChannel().getHistory().retrievePast(100).complete();
        messages.remove(0);

        Message from = null;

        for (Message message : messages)
        {
            if (message.getContent().toLowerCase().contains(args.get("query").getAsString().toLowerCase().trim()))
            {
                from = message;
                break;
            }
        }

        if (from == null)
        {
            context.sendMessage(Dialog.info("Erreur", "Impossible de trouver le message"));
            return;
        }

        int amount = args.get("amount").getAsNumber();
        MessageHistory history = context.getChannel().getHistoryAround(from, amount).complete();

        messages = new ArrayList<>();

        main:
        while (messages.size() < amount)
        {
            Message last = null;
            int lastSize = messages.size();

            for (Message message : history.getRetrievedHistory())
            {
                if (!messages.contains(message) && (after ? message.getCreationTime().isAfter(from.getCreationTime()) : message.getCreationTime().isBefore(from.getCreationTime())))
                {
                    messages.add(message);
                    last = message;
                }
            }

            if (lastSize == messages.size())
            {
                break;
            }

            int toRetrieve = (amount - messages.size()) * 2;

            if (toRetrieve == 0 /*|| (after && messages.contains(context.getChannel().getMessageById(context.getChannel().getLatestMessageId()).complete()))*/)
            {
                break main;
            }

            history = context.getChannel().getHistoryAround(last == null ? from : last, toRetrieve > 100 ? 100 : toRetrieve).complete();

            /*if (history.getRetrievedHistory().size() < toRetrieve)
            {
                amount -= (toRetrieve - history.getRetrievedHistory().size());
            }*/
        }

        if (messages.size() == 1)
        {
            messages.get(0).delete().queue();
        }
        else
        {
            context.getChannel().deleteMessages(messages).queue();
        }
    }
}
