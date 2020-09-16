package fr.litarvan.shenron.music.command;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.music.MusicPlayer;
import java.util.List;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.managers.AudioManager;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;

@GuildOnly
@Command(value = "play <query...>", desc = "Joue une musique depuis youtube ('query' = recherche / lien(s))", aliases = {"p", "play", "paly", "plya", "lpay"}, subs = CommandPlayOnce.class)
public class CommandPlay implements CommandHandler
{
    private static YouTube youtube;
    private ConfigProvider config;

    @Inject
    public CommandPlay(ConfigProvider config)
    {
        if (config.get("youtube") != null)
        {
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {})
                .setApplicationName(config.at("youtube.app-name")).build();
        }

        this.config = config;
    }

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        if (!context.getBotMember().hasPermission((GuildChannel) context.getChannel(), Permission.VOICE_CONNECT, Permission.VOICE_SPEAK))
        {
            return context.warn("Erreur", "Le bot n'a pas la permission de voir/parler dans le channel où vous êtes");
        }

        String[] arg = args.get("query");

        if (arg.length == 0)
        {
            return context.warn("Erreur", "Vous devez fournir un(des) lien(s) / un recherche youtube");
        }

        String first = arg[0];
        MusicPlayer player = MusicPlayer.from(context.getGuild());

        if (!first.startsWith("https://") && !first.startsWith("http://") && youtube != null)
        {
            String query = String.join(" ", arg);
            Message message = context.info("Recherche", "Recherche en cours de '" + query + "'...").join();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(config.at("youtube.api-key"));
            search.setQ(query);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(1L);

            SearchListResponse response = search.execute();
            List<SearchResult> list = response.getItems();

            message.delete().queue();

            if (list.size() == 0)
            {
                return context.warn("Erreur", "Aucune musique trouvée pour la recherche '" + query + "'");
            }

            add(player, "https://youtube.com/watch?v=" + list.get(0).getId().getVideoId(), context);
            return null;
        }

        for (String link : arg)
        {
            add(player, link, context);
        }

        return null;
    }

    protected void add(MusicPlayer player, String link, MessageContext context)
    {
        MusicPlayer.getPlayerManager().loadItemOrdered(player, link, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                queue(context, player, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack first = playlist.getSelectedTrack();

                if (first == null)
                {
                    first = playlist.getTracks().get(0);
                }

                queue(context, player, first);
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
    }

    protected void queue(MessageContext context, MusicPlayer player, AudioTrack track)
    {
        context.send(createQueueMessage(track));

        connect(context);
        queue(player, track);
    }

    protected void queue(MusicPlayer player, AudioTrack track)
    {
        player.add(track);
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

    protected YouTube getYoutube()
    {
        return youtube;
    }
}
