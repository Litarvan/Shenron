package fr.litarvan.shenron.command;

import net.dv8tion.jda.core.Permission;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import java.util.Map;
import fr.litarvan.shenron.MessageSudo;
import org.jetbrains.annotations.NotNull;
import org.krobot.permission.BotRequires;

@BotRequires({Permission.MANAGE_WEBHOOKS})
public class CommandSimpleLink implements CommandHandler
{
    private String link;

    public CommandSimpleLink(String link)
    {
        this.link = link;
    }

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        MessageSudo.send(context.getUser(), context.getChannel(), link);
    }

    public String getLink()
    {
        return link;
    }
}
