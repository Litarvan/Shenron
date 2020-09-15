package fr.litarvan.shenron.music.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;

@GuildOnly
@Command(value = "pause", desc = "Met en pause ou reprend la musique en cours", aliases = {"pa", "resume", "re"})
public class CommandPause implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        AudioPlayer player = MusicPlayer.from(context.getGuild()).getPlayer();
        boolean newValue = !player.isPaused();

        player.setPaused(newValue);

        return context.info("Musique", "Musique " + (newValue ? "en pause" : "reprise"));
    }
}
