package fr.litarvan.shenron.support;

import javax.inject.Inject;
import org.krobot.KrobotModule;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;

@Include(
    commands = {
        FAQCommand.class
    }
)
public class SupportModule extends KrobotModule
{
    @Inject
    private ConfigProvider configs;

    @Override
    public void preInit()
    {
    }

    @Override
    public void init()
    {
        when(context -> context.getGuild().getId().equals(configs.at("support.id")))
            .disable();
    }

    @Override
    public void postInit()
    {
    }
}
