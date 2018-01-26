package fr.litarvan.shenron.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.litarvan.shenron.util.MessageSudo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;
import org.krobot.util.Interact;
import org.krobot.util.Markdown;

@Singleton
public class SupportEngine
{
    @Inject
    private ConfigProvider config;

    private TextChannel helpChannel;
    private TextChannel dashboardChannel;

    private List<SupportRequest> requests;

    public void setup(TextChannel helpChannel, TextChannel dashboardChannel)
    {
        this.helpChannel = helpChannel;
        this.dashboardChannel = dashboardChannel;

        this.requests = new ArrayList<>();

        helpChannel.getJDA().addEventListener(this);
    }

    public synchronized SupportRequest helpRequest(User author, String request)
    {
        int id = incrementRequestCount();
        String channelName = "aide_" + author.getName().toLowerCase().replaceAll("[^a-z0-9]", "") + "_" + id;

        TextChannel channel = (TextChannel) helpChannel.getGuild().getController().createTextChannel(channelName)
                                                       .setTopic("Demande d'aide de '" + author.getName() + "'")
                                                       .complete();

        MessageSudo.send(author, channel, Markdown.bold("Demande d'aide #" + id) + "\n\n" + request);

        MessageEmbed embed = new EmbedBuilder()
            .setTitle("Demande d'aide #" + id + " de '" + author.getName() + "'")
            .addField("Channel", channel.getAsMention(), true)
            .addField("Description", request, false)
            .setColor(Dialog.INFO_COLOR)
            .build();

        Message message = dashboardChannel.sendMessage(embed).complete();

        SupportRequest supportRequest = new SupportRequest(channel, message);
        this.requests.add(supportRequest);

        interact(author, supportRequest);

        return supportRequest;
    }

    public void up(User author, SupportRequest request)
    {
        if (System.currentTimeMillis() - request.getLastUp() < config.at("support.up-delay", long.class))
        {
            return;
        }

        MessageEmbed embed = request.getDashboardMessage().getEmbeds().get(0);
        request.getDashboardMessage().delete().complete();

        request.up();

        request.setDashboardMessage(dashboardChannel.sendMessage(embed).complete());
        interact(author, request);
    }

    protected void interact(User author, SupportRequest request)
    {
        Interact.from(request.getDashboardMessage(), 0)
                .on(Interact.YES, context -> {
                    if (!context.getUser().getId().equals(author.getId()))
                    {
                        return;
                    }

                    request.getChannel().delete().queue();
                    request.getDashboardMessage().delete().queue();

                    this.requests.remove(request);
                })
                .on("🆙", context -> {
                    if (context.getUser().getId().equals(author.getId()))
                    {
                        up(author, request);
                    }

                    context.getMessage().getReactions().forEach(r -> {
                        if (r.getEmote().getName().equals("🆙") && r.getUsers().complete().contains(context.getUser())) {
                            r.removeReaction().complete();
                        }
                    });
                });
    }

    public synchronized int requestCount()
    {
        return config.at("support.request-count", int.class);
    }

    public synchronized int incrementRequestCount()
    {
        int next = requestCount() + 1;
        config.set("support.request-count", next);

        return next;
    }

    @SubscribeEvent
    public void onMessage(MessageReceivedEvent event)
    {
        if (!event.getChannel().getId().equals(this.helpChannel.getId()))
        {
            return;
        }

        event.getMessage().delete().queue();
        helpRequest(event.getAuthor(), event.getMessage().getContent());
    }

    public List<SupportRequest> getRequests()
    {
        return requests;
    }

    public class SupportRequestSerializer extends TypeAdapter<SupportRequest>
    {
        @Override
        public void write(JsonWriter out, SupportRequest value) throws IOException
        {
            out.beginObject();
            {
                out.name("channel").value(value.getChannel().getId());
                out.name("message").value(value.getDashboardMessage().getContent());
            }
            out.close();
        }

        @Override
        public SupportRequest read(JsonReader in) throws IOException
        {
            String channelId = null;
            String messageId = null;

            in.beginObject();
            {
                for (int i = 0; i < 2; i++)
                {
                    switch (in.nextName())
                    {
                        case "channel":
                            channelId = in.nextString();
                            break;
                        case "message":
                            messageId = in.nextString();
                            break;
                    }
                }
            }
            in.close();

            TextChannel channel = channelId == null ? null : helpChannel.getGuild().getTextChannelById(channelId);
            Message message = messageId == null ? null : dashboardChannel.getMessageById(messageId).complete();

            return new SupportRequest(channel, message);
        }
    }
}
