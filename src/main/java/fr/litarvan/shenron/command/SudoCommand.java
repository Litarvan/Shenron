package fr.litarvan.shenron.command;

import fr.litarvan.shenron.util.MessageSudo;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.NoTyping;

@NoTyping
@Command(value = "sudo <target:user> <message:string...>", desc = "Simule l'envoi d'un message donné par un utilisateur donné")
public class SudoCommand implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MessageSudo.send(args.get("target"), context.getChannel(), String.join(" ", args.get("message", String[].class)));
        return null;
    }
}
