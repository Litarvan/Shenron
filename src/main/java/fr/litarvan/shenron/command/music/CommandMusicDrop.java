package fr.litarvan.shenron.command.music;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.shenron.MusicPlayer;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class CommandMusicDrop implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        AudioManager manager = context.getGuild().getAudioManager();

        player.stop();
        manager.closeAudioConnection();
    }
}
