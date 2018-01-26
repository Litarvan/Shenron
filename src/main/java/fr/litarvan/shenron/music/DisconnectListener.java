package fr.litarvan.shenron.music;

import java.util.List;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

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
