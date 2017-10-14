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
package fr.litarvan.shenron.util;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.krobot.util.IconUtils;
import org.krobot.util.WebhookBuilder;

public final class MessageSudo
{
    public static final String WEBHOOK_NAME = "Shenron Hook";
    public static final String WEBHOOK_ICON = "/webhook.png";

    public static void send(User user, TextChannel channel, String message)
    {
        WebhookBuilder
            .from(WEBHOOK_NAME, channel, IconUtils.readFromClasspath(WEBHOOK_ICON))
            .setUsername(user.getName())
            .setAvatarUrl(user.getAvatarUrl())
            .setContent(message)
            .execute().queue();
    }
}
