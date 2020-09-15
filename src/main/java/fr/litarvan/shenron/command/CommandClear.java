package fr.litarvan.shenron.command;

import java.util.List;
import java.util.concurrent.ExecutionException;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
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
public class CommandClear implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        context.getMessage().delete().complete();

        int amount = args.get("amount");

        if (amount <= 0) {
            return context.warn("Argument invalide", "Le nombre de message doit être supérieur à 0");
        }

        Interact.from(context.info("Supprimer des messages ?", "Êtes-vous sûr de vouloir supprimer " + amount + " messages ?").get(), context.getUser())
                .thenDelete()
                .on(Interact.YES, c -> delete(context, amount))
                .on(Interact.NO, c -> {});

        return null;
    }

    private void delete(MessageContext context, int amount)
    {
        while (amount != 0)
        {
            int toDelete = amount > 100 ? 100 : amount;
            List<Message> messages = context.getChannel().getHistory().retrievePast(toDelete).complete();

            if (messages.size() == 1)
            {
                messages.get(0).delete().queue();
            }
            else if (messages.size() == 0)
            {
                break;
            }
            else
            {
                ((TextChannel) context.getChannel()).deleteMessages(messages).queue();
            }

            amount -= messages.size();
        }

        try {
            MessageUtils.deleteAfter(context.info("Done", "✅").get(), 1500);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
