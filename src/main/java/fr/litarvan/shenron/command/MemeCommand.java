package fr.litarvan.shenron.command;

import fr.litarvan.shenron.model.Meme;
import fr.litarvan.shenron.util.MessageSudo;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.command.NoTyping;

@NoTyping
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
