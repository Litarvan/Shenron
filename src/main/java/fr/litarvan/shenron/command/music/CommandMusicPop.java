package fr.litarvan.shenron.command.music;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.util.Dialog;
import fr.litarvan.shenron.ShenronPlayerSendHandler;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.managers.AudioManager;
import fr.litarvan.shenron.MusicPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandMusicPop implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        Guild guild = context.getChannel().getGuild();
        GuildVoiceState state = guild.getMember(context.getUser()).getVoiceState();

        if (!state.inVoiceChannel())
        {
            context.getChannel().sendMessage(Dialog.warn("Erreur", "Vous n'êtes pas dans un channel vocal")).queue();
            return;
        }

        AudioManager manager = guild.getAudioManager();
        manager.setSendingHandler(new ShenronPlayerSendHandler(player.getPlayer()));
        manager.openAudioConnection(state.getChannel());

        player.play();
    }
}
