/*
 * Copyright 2016-2017 Adrien 'Litarvan' Navratil
 *
 * This file is part of Shenron.
 *
 * Shenron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shenron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shenron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.litarvan.shenron.command.group;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.Dialog;
import fr.litarvan.shenron.Group;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

@UserRequires({Permission.ADMINISTRATOR})
@BotRequires({Permission.MANAGE_ROLES, Permission.MANAGE_CHANNEL})
public class CommandGroupCreate implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String name = args.get("name").getAsString();
        String channel = args.containsKey("channel") ? args.get("channel").getAsString().toLowerCase() : name.toLowerCase();

        Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

        if (groups != null)
        {
            for (Group group : groups)
            {
                if (group.getName().equalsIgnoreCase(name))
                {
                    context.sendMessage(Dialog.warn("Erreur", "Un groupe du même nom existe déjà"));
                    return;
                }
            }
        }

        Role role = context.getGuild().getController().createRole().complete();

        List<Role> query = context.getGuild().getRolesByName("Membre", true);
        Role member = query.size() < 1 ? context.getGuild().getPublicRole() : query.get(0);

        Channel chan = context.getGuild().getController().createTextChannel(channel).complete();

        PermissionOverride perm = chan.createPermissionOverride(member).complete();
        perm.getManager().deny(Permission.MESSAGE_READ).queue();

        perm = chan.createPermissionOverride(role).complete();
        perm.getManager().grant(Permission.MESSAGE_READ).queue();

        role.getManager().setMentionable(true).queue();
        role.getManagerUpdatable().getNameField().setValue(name).update().queue();

        config.get("groups").append(context.getGuild().getId(), Group[].class, new Group(name, channel));

        context.sendMessage(Dialog.info("Succès", "Groupe '" + name + "' créé (channel #" + channel + " )"));
    }
}
