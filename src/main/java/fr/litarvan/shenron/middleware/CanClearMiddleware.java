package fr.litarvan.shenron.middleware;

import org.krobot.command.Command;
import org.krobot.command.CommandContext;
import org.krobot.command.Middleware;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.Nullable;

public class CanClearMiddleware implements Middleware
{
    @Inject
    private JDA jda;

    @Override
    public boolean handle(Command command, @Nullable Map<String, SuppliedArgument> args, CommandContext context)
    {
        if (!context.getMember().hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Vous n'avez pas la permission de supprimer les messages")).queue();
            return false;
        }

        if (!context.getGuild().getMember(jda.getSelfUser()).hasPermission(context.getChannel(), Permission.MESSAGE_MANAGE))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Shenron n'a pas la permission de supprimer les messages")).queue();
            return false;
        }

        return true;
    }
}
