package fr.litarvan.shenron.web;

import fr.litarvan.paladin.Paladin;
import fr.litarvan.paladin.PaladinBuilder;
import org.krobot.KrobotModule;

public class ShenronWebModule extends KrobotModule
{
    private Paladin paladin;

    @Override
    public void preInit()
    {
    }

    @Override
    public void init()
    {
    }

    @Override
    public void postInit()
    {
        this.paladin = PaladinBuilder.create(ShenronWeb.class)
                                     .setConfigFolder("config-web/")
                                     .setRoutesFile("/routes.groovy")
                                     .build();

        this.paladin.setExceptionHandler(new ShenronWebExceptionHandler(this.paladin));

        new Thread(() -> this.paladin.start()).start();
    }
}
