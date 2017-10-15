package fr.litarvan.shenron;

import org.krobot.Krobot;

public class Main
{
    public static void main(String[] args)
    {
        Krobot.create()
            .readTokenFromArgs(args)
            .saveTokenIn(".token")
            .run(Shenron.class);
    }
}
