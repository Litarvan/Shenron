package fr.litarvan.shenron.music.command;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.music.MusicPlayer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.managers.AudioManager;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.util.Dialog;

@Command(value = "play <query>", desc = "Joue une musique depuis youtube ('query' = recherche / lien)", aliases = "p")
public class CommandPlay implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        String link = args.get("query");

        MusicPlayer player = MusicPlayer.from(context.getGuild());
        MusicPlayer.getPlayerManager().loadItemOrdered(player, link, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                context.send(createQueueMessage(track));

                connect(context);
                player.addToQueue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack first = playlist.getSelectedTrack();

                if (first == null)
                {
                    first = playlist.getTracks().get(0);
                }

                context.send(createQueueMessage(first));

                connect(context);
                player.addToQueue(first);
            }

            @Override
            public void noMatches()
            {
                context.warn("Musique introuvable", "Impossible de trouver une musique pour '" + link + "'");
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                context.error("Erreur", "Impossible de mettre la musique : " + exception.getMessage());
            }
        });

        return null;
    }

    protected void connect(MessageContext context)
    {
        AudioManager manager = context.getGuild().getAudioManager();

        if (manager.isConnected() || manager.isAttemptingToConnect())
        {
            return;
        }

        if (!context.getMember().getVoiceState().inVoiceChannel())
        {
            context.warn("Erreur", "Vous n'êtes pas connecté à un channel vocal");
            return;
        }

        manager.openAudioConnection(context.getMember().getVoiceState().getChannel());
    }

    protected EmbedBuilder createQueueMessage(AudioTrack track)
    {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Mise en attente de la musique", null);
        builder.setColor(Dialog.INFO_COLOR);
        builder.setThumbnail(Dialog.INFO_ICON);

        builder.addField("Titre", track.getInfo().title, false);
        builder.addField("Durée", MusicModule.parseTime(track.getDuration()), false);

        return builder;
    }
}
