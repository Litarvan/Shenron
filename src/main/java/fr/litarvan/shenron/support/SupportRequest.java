package fr.litarvan.shenron.support;

import java.util.List;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

public class SupportRequest
{
    private TextChannel channel;
    private Message dashboardMessage;

    private long lastUp;

    public SupportRequest(TextChannel channel, Message dashboardMessage)
    {
        this.channel = channel;
        this.dashboardMessage = dashboardMessage;
        this.lastUp = System.currentTimeMillis();
    }

    public TextChannel getChannel()
    {
        return channel;
    }

    public void setDashboardMessage(Message dashboardMessage)
    {
        this.dashboardMessage = dashboardMessage;
    }

    public Message getDashboardMessage()
    {
        return dashboardMessage;
    }

    public void up()
    {
        this.lastUp = System.currentTimeMillis();

        Guild guild = channel.getGuild();
        List<Role> query = guild.getRolesByName("Helper", true);

        channel.sendMessage(query.size() > 0 ? query.get(0).getAsMention() : "(Le 'up' est cassé lol)").queue();
    }

    public long getLastUp()
    {
        return lastUp;
    }
}
