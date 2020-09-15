package fr.litarvan.shenron.music.command.queue;

import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;

@GuildOnly
@Command(value = "mode <mode:conserve|once>", desc = "Change le mode de la file d'attente (Playlist ou File d'attente)", aliases = "m")
public class CommandQueueMode implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args)
    {
        boolean conserve = args.get("mode").equals("conserve");
        MusicPlayer.from(context.getGuild()).setConserve(conserve);

        return context.info("Mode changé", "Mode passé à : " + (conserve ? "Playlist" : "File d'attente"));
    }
}
