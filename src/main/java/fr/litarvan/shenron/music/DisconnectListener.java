package fr.litarvan.shenron.music;

import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class DisconnectListener
{
    @SubscribeEvent
    public void onVocalDisconnect(GuildVoiceLeaveEvent event)
    {
        check(event.getJDA(), event.getGuild(), event.getChannelLeft());
    }

    @SubscribeEvent
    public void onVocalChange(GuildVoiceMoveEvent event)
    {
        check(event.getJDA(), event.getGuild(), event.getChannelLeft());
    }

    protected void check(JDA jda, Guild guild, VoiceChannel channel)
    {
        List<Member> members = channel.getMembers();

        if (members.size() == 1 && members.get(0).getUser().getIdLong() == jda.getSelfUser().getIdLong())
        {
            MusicPlayer.from(guild).stop();
            guild.getAudioManager().closeAudioConnection();
        }
    }
}
