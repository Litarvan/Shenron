package fr.litarvan.shenron.group;

import javax.inject.Inject;
import org.krobot.KrobotModule;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;

@Include(
    listeners = {
        GroupListener.class
    }
)
public class GroupModule extends KrobotModule
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
