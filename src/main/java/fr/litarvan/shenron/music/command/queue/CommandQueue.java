package fr.litarvan.shenron.music.command.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.util.Markdown;

@Command(value = "queue", desc = "Affiche la file d'attente actuelle", aliases = {"q", "quuee", "queeu", "uqueue"}, subs = {CommandQueueMode.class, CommandQueueRemove.class, CommandQueueClear.class})
public class CommandQueue implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        StringBuilder content = new StringBuilder();

        MusicPlayer player = MusicPlayer.from(context.getGuild());
        int i = 0;

        for (AudioTrack track : player.getPlaylist())
        {
            i++;
            boolean current = i == player.getCurrent();
            content.append(current ? "**" : "").append(i).append(". ").append(current ? "[En cours] " : "").append(track.getInfo().title).append(" (").append(MusicModule.parseTime(track.getDuration())).append(")").append(current ? "**" : "").append("\n");
        }

        return context.info("File d'attente, mode : " + Markdown.bold(player.isConserve() ? "Playlist" : "File d'attente"), content.toString());
    }
}
