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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.dv8tion.jda.core.entities.Guild;

public class MusicPlayer extends AudioEventAdapter
{
    private static AudioPlayerManager playerManager;
    private static TLongObjectMap<MusicPlayer> players;

    private AudioPlayer player;
    private BlockingQueue<AudioTrack> queue;

    public MusicPlayer(AudioPlayer player)
    {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();

        this.player.addListener(this);
        this.setVolume(50);
    }

    public void addToQueue(AudioTrack track)
    {
        if (!player.startTrack(track, true))
        {
            queue.offer(track);
        }
    }

    public void next()
    {
        player.startTrack(queue.poll(), false);
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
            next();
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

    public BlockingQueue<AudioTrack> getQueue()
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
