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

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

@Singleton
public class TimerManager
{
    private Map<String, TimerMessage> timers = new HashMap<>();

    public void start(TextChannel channel)
    {
        stop(channel);

        TimerMessage message = new TimerMessage(channel.sendMessage(":clock12:").complete());
        timers.put(channel.getId(), message);

        Thread t = new Thread(() ->
        {
            Message msg = message.message;

            while (message.running)
            {
                int i = Integer.parseInt(msg.getContent().substring(6, msg.getContent().length() - 1));

                if (i == 12)
                {
                    i = 0;
                }

                RestAction<Message> action = msg.editMessage(":clock" + (i + 1) + ":");

                long time = System.currentTimeMillis();
                msg = action.complete();

                long sleep = 1750L - (System.currentTimeMillis() - time);

                if (sleep < 0)
                {
                    sleep = 0;
                }

                try
                {
                    Thread.sleep(sleep);
                }
                catch (InterruptedException ignored)
                {
                }
            }

            msg.delete().queue();
        });

        t.start();
    }

    public void stop(TextChannel channel)
    {
        TimerMessage message = timers.get(channel.getId());

        if (message == null)
        {
            return;
        }

        message.running = false;
    }

    private static class TimerMessage
    {
        public final Message message;
        public boolean running;

        public TimerMessage(Message message)
        {
            this.message = message;
            this.running = true;
        }
    }
}
