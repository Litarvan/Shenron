package fr.litarvan.shenron.command;

import fr.litarvan.shenron.util.TextEmoji;
import net.dv8tion.jda.api.entities.Message;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.NoTyping;

@NoTyping
@Command(value = "word-react [message...]", desc = "Ajoute le message donné sous forme de réactions au dernier message", aliases = {"wordreact", "wr"})
public class WordReactCommand implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Message last = context.getChannel().getHistory().retrievePast(2).complete().get(1);
        TextEmoji[] reactions = TextEmoji.toEmoji(String.join("", args.get("message", String[].class)).trim().toLowerCase(), true);

        for (TextEmoji reaction : reactions)
        {
            last.addReaction(reaction.getUnicode()).queue();
        }

        return null;
    }
}
