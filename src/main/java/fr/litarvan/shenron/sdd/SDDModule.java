package fr.litarvan.shenron.sdd;

import fr.litarvan.shenron.sdd.command.RegisterCommand;
import fr.litarvan.shenron.util.Interact;
import javax.inject.Inject;
import org.krobot.KrobotModule;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;
import org.krobot.util.Dialog;

@Include(
    commands = {
        RegisterCommand.class
    },
    listeners = {
        GroupListener.class
    }
)
public class SDDModule extends KrobotModule
{
    @Inject
    private ConfigProvider configs;

    @Override
    public void preInit()
    {
        folder("config/")
            .configs("sdd", "groups")
            .withDefaultsIn().classpathFolder("/");
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
