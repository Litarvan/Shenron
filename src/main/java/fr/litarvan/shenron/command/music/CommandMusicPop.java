package fr.litarvan.shenron.command.music;

import fr.litarvan.krobot.Krobot;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.util.Dialog;
import fr.litarvan.shenron.ShenronPlayerSendHandler;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import fr.litarvan.shenron.MusicPlayer;

public class CommandMusicPop implements CommandHandler
{
    @Inject
    private MusicPlayer player;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        Guild guild = context.getChannel().getGuild();
        List<VoiceChannel> channels = guild.getVoiceChannels();

        VoiceChannel channel = null;

        for (VoiceChannel chan : channels)
        {
            if (chan.getMembers().contains(guild.getMember(context.getUser())))
            {
                channel = chan;
            }
        }

        if (channel == null)
        {
            context.getChannel().sendMessage(Dialog.warn("Erreur", "Vous n'êtes pas dans un channel vocal")).queue();
            return;
        }

        AudioManager manager = guild.getAudioManager();
        manager.setSendingHandler(new ShenronPlayerSendHandler(player.getPlayer()));
        manager.openAudioConnection(channel);

        Krobot.injector().getInstance(MusicPlayer.class).play();
    }
}
