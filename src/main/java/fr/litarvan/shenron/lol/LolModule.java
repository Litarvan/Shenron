package fr.litarvan.shenron.lol;

import org.krobot.KrobotModule;
import org.krobot.module.Include;

@Include(
    commands = {
        GameCommand.class
    }
)
public class LolModule extends KrobotModule
{
    @Override
    public void preInit()
    {
        config("config/lol.json")
            .defaultIn().classpath("/lol.default.json");
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
