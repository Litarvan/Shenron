package fr.litarvan.shenron;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class ShenronPlayerSendHandler implements AudioSendHandler
{
    private AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public ShenronPlayerSendHandler(AudioPlayer audioPlayer)
    {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide()
    {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio()
    {
        return lastFrame.data;
    }

    @Override
    public boolean isOpus()
    {
        return true;
    }
}
