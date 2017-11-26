package fr.litarvan.shenron.music.command;

import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;

@Command(value = "volume [value:number]", desc = "Affiche le volume actuel ou définit le volume", aliases = "v")
public class CommandVolume implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MusicPlayer player = MusicPlayer.from(context.getGuild());

        if (args.has("value"))
        {
            player.setVolume(args.get("value"));
        }

        return context.info("Volume", "Volume : " + player.getVolume() + "%");
    }
}
