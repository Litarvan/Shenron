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
package fr.litarvan.shenron;

import fr.minuskube.bot.discord.util.Webhook;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import org.json.JSONObject;

public final class MessageSudo
{
    private static boolean initialized = false;

    public static void send(User user, TextChannel channel, String message) throws WebhookException, ExecutionException, InterruptedException
    {
        Guild guild = channel.getGuild();
        JDAImpl jda = (JDAImpl) guild.getJDA();

        if (!initialized)
        {
            Webhook.initBotHooks(jda);
            initialized = true;
        }

        Webhook hook = Webhook.getBotHook(jda, channel);

        if (hook == null)
        {
            throw new WebhookException("creating", channel.getName());
        }

        if (!hook.execute(jda, new JSONObject(new HashMap<String, Object>()
        {
            {
                String username = guild.getMember(user).getNickname();

                if (username == null)
                {
                    username = user.getName();
                }

                put("username", username);
                put("avatar_url", user.getAvatarUrl());
                put("content", message);
            }
        })))
        {
            throw new WebhookException("sending", channel.getName());
        }
    }
}
