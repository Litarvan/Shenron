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
package fr.litarvan.shenron.middleware;

import org.krobot.command.Command;
import org.krobot.command.CommandContext;
import org.krobot.command.Middleware;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;

public class SupportMiddleware implements Middleware
{
    @Inject
    private ConfigProvider config;

    @Override
    public boolean handle(Command command, Map<String, SuppliedArgument> args, CommandContext context)
    {
        return context.getGuild().getName().equals(config.at("support.name"));
    }
}
