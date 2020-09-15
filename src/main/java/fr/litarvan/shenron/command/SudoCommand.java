package fr.litarvan.shenron.command;

import fr.litarvan.shenron.util.MessageSudo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.*;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;

@GuildOnly
@NoTyping
@UserRequires(Permission.MESSAGE_MANAGE)
@BotRequires({ Permission.MESSAGE_MANAGE, Permission.MANAGE_WEBHOOKS })
@Command(value = "sudo <target:user> <message:string...>", desc = "Simule l'envoi d'un message donné par un utilisateur donné")
public class SudoCommand implements CommandHandler
{
    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        MessageSudo.send(args.get("target"), (TextChannel) context.getChannel(), String.join(" ", args.get("message", String[].class)));
        return null;
    }
}
