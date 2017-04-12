package org.sdd.shenron.command.group;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import org.sdd.shenron.Group;

public class CommandGroupCreate implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        String name = args.get("name").getAsString();
        String channel = args.containsKey("channel") ? args.get("channel").getAsString().toLowerCase() : name.toLowerCase();

        for (Group group : config.at("groups.groups", Group[].class))
        {
            if (group.getName().equalsIgnoreCase(name))
            {
                context.getChannel().sendMessage(Dialog.warn("Erreur", "Un groupe du même nom existe déjà")).queue();
                return;
            }
        }

        Guild guild = context.getChannel().getGuild();
        Role role = guild.getController().createRole().complete();

        Role member = guild.getRolesByName("Membre", true).get(0);

        Channel chan = guild.getController().createTextChannel(channel).complete();

        PermissionOverride perm = chan.createPermissionOverride(member).complete();
        perm.getManager().deny(Permission.MESSAGE_READ).queue();

        perm = chan.createPermissionOverride(role).complete();
        perm.getManager().grant(Permission.MESSAGE_READ).queue();

        role.getManager().setMentionable(true).queue();
        role.getManagerUpdatable().getNameField().setValue(name).update().queue();

        config.get("groups").append("groups", Group[].class, new Group(name, channel));

        context.getChannel().sendMessage(Dialog.info("Succès", "Groupe '" + name + "' créé (channel #" + channel + " )")).queue();
    }
}
