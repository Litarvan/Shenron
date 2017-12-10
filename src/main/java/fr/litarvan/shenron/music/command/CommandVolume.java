package fr.litarvan.shenron.music.command;

import fr.litarvan.shenron.music.MusicPlayer;
import org.apache.commons.lang3.StringUtils;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;

@Command(value = "volume [value]", desc = "Affiche le volume actuel ou définit le volume", aliases = {"v", "voluem", "vloume"})
public class CommandVolume implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MusicPlayer player = MusicPlayer.from(context.getGuild());

        if (args.has("value"))
        {
            String volume = args.get("value");
            if (volume.endsWith("%"))
            {
                volume = volume.substring(0, volume.length() - 1);
            }

            if (volume.endsWith("à"))
            {
                volume = volume.replace('à', '0');
            }

            if (!StringUtils.isNumeric(volume))
            {
                return context.warn("Erreur", "'" + volume + "' n'est pas un nombre");
            }

            player.setVolume(Integer.parseInt(volume));
        }

        return context.info("Volume", "Volume : " + player.getVolume() + "%");
    }
}
