package fr.litarvan.shenron;

import org.krobot.config.ConfigProvider;
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
