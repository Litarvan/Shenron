package org.sdd.shenron.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import org.sdd.shenron.Music;
import org.sdd.shenron.MusicPlayer;

public class CommandMusic implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        if (!args.containsKey("action"))
        {
            AudioTrack track = player.getQueue().get(0);
            context.getChannel().sendMessage(Dialog.info("Musique actuelle", "\n" + track.getInfo().title + "\n" + Music.parseTime(track.getPosition()) + " / " + Music.parseTime(track.getDuration()))).queue();

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
        }
    }
}
