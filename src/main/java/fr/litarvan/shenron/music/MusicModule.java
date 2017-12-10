package fr.litarvan.shenron.music;

import fr.litarvan.shenron.music.command.CommandDisconnect;
import fr.litarvan.shenron.music.command.CommandMusic;
import fr.litarvan.shenron.music.command.CommandNext;
import fr.litarvan.shenron.music.command.CommandPlay;
import fr.litarvan.shenron.music.command.queue.CommandQueue;
import fr.litarvan.shenron.music.command.CommandVolume;
import java.util.concurrent.TimeUnit;
import org.krobot.KrobotModule;
import org.krobot.module.Include;

@Include(
    commands = {
        CommandMusic.class,
        CommandPlay.class,
        CommandNext.class,
        CommandDisconnect.class,
        CommandQueue.class,
        CommandVolume.class
    }
)
public class MusicModule extends KrobotModule
{
    @Override
    public void preInit()
    {
        config("config/youtube.json")
            .defaultIn().classpath("/youtube.default.json");
    }

    @Override
    public void init()
    {
    }

    @Override
    public void postInit()
    {
    }

    public static String parseTime(long duration)
    {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return String.format("%dm%02ds",minutes , TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes));
    }
}
