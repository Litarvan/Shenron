package fr.litarvan.shenron;

import org.krobot.KrobotRunner;

public class Main
{
    public static void main(String[] args)
    {
        KrobotRunner runner = new KrobotRunner();
        runner.readTokenFromArgs(args).saveTokenIn(".token");

        if (System.console() == null)
        {
            runner.disableStateBar();
        }

        runner.run(Shenron.class);
    }
}
