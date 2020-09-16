package fr.litarvan.shenron.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.Interact;
import org.krobot.util.MessageUtils;

@GuildOnly
@UserRequires({ Permission.MESSAGE_MANAGE })
@BotRequires({ Permission.MESSAGE_MANAGE })
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
        context.getMessage().delete().complete();
        return clearWhere(this.after, context, args.get("amount"), args.get("query"), true);
    }

    public static Object clearWhere(boolean after, MessageContext context, int amount, String query, boolean confirm)
    {
        List<Message> messages = context.getChannel().getHistory().retrievePast(100).complete();
        messages.remove(0);

        Message from = MessageUtils.search((TextChannel) context.getChannel(), query, 500);

        if (from == null)
        {
            return context.warn("Erreur", "Impossible de trouver le message");
        }

        if (amount <= 0)
        {
            return context.warn("Argument invalide", "Le nombre de message doit être supérieur à 0");
        }

        MessageHistory history = context.getChannel().getHistoryAround(from, amount).complete();

        messages = new ArrayList<>();

        while (messages.size() < amount)
        {
            Message last = null;
            int lastSize = messages.size();

            for (Message message : history.getRetrievedHistory())
            {
                if (!messages.contains(message) && (after ? message.getTimeCreated().isAfter(from.getTimeCreated()) : message.getTimeCreated().isBefore(from.getTimeCreated())))
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

        String message = "Voulez-vous supprimer " + messages.size() + " message" + (messages.size() > 1 ? "s" : "") + " ?";
        if (messages.size() > 1)
        {
            String firstContent = messages.get(messages.size() - 1).getContentDisplay();
            String lastContent = messages.get(0).getContentDisplay();

            message += "\n\nDe '" + firstContent.substring(0, Math.min(10, firstContent.length())) + (firstContent.length() > 10 ? "..." : "") + "'";
            message += "\nÀ '" + lastContent.substring(0, Math.min(10, lastContent.length())) + (lastContent.length() > 10 ? "..." : "") + "'";
        }

        List<Message> finalMessages = messages;
        Consumer<MessageContext> apply = c ->
        {
            if (finalMessages.size() == 1)
            {
                finalMessages.get(0).delete().queue();
            }
            else if (finalMessages.size() != 0)
            {
                ((TextChannel) context.getChannel()).deleteMessages(finalMessages).queue();
            }

            MessageUtils.deleteAfter(context.info("Done", "✅").join(), 1500);
        };

        if (confirm)
        {
            Interact.from(context.info("Supprimer des messages ?", message).join(), context.getUser())
                    .thenDelete()
                    .on(Interact.YES, apply)
                    .on(Interact.NO, c -> {});
        }
        else
        {
            apply.accept(context);
        }

        return null;
    }
}
