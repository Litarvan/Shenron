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
package fr.litarvan.shenron.command;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.krobot.permission.BotRequires;

@BotRequires({Permission.MANAGE_ROLES})
public class CommandFAQ implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args)
    {
        String link = config.at("support.faq");

        if (!args.containsKey("target") || !context.getMember().hasPermission(Permission.ADMINISTRATOR))
        {
            context.sendMessage("FAQ : " + link);
            return;
        }

        Member member = context.getGuild().getMember(args.get("target").getAsUser());

        context.sendMessage(config.at("support.message"), member.getUser().getAsMention(), link);

        Role moche = context.getGuild().getRolesByName("Pabo", true).get(0);
        Role hyperMoche = context.getGuild().getRolesByName("Hyper Pabo", true).get(0);
        Role ultraMoche = context.getGuild().getRolesByName("Ultra Pabo", true).get(0);

        if (context.getGuild().getMembersWithRoles(ultraMoche).contains(member))
        {
            context.sendMessage("En plus t'es Ultra Pabo, t'es vraiment le pire des pabo omg");
        }
        else if (context.getGuild().getMembersWithRoles(hyperMoche).contains(member))
        {
            context.getGuild().getController().addRolesToMember(member, ultraMoche).queue();
        }
        else if (context.getGuild().getMembersWithRoles(moche).contains(member))
        {
            context.getGuild().getController().addRolesToMember(member, hyperMoche).queue();
        }
        else
        {
            context.getGuild().getController().addRolesToMember(member, moche).queue();
        }
    }
}
