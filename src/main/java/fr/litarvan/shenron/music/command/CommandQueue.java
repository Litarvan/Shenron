package fr.litarvan.shenron.music.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;

@Command(value = "queue", desc = "Affiche la file d'attente actuelle", aliases = "q")
public class CommandQueue implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        StringBuilder content = new StringBuilder();

        MusicPlayer player = MusicPlayer.from(context.getGuild());
        int i = 0;

        for (AudioTrack track : player.getQueue())
        {
            i++;
            content.append(i).append(". ").append(track.getInfo().title).append(" (").append(MusicModule.parseTime(track.getDuration())).append(")\n");
        }

        return context.info("File d'attente", content.toString());
    }
}
