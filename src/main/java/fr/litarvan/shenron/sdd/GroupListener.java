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
package fr.litarvan.shenron.sdd;

import java.util.List;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.krobot.config.ConfigProvider;

public class GroupListener
{
    @Inject
    private ConfigProvider config;

    @SubscribeEvent
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if (event.getUser().isBot() || !event.getGuild().getMember(event.getJDA().getSelfUser()).hasPermission(Permission.MESSAGE_READ))
        {
            return;
        }

        event.getChannel().getMessageById(event.getMessageId()).queue((message) ->
        {
            Role role = getRole(message, event.getReaction());
            Guild guild = message.getGuild();

            if (role == null)
            {
                return;
            }

            guild.getController().addRolesToMember(guild.getMember(event.getUser()), role).queue();
        });
    }

    @SubscribeEvent
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        if (event.getUser().isBot())
        {
            return;
        }

        event.getChannel().getMessageById(event.getMessageId()).queue((message) ->
        {
            Role role = getRole(message, event.getReaction());
            Guild guild = message.getGuild();

            if (role == null)
            {
                return;
            }

            guild.getController().removeRolesFromMember(guild.getMember(event.getUser()), role).queue();
        });
    }

    private Role getRole(Message message, MessageReaction reaction)
    {
        GroupTrigger trigger = null;
        Pair<String, String> group = null;

        for (GroupTrigger tr : config.at("groups.triggers", GroupTrigger[].class))
        {
            if (message.getId().equals(tr.getMessageId()))
            {
                trigger = tr;
                break;
            }
        }

        if (trigger == null)
        {
            return null;
        }

        for (Pair<String, String> pair : trigger.getGroups())
        {
            if (pair.getKey().equals(reaction.getEmote().getId()))
            {
                group = pair;
                break;
            }
        }

        if (group == null)
        {
            return null;
        }

        List<Role> roles = message.getGuild().getRolesByName(group.getValue(), true);

        if (roles.size() == 0)
        {
            return null;
        }

        return roles.get(0);
    }
}
