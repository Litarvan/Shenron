package fr.litarvan.shenron.support.command;

import javax.inject.Inject;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;

@Command(value = "faq [target:user]", desc = "Affiche le lien de la FAQ (Admin: Averti un membre)", aliases = "f")
@BotRequires({Permission.MANAGE_ROLES})
public class FAQCommand implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        String link = config.at("support.faq");

        if (!args.has("target") || !context.hasPermission(Permission.ADMINISTRATOR))
        {
            return "FAQ : " + link;
        }

        return context.send(config.at("support.message"), args.get("target", User.class).getAsMention(), link);
    }
}
