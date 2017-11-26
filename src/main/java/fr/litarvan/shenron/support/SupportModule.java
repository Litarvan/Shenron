package fr.litarvan.shenron.support;

import fr.litarvan.shenron.support.command.*;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.krobot.KrobotModule;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;
import org.krobot.runtime.KrobotRuntime;

@Include(
    commands = {
        FAQCommand.class,
        SetupCommand.class
    }
)
public class SupportModule extends KrobotModule
{
    @Inject
    private ConfigProvider config;

    @Override
    public void preInit()
    {
        config("config/support.json")
            .defaultIn().classpath("/support.default.json");
    }

    @Override
    public void init()
    {
        if (config.at("support.enabled", boolean.class))
        {
            when(context -> !context.getGuild().getId().equals(config.at("support.id")))
                .disable();
        }
    }

    @Override
    public void postInit()
    {
        if (!config.at("support.enabled", boolean.class))
        {
            return;
        }

        Guild guild = jda().getGuildById(config.at("support.id"));

        for (TextChannel channel : guild.getTextChannels())
        {
            if (channel.getName().contains("private"))
            {
                channel.sendMessage(KrobotRuntime.get().getPrefix() + "setup").queue();
            }
        }
    }
}
