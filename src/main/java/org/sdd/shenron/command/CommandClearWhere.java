package org.sdd.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

public class CommandClearWhere implements CommandHandler
{
    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
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
            context.getChannel().sendMessage(Dialog.info("Erreur", "Impossible de trouver le message")).queue();
            return;
        }

        int amount = args.get("amount").getAsNumber();
        MessageHistory history = context.getChannel().getHistoryAround(from, amount).complete();
        boolean after = args.get("where").getAsString().equals("after");

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
