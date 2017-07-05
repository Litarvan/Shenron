package fr.litarvan.shenron.command;

import net.dv8tion.jda.core.Permission;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import fr.litarvan.shenron.TextEmoji;
import java.util.Map;
import net.dv8tion.jda.core.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.krobot.permission.BotRequires;

@BotRequires({Permission.MESSAGE_ADD_REACTION})
public class CommandWordReact implements CommandHandler
{
    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        Message last = context.getChannel().getHistory().retrievePast(2).complete().get(1);
        TextEmoji[] reactions = TextEmoji.toEmoji(args.get("message").getAsString().trim().toLowerCase(), true);

        for (TextEmoji reaction : reactions)
        {
            last.addReaction(reaction.getUnicode()).queue();
        }
    }
}
