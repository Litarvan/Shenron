package org.sdd.shenron;

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
                put("username", user.getName());
                put("avatar_url", user.getAvatarUrl());
                put("content", message);
            }
        })))
        {
            throw new WebhookException("sending", channel.getName());
        }
    }
}
