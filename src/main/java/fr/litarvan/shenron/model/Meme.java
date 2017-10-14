package fr.litarvan.shenron.model;

public class Meme
{
    private String name;
    private String command;
    private String link;

    public Meme(String name, String command, String link)
    {
        this.name = name;
        this.command = command;
        this.link = link;
    }

    public String getName()
    {
        return name;
    }

    public String getCommand()
    {
        return command;
    }

    public String getLink()
    {
        return link;
    }
}
