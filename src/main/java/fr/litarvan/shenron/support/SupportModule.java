package fr.litarvan.shenron.support;

import fr.litarvan.shenron.support.command.*;
import org.krobot.KrobotModule;
import org.krobot.module.Include;

@Include(
    commands = {
        FAQCommand.class
    }
)
public class SupportModule extends KrobotModule
{
    @Override
    public void preInit()
    {
        config("config/support.json")
            .defaultIn().classpath("/support.default.json");
    }

    @Override
    public void init()
    {
    }

    @Override
    public void postInit()
    {
    }
}
