package fr.litarvan.shenron.support;

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

    @Inject
    public SetupCommand(ConfigProvider config)
    {
        this.config = config.get("support");
    }

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Guild guild = context.getGuild();

        Message message = context.info("Démarrage...", "Setup du système de support...").get();

        TextChannel homeChannel = getChannel(guild, "home");
        TextChannel helpChannel = getChannel(guild, "help", Permission.MESSAGE_READ);
        TextChannel dashboardChannel = getChannel(guild, "dashboard", Permission.MESSAGE_READ);

        Interact.from(homeChannel.sendMessage(config.at("channels.home.message")), 0L)
                .on(Interact.YES, ctx -> {
                    helpChannel.createPermissionOverride(ctx.getMember())
                            .setAllow(Permission.MESSAGE_READ)
                            .queue();

                    dashboardChannel.createPermissionOverride(ctx.getMember())
                            .setAllow(Permission.MESSAGE_READ)
                            .queue();
                });

        Interact.from(helpChannel.sendMessage(config.at("channels.help.message")), 0)
                .on(Interact.YES, ctx -> {
                    helpChannel.getPermissionOverride(ctx.getMember()).getManager()
                            .grant(Permission.MESSAGE_WRITE)
                            .queue();
                });

        dashboardChannel.sendMessage(config.at("channels.dashboard.message")).queue();

        message.delete().queue();

        return context.info("Terminé", "Système de support redémarré");
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

    protected boolean isEmpty(TextChannel channel)
    {
        return channel.getHistory().isEmpty();
    }
}
