package fr.litarvan.shenron.command;

import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.util.MessageUtils;

public class CommandClear implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        context.getMessage().delete().complete();

        int amount = args.get("amount");

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
                context.getChannel().deleteMessages(messages).queue();
            }

            amount -= messages.size();
        }

        MessageUtils.deleteAfter(context.info("Done", "✅").get(), 1500);

        return null;
    }
}
