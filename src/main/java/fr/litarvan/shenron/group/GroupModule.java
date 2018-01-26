package fr.litarvan.shenron.group;

import org.krobot.KrobotModule;
import org.krobot.module.Include;

@Include(
    listeners = {
        GroupListener.class
    }
)
public class GroupModule extends KrobotModule
{
    @Override
    public void preInit()
    {
        config("config/groups.json")
                .defaultIn().classpath("/groups.default.json");
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
