package fr.litarvan.shenron.sdd;

import fr.litarvan.shenron.sdd.command.RegisterCommand;
import javax.inject.Inject;
import org.krobot.KrobotModule;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;

@Include(
    commands = {
        RegisterCommand.class
    }
)
public class SDDModule extends KrobotModule
{
    @Inject
    private ConfigProvider configs;

    @Override
    public void preInit()
    {
        config("config/sdd.json")
                .defaultIn().classpath("/sdd.default.json");
    }

    @Override
    public void init()
    {
        when(context -> !context.getGuild().getId().equals(configs.at("sdd.id")))
            .disable();
    }

    @Override
    public void postInit()
    {
    }
}
