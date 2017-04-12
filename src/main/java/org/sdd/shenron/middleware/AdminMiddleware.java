package org.sdd.shenron.middleware;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.Middleware;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang3.ArrayUtils;

public class AdminMiddleware implements Middleware
{
    @Inject
    private ConfigProvider config;

    @Override
    public boolean handle(Command command, Map<String, SuppliedArgument> args, CommandContext context)
    {
        if (!ArrayUtils.contains(config.get("admins").at("main", String[].class), context.getUser().getId()))
        {
            context.getChannel().sendMessage(Dialog.error("Unauthorized", "Only an administrator can do that")).queue();
            return false;
        }

        return true;
    }
}
