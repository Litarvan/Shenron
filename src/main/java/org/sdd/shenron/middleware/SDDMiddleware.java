package org.sdd.shenron.middleware;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.Middleware;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;

public class SDDMiddleware implements Middleware
{
    @Inject
    private ConfigProvider config;

    @Override
    public boolean handle(Command command, Map<String, SuppliedArgument> args, CommandContext context)
    {
        return context.getChannel().getGuild().getName().equals(config.at("shenron.sdd"));
    }
}
