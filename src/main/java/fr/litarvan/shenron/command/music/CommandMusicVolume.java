package fr.litarvan.shenron.command.music;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import fr.litarvan.shenron.MusicPlayer;
import java.util.Map;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class CommandMusicVolume implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        if (args.containsKey("value"))
        {
           player.setVolume(args.get("value").getAsNumber());
        }

        context.sendMessage(Dialog.info("Volume", player.getVolume() + "%"));
    }
}
