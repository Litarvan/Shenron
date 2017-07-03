package fr.litarvan.shenron.command.group;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;
import fr.litarvan.shenron.GroupTrigger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import fr.litarvan.shenron.Group;
import org.jetbrains.annotations.NotNull;

public class CommandGroupTrigger implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String text = args.get("message").getAsString();
        List<String> triggerStrings = args.get("emote#group").getAsStringList();

        Message message = context.sendMessage(text).get();

        List<ImmutablePair<String, String>> entries = new ArrayList<>();

        for (String group : triggerStrings)
        {
            String[] split = group.split("#");

            String groupName = split[0];
            String emoteName = split[1];

            List<Emote> emotes = context.getChannel().getJDA().getEmotesByName(emoteName, true);

            if (emotes.size() == 0)
            {
                context.sendMessage(Dialog.warn("Erreur", "Impossible de trouver l'emote '" + emoteName + "'"));
                message.delete().queue();

                return;
            }

            Group gr = null;

            Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

            if (groups == null)
            {
                context.sendMessage(Dialog.warn("Erreur", "Il n'y a pas encore de groupe sur ce serveur"));
                return;
            }

            for (Group g : groups)
            {
                if (g.getName().trim().equalsIgnoreCase(groupName.trim()))
                {
                    gr = g;
                    break;
                }
            }

            if (gr == null)
            {
                context.sendMessage(Dialog.warn("Erreur", "Impossible de trouver le groupe '" + groupName + "'"));
                message.delete().queue();

                return;
            }

            Emote emote = emotes.get(0);

            message.addReaction(emote).queue();
            entries.add(new ImmutablePair<>(emote.getId(), gr.getName()));
        }

        config.get("groups").append("triggers", GroupTrigger[].class, new GroupTrigger(message.getId(), entries));
    }
}
