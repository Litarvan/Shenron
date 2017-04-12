package org.sdd.shenron;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import fr.litarvan.krobot.config.ConfigProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlayer extends AudioEventAdapter implements AudioLoadResultHandler
{
    private ConfigProvider config;
    private YouTube youTube;
    private List<AudioTrack> queue;
    private AudioPlayerManager playerManager;
    private AudioPlayer player;

    @Inject
    public MusicPlayer(ConfigProvider config)
    {
        this.config = config;
        this.youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (request) -> {})
                .setApplicationName(config.at("youtube.app-name")).build();
        this.queue = new ArrayList<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        this.player = playerManager.createPlayer();
        this.player.addListener(this);

        this.setVolume(25);
    }

    public Music[] search(String query) throws IOException
    {
        YouTube.Search.List search = youTube.search().list("id,snippet");

        search.setKey(config.at("youtube.api-key"));
        search.setQ(query);
        search.setType("video");
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults((long) 10);

        SearchListResponse response = search.execute();
        List<SearchResult> list = response.getItems();

        Music[] musics = new Music[list.size()];

        for (int i = 0; i < list.size(); i++)
        {
            SearchResult result = list.get(i);
            musics[i] = new Music(result.getSnippet().getTitle(), "http://youtube.com/watch?v=" + result.getId().getVideoId());
        }

        return musics;
    }

    public Video validate(String url) throws IOException
    {
        VideoListResponse response = youTube.videos().list("snippet,contentDetails")
                                            .setKey(config.at("youtube.api-key"))
                                            .setId(url.substring(url.lastIndexOf("=") + 1))
                                            .execute();
        List<Video> videoList = response.getItems();

        if (videoList.isEmpty())
        {
            return null;
        }

        return videoList.get(0);
    }

    public void load(String youtubeVideo)
    {
        playerManager.loadItem(youtubeVideo, this);
    }

    public void next()
    {
        stop();
        start();
    }

    public void start()
    {
        if (queue.size() > 0)
        {
            player.playTrack(queue.get(0));
        }
    }

    public void pause()
    {
        player.setPaused(true);
    }

    public void play()
    {
        player.setPaused(false);
    }

    public void stop()
    {
        player.stopTrack();

        if (queue.size() > 0)
        {
            queue.remove(0);
        }
    }

    public void setVolume(int value)
    {
        player.setVolume(value / 2);
    }

    public int getVolume()
    {
        return player.getVolume() * 2;
    }

    public AudioPlayer getPlayer()
    {
        return player;
    }

    public List<AudioTrack> getQueue()
    {
        return queue;
    }

    @Override
    public void trackLoaded(AudioTrack track)
    {
        queue.add(track);

        if (queue.size() == 1)
        {
            start();
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {
    }

    @Override
    public void noMatches()
    {
    }

    @Override
    public void loadFailed(FriendlyException exception)
    {
        throw exception;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        super.onTrackEnd(player, track, endReason);

        next();
    }
}
