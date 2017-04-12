package org.sdd.shenron.command.music;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import fr.litarvan.krobot.util.Markdown;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Message;
import org.sdd.shenron.Music;
import org.sdd.shenron.MusicPlayer;

public class CommandMusicSearch implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        Message message = context.getChannel().sendMessage(Dialog.info("Recherche", "Recherche en cours...")).complete();

        Music[] musics = player.search(String.join(" ", args.get("query").getAsStringList()));
        StringBuilder string = new StringBuilder();

        for (Music music : musics)
        {
            string.append("-> ").append(Markdown.bold(music.getName())).append("\n---> ").append(music.getUrl()).append("\n\n");
        }

        message.delete().queue();
        context.getChannel().sendMessage(Dialog.info(Markdown.underline("Résultats de la recherche :"), string.toString())).queue();
    }
}
