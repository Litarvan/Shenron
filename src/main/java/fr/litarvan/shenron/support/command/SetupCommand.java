package fr.litarvan.shenron.support.command;

import fr.litarvan.shenron.support.SupportEngine;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.Config;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.Interact;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UserRequires(Permission.ADMINISTRATOR)
@BotRequires(Permission.ADMINISTRATOR)
@Command(value = "setup", desc = "Setup le système de support sur le serveur")
public class SetupCommand implements CommandHandler
{
    private Config config;
    private SupportEngine engine;

    @Inject
    public SetupCommand(ConfigProvider config, SupportEngine engine)
    {
        this.config = config.get("support");
        this.engine = engine;
    }

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Guild guild = context.getGuild();

        Message message = context.info("Démarrage...", "Setup du système de support...").get();

        TextChannel homeChannel = getChannel(guild, "home");
        TextChannel helpChannel = getChannel(guild, "help", Permission.MESSAGE_READ);
        TextChannel dashboardChannel = getChannel(guild, "dashboard", Permission.MESSAGE_READ);

        Interact.from(loadMessage(homeChannel, "home"), 0L)
                .on(Interact.YES, ctx -> {
                    helpChannel.createPermissionOverride(ctx.getMember())
                            .setAllow(Permission.MESSAGE_READ)
                            .queue();

                    dashboardChannel.createPermissionOverride(ctx.getMember())
                            .setAllow(Permission.MESSAGE_READ)
                            .queue();
                });

        Interact.from(loadMessage(helpChannel, "help"), 0)
                .on(Interact.YES, ctx -> {
                    helpChannel.getPermissionOverride(ctx.getMember()).getManager()
                            .grant(Permission.MESSAGE_WRITE)
                            .queue();
                });

        loadMessage(dashboardChannel, "dashboard");

        engine.setup(helpChannel, dashboardChannel);

        message.delete().queue();

        return context.info("Terminé", "Système de support redémarré");
    }

    protected Message loadMessage(TextChannel channel, String id)
    {
        String messageId = config.at("channels." + id + ".message.id");
        String messageContent = config.at("channels." + id + ".message.content");

        Message message = null;

        if (messageId != null)
        {
            message = channel.getMessageById(messageId).complete();

            if (!message.getContent().equals(messageContent))
            {
                message.editMessage(messageContent).queue();
            }
        }

        if (message == null)
        {
            message = channel.sendMessage(messageContent).complete();
            config.set("channels." + id + ".message.id", message.getId());
        }

        return message;
    }

    protected TextChannel getChannel(Guild guild, String id, Permission... denyPermission)
    {
        String name = config.at("channels." + id + ".name");
        String topic = config.at("channels." + id + ".topic");

        TextChannel channel = null;

        List<TextChannel> query = guild.getTextChannelsByName(name, true);
        if (query.size() > 0)
        {
            channel = query.get(0);
        }

        if (channel == null)
        {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.MESSAGE_WRITE);
            permissions.add(Permission.MESSAGE_ADD_REACTION);
            permissions.addAll(Arrays.asList(denyPermission));

            channel = (TextChannel) guild.getController().createTextChannel(name)
                    .addPermissionOverride(guild.getPublicRole(), Collections.emptyList(), permissions)
                    .setTopic(topic)
                    .complete();
        }

        return channel;
    }
}
