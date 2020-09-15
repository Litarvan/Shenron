package fr.litarvan.shenron.music.command.queue;

import fr.litarvan.shenron.music.MusicPlayer;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;
import org.krobot.util.Interact;

@GuildOnly
@Command(value = "clear", desc = "Vide la playlist", aliases = "c")
public class CommandQueueClear implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args)
    {
        Interact.from(context.info("Vider la playlist", "Êtes-vous sûr de vouloir vider la playlist ?"))
                .thenDelete()
                .on(Interact.YES, ctx -> MusicPlayer.from(ctx.getGuild()).clear())
                .on(Interact.NO, ctx -> {});

        return null;
    }
}
