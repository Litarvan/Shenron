package fr.litarvan.shenron.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import fr.litarvan.shenron.MusicPlayer;
import java.util.Map;
import javax.inject.Inject;
import fr.litarvan.shenron.Music;
import org.jetbrains.annotations.NotNull;

public class CommandMusic implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        if (!args.containsKey("action"))
        {
            if (player.getQueue().size() == 0)
            {
                context.sendMessage(Dialog.info("Pas de musique", "Il n'y a pas de musique en train d'être jouée actuellement"));
                return;
            }

            AudioTrack track = player.getQueue().get(0);
            context.sendMessage(Dialog.info("Musique actuelle", "\n" + track.getInfo().title + "\n" + Music.parseTime(track.getPosition()) + " / " + Music.parseTime(track.getDuration())));

            return;
        }

        switch (args.get("action").getAsString())
        {
            case "unpause":
                player.play();
                break;
            case "pause":
                player.pause();
                break;
            case "next":
                player.next();
                break;
            case "stop":
                player.stop();
                break;
        }
    }
}
