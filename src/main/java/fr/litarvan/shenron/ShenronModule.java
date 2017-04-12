package fr.litarvan.shenron;

import com.google.inject.AbstractModule;

public class ShenronModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(fr.litarvan.krobot.ExceptionHandler.class).to(ExceptionHandler.class);
    }
}
