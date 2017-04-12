package org.sdd.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import java.util.Map;
import org.sdd.shenron.MessageSudo;

public class CommandSimpleLink implements CommandHandler
{
    private String link;

    public CommandSimpleLink(String link)
    {
        this.link = link;
    }

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        MessageSudo.send(context.getUser(), context.getChannel(), link);
    }

    public String getLink()
    {
        return link;
    }
}
