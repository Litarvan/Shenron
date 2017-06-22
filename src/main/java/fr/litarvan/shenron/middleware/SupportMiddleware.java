package fr.litarvan.shenron.middleware;

import org.krobot.command.Command;
import org.krobot.command.CommandContext;
import org.krobot.command.Middleware;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;

public class SupportMiddleware implements Middleware
{
    @Inject
    private ConfigProvider config;

    @Override
    public boolean handle(Command command, Map<String, SuppliedArgument> args, CommandContext context)
    {
        return context.getGuild().getName().equals(config.at("support.name"));
    }
}
