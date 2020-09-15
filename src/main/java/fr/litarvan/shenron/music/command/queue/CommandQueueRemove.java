package fr.litarvan.shenron.music.command.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicPlayer;
import java.util.List;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;

@GuildOnly
@Command(value = "remove <entry:number>", desc = "Supprime une entrée de la file d'attente", aliases = {"r", "remoev", "ermove"})
public class CommandQueueRemove implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        List<AudioTrack> tracks = MusicPlayer.from(context.getGuild()).getPlaylist();
        int entry = args.get("entry");

        if (entry > tracks.size())
        {
            return context.warn("Erreur", "Le nombre donné ('" + entry + "') est trop grand (la playlist contient '" + tracks.size() + "' chansons)");
        }

        AudioTrack deleted = tracks.remove(entry - 1);
        return context.info("Supprimé", "Musique '" + deleted.getInfo().title + "' supprimée de la playlist");
    }
}
