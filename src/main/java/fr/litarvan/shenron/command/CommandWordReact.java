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

import net.dv8tion.jda.core.Permission;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import fr.litarvan.shenron.TextEmoji;
import java.util.Map;
import net.dv8tion.jda.core.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.krobot.permission.BotRequires;

@BotRequires({Permission.MESSAGE_ADD_REACTION})
public class CommandWordReact implements CommandHandler
{
    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        Message last = context.getChannel().getHistory().retrievePast(2).complete().get(1);
        TextEmoji[] reactions = TextEmoji.toEmoji(args.get("message").getAsString().trim().toLowerCase(), true);

        for (TextEmoji reaction : reactions)
        {
            last.addReaction(reaction.getUnicode()).queue();
        }
    }
}
