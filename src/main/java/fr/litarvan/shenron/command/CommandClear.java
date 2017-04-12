package fr.litarvan.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandClear implements CommandHandler
{
    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        context.getMessage().delete().complete();

        if (!context.getChannel().getGuild().getMember(context.getUser()).hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Vous n'avez pas la permission de supprimer les messages")).queue();
            return;
        }

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
