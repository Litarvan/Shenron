package fr.litarvan.shenron.command;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.util.MessageUtils;

public class CommandClearWhere implements CommandHandler
{
    private boolean after;

    public CommandClearWhere(boolean after)
    {
        this.after = after;
    }

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        context.getMessage().delete().submit().get();

        List<Message> messages = context.getChannel().getHistory().retrievePast(100).complete();
        messages.remove(0);

        Message from = MessageUtils.search(context.getChannel(), args.get("query"), 500);

        if (from == null)
        {
            return context.warn("Erreur", "Impossible de trouver le message");
        }

        int amount = args.get("amount");
        MessageHistory history = context.getChannel().getHistoryAround(from, amount).complete();

        messages = new ArrayList<>();

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

            if (toRetrieve == 0)
            {
                break;
            }

            history = context.getChannel().getHistoryAround(last == null ? from : last, toRetrieve > 100 ? 100 : toRetrieve).complete();
        }

        if (messages.size() == 1)
        {
            messages.get(0).delete().queue();
        }
        else if (messages.size() != 0)
        {
            context.getChannel().deleteMessages(messages).queue();
        }

        MessageUtils.deleteAfter(context.info("Done", "✅").get(), 1500);

        return null;
    }
}
