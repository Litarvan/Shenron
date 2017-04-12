package org.sdd.shenron.command.music;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import org.sdd.shenron.MusicPlayer;

public class CommandMusicVolume implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        if (args.containsKey("value"))
        {
           player.setVolume(args.get("value").getAsNumber());
        }
        else
        {
            context.getChannel().sendMessage(Dialog.info("Volume", player.getVolume() + "%")).queue();
        }
    }
}
