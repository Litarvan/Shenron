package fr.litarvan.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.entities.Message;

public class CommandClear implements CommandHandler
{
    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        context.getMessage().delete().complete();

        int amount = args.get("amount").getAsNumber();

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
                return;
            }
            else
            {
                context.getChannel().deleteMessages(messages).queue();
            }

            amount -= messages.size();
        }
    }
}
