package fr.litarvan.shenron.command.music;

import com.google.api.services.youtube.model.Video;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import java.awt.Color;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.EmbedBuilder;
import fr.litarvan.shenron.Music;
import fr.litarvan.shenron.MusicPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandMusicPlay implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String music = args.get("url").getAsString();
        Video video = player.validate(music);

        if (video == null)
        {
            context.sendMessage(Dialog.warn("Erreur", "Impossible de trouver cette vidéo !\nVous devez entrer un url de vidéo Youtube"));
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Mise en attente de la musique", null);
        builder.setColor(Color.decode("0x0094FF"));
        builder.setThumbnail(Dialog.INFO_ICON);

        builder.addField("Titre", video.getSnippet().getTitle(), false);
        builder.addField("Durée", video.getContentDetails().getDuration().substring(2).toLowerCase(), false);

        int waiting = 0;

        for (AudioTrack track : player.getQueue())
        {
            waiting += track.getDuration();
        }

        builder.addField("Place dans la file d'attente", (player.getQueue().size() + 1) + "/" + (player.getQueue().size() + 1), false);
        builder.addField("Temps d'attente avant diffusion", waiting == 0 ? "Aucun" : Music.parseTime(waiting), false);

        player.load(music);

        context.sendMessage(builder);
    }
}
