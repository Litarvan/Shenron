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
import org.krobot.util.Dialog;
import org.krobot.util.Markdown;
import fr.litarvan.shenron.Group;
import java.util.Map;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class CommandGroup implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        StringBuilder message = new StringBuilder();

        Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

        if (groups == null)
        {
            context.sendMessage(Dialog.warn("Erreur", "Il n'y a pas encore de groupe sur ce serveur"));
            return;
        }

        for (Group group : groups)
        {
            message.append(" - ").append(group.getName()).append(group.getChannel() != null ? " ( #" + group.getChannel() + " )" : "").append("\n");
        }

        context.sendMessage(Dialog.info(Markdown.underline("Liste des groupes :"), message.toString()));
    }
}
