package fr.litarvan.shenron.command.music;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import org.krobot.util.Markdown;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Message;
import fr.litarvan.shenron.Music;
import fr.litarvan.shenron.MusicPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandMusicSearch implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        Message message = context.sendMessage(Dialog.info("Recherche", "Recherche en cours...")).get();

        Music[] musics = player.search(String.join(" ", args.get("query").getAsStringList()));
        StringBuilder string = new StringBuilder();

        for (Music music : musics)
        {
            string.append("-> ").append(Markdown.bold(music.getName())).append("\n---> ").append(music.getUrl()).append("\n\n");
        }

        message.delete().queue();
        context.sendMessage(Dialog.info(Markdown.underline("Résultats de la recherche :"), string.toString()));
    }
}
