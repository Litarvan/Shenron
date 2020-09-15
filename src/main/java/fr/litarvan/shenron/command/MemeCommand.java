package fr.litarvan.shenron.command;

import fr.litarvan.shenron.model.Meme;
import fr.litarvan.shenron.util.MessageSudo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;
import org.krobot.command.NoTyping;
import org.krobot.permission.BotRequires;

@GuildOnly
@NoTyping
@BotRequires({ Permission.MANAGE_WEBHOOKS })
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
        MessageSudo.send(context.getUser(), (TextChannel) context.getChannel(), meme.getLink());
        return null;
    }
}
