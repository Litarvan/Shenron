package fr.litarvan.shenron.music;

import java.util.List;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class DisconnectListener
{
    @SubscribeEvent
    public void onVocalDisconnect(GuildVoiceLeaveEvent event)
    {
        List<Member> members = event.getChannelLeft().getMembers();

        if (members.size() == 1 && members.get(0).getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong())
        {
            MusicPlayer.from(event.getGuild()).stop();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }
}
