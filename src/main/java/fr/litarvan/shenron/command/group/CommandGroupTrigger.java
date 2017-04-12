package fr.litarvan.shenron.command.group;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import fr.litarvan.shenron.GroupTrigger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import fr.litarvan.shenron.Group;

public class CommandGroupTrigger implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        String text = args.get("message").getAsString();
        List<String> triggerStrings = args.get("emote#group").getAsStringList();

        Message message = context.getChannel().sendMessage(text).complete();

        List<ImmutablePair<String, String>> entries = new ArrayList<>();

        for (String group : triggerStrings)
        {
            String[] split = group.split("#");

            String groupName = split[0];
            String emoteName = split[1];

            List<Emote> emotes = context.getChannel().getJDA().getEmotesByName(emoteName, true);

            if (emotes.size() == 0)
            {
                context.getChannel().sendMessage(Dialog.warn("Erreur", "Impossible de trouver l'emote '" + emoteName + "'")).queue();
                message.delete().queue();

                return;
            }

            Group gr = null;

            for (Group g : config.at("groups.groups", Group[].class))
            {
                if (g.getName().trim().equalsIgnoreCase(groupName.trim()))
                {
                    gr = g;
                    break;
                }
            }

            if (gr == null)
            {
                context.getChannel().sendMessage(Dialog.warn("Erreur", "Impossible de trouver le groupe '" + groupName + "'")).queue();
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
