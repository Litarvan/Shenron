package fr.litarvan.shenron.web;

import fr.litarvan.paladin.App;
import fr.litarvan.shenron.Shenron;

public class ShenronWeb extends App
{
    @Override
    public void onStart()
    {
    }

    @Override
    public void onStop()
    {
    }

    @Override
    public String getName()
    {
        return "Shenron Web";
    }

    @Override
    public String getVersion()
    {
        return Shenron.VERSION;
    }
}
