package org.sdd.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import org.sdd.shenron.TextEmoji;

public class CommandWordReact implements CommandHandler
{
    @Inject
    private JDA jda;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        Message last = context.getChannel().getHistory().retrievePast(2).complete().get(1);
        TextEmoji[] reactions = TextEmoji.toEmoji(args.get("message").getAsString().trim().toLowerCase(), true);

        for (TextEmoji reaction : reactions)
        {
            last.addReaction(reaction.getUnicode()).queue();
        }
    }
}
