package fr.litarvan.shenron.group.command;

import fr.litarvan.shenron.group.Group;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.Config;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;

import javax.inject.Inject;

//@UserRequires({ Permission.MANAGE_ROLES })
@BotRequires({ Permission.MANAGE_ROLES })
@Command(value = "create-group <name> [channel-name]",  desc = "Créé un groupe et son channel")
public class CreateGroupCommand implements CommandHandler
{
    @Inject
    private ConfigProvider configs;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        String name = args.get("name");
        String channelName = args.get("channel-name");
        if (channelName == null) {
            channelName = name.toLowerCase().replace(' ', '-').replaceAll("[^A-z]", "");
        }

        Guild guild = context.getGuild();
        Role role = guild.getController().createRole().setName(name).setMentionable(true).complete();
        Channel channel = guild.getController().createTextChannel(channelName)
                .addPermissionOverride(guild.getPublicRole(), 0, Permission.getRaw(Permission.MESSAGE_READ))
                .addPermissionOverride(role, Permission.getRaw(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE), 0)
                .complete();
        Group group = new Group(role.getName(), channel.getName());

        Config groups = configs.get("groups");
        groups.append(guild.getId(), Group[].class, group);

        return context.info("Groupe créé", "Groupe '" + role.getAsMention() + "' (channel: '" + ((TextChannel) channel).getAsMention() + "') créé avec succès");
    }
}
