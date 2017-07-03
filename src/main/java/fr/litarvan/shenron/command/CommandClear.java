package fr.litarvan.shenron.command;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import org.jetbrains.annotations.NotNull;

public class CommandClear implements CommandHandler
{
    @Inject
    private JDA jda;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        if (!context.getMember().hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Vous n'avez pas la permission de supprimer les messages")).queue();
            return;
        }

        if (!context.getGuild().getMember(jda.getSelfUser()).hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Shenron n'a pas la permission de supprimer les messages")).queue();
            return;
        }

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
