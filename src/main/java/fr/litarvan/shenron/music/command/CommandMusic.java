package fr.litarvan.shenron.music.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;

@GuildOnly
@Command(value = "music", desc = "Affiche les informations de la musique actuelle")
public class CommandMusic implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MusicPlayer player = MusicPlayer.from(context.getGuild());
        AudioTrack track = player.getPlayer().getPlayingTrack();

        if (track == null)
        {
            return context.warn("Erreur", "Aucune musique n'est actuellement jouée");
        }

        return context.info("Musique actuelle", track.getInfo().title + " [" + MusicModule.parseTime(track.getPosition()) + "/" + MusicModule.parseTime(track.getDuration()) + "]");
    }
}
