package fr.litarvan.shenron.command;

import fr.litarvan.shenron.model.Meme;
import fr.litarvan.shenron.util.MessageSudo;
import net.dv8tion.jda.core.Permission;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.command.NoTyping;
import org.krobot.permission.BotRequires;

@NoTyping
@BotRequires({Permission.MANAGE_WEBHOOKS })
public class MemeCommand implements CommandHandler
{
    private Meme meme;

    public MemeCommand(Meme meme)
    {
        this.meme = meme;
    }

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MessageSudo.send(context.getUser(), context.getChannel(), meme.getLink());
        return null;
    }
}
