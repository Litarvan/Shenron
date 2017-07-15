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
package fr.litarvan.shenron.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import org.krobot.util.Markdown;
import java.util.Map;
import javax.inject.Inject;
import fr.litarvan.shenron.Music;
import fr.litarvan.shenron.MusicPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandMusicQueue implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < player.getQueue().size(); i++)
        {
            AudioTrack track = player.getQueue().get(i);
            message.append(i + 1).append(". ").append(Markdown.bold(track.getInfo().title)).append(" ").append(Music.parseTime(track.getDuration())).append("\n\n");
        }

        context.sendMessage(Dialog.info("File d'attente", message.toString()));
    }
}
