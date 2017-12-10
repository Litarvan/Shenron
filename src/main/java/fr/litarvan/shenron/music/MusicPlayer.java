package fr.litarvan.shenron.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;

public class MusicPlayer extends AudioEventAdapter
{
    private static AudioPlayerManager playerManager;
    private static TLongObjectMap<MusicPlayer> players;

    private AudioPlayer player;
    private List<AudioTrack> queue;
    private List<AudioTrack> once;

    private boolean conserve;

    public MusicPlayer(AudioPlayer player)
    {
        this.player = player;
        this.queue = new ArrayList<>();
        this.once = new ArrayList<>();

        this.player.addListener(this);
        this.setVolume(50);
        this.setConserve(true);
    }

    public void addToQueue(AudioTrack track)
    {
        queue.add(track);
        next(true);
    }

    public void addOnce(AudioTrack track)
    {
        if (!player.startTrack(track, true))
        {
            queue.add(track);
            once.add(track);
        }
    }

    public void next(boolean noInterrupt)
    {
        if (queue.size() == 0)
        {
            return;
        }

        AudioTrack track = queue.remove(0);
        player.startTrack(track, noInterrupt);

        boolean conserve = this.conserve;

        if (once.contains(track))
        {
            conserve = false;
            once.remove(track);
        }

        if (conserve)
        {
            queue.add(track.makeClone());
        }
    }

    public void stop()
    {
        queue.clear();
        player.stopTrack();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            next(false);
        }
    }

    public int getVolume()
    {
        return player.getVolume() * 3;
    }

    public void setVolume(int volume)
    {
        player.setVolume(Math.min(100, volume) / 3 + 1);
    }

    public void setConserve(boolean conserve)
    {
        this.conserve = conserve;
    }

    public boolean isConserve()
    {
        return conserve;
    }

    public List<AudioTrack> getQueue()
    {
        return queue;
    }

    public AudioPlayer getPlayer()
    {
        return player;
    }

    static
    {
        playerManager = new DefaultAudioPlayerManager();
        players = new TLongObjectHashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized static MusicPlayer from(Guild guild)
    {
        MusicPlayer player = players.get(guild.getIdLong());

        if (player == null)
        {
            player = new MusicPlayer(playerManager.createPlayer());
            players.put(guild.getIdLong(), player);
        }

        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player.getPlayer()));

        return player;
    }

    public static AudioPlayerManager getPlayerManager()
    {
        return playerManager;
    }
}
