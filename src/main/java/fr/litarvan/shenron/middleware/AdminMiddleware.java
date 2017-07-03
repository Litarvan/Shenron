package fr.litarvan.shenron.middleware;

import org.krobot.command.Command;
import org.krobot.command.CommandContext;
import org.krobot.command.Middleware;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public class AdminMiddleware implements Middleware
{
    @Override
    public boolean handle(@NotNull Command command, Map<String, SuppliedArgument> args, @NotNull CommandContext context)
    {
        if (!context.getMember().hasPermission(Permission.ADMINISTRATOR))
        {
            context.sendMessage(Dialog.error("Non-autorisé", "Seul un administrateur peut faire ça"));
            return false;
        }

        return true;
    }
}
