package fr.litarvan.shenron.group.command;

import fr.litarvan.shenron.group.GroupTrigger;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.ConfigProvider;
import org.krobot.runtime.KrobotRuntime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Command(value = "group-trigger <message> <groups...>", desc = "Envoie un message puis lui ajoute des reactions permettant de rejoindre des groupes. Format de 'groups' : 'groupe|:emoji:'")
public class GroupTriggerCommand implements CommandHandler
{
    @Inject
    private ConfigProvider configs;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Message message = context.getChannel().sendMessage(args.get("message", String.class)).complete();
        String[] groups = args.get("groups");
        List<ImmutablePair<String, String>> reactions = new ArrayList<>();

        for (String group : groups) {
            String[] split = group.split("\\|");
            String[] subSplit = split[1].trim().split(":");
            String emoji = subSplit[subSplit.length - 1];
            emoji = emoji.substring(0, emoji.length() - 1);

            reactions.add(new ImmutablePair<>(emoji, split[0].trim()));
            message.addReaction(KrobotRuntime.get().jda().getEmoteById(emoji)).queue();
        }

        GroupTrigger trigger = new GroupTrigger(message.getId(), reactions);
        configs.get("groups").append("triggers", GroupTrigger[].class, trigger);

        return null;
    }
}
