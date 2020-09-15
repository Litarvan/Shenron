package fr.litarvan.shenron.music.command;

import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.*;

@GuildOnly
@NoTyping
@Command(value = "disconnect", desc = "Déconnecte le bot du channel vocal", aliases = "d")
public class CommandDisconnect implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        if (!context.getGuild().getAudioManager().isConnected())
        {
            return context.warn("Erreur", "Le bot n'est pas dans un channel vocal");
        }

        MusicPlayer.from(context.getGuild()).stop();
        context.getGuild().getAudioManager().closeAudioConnection();

        return null;
    }
}
