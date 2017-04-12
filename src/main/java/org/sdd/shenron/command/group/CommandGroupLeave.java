package org.sdd.shenron.command.group;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.sdd.shenron.Group;

public class CommandGroupLeave implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        String group = args.containsKey("group") ? args.get("group").getAsString() : null;

        if (group == null)
        {
            for (Group g : config.at("groups.groups", Group[].class))
            {
                if (g.getChannel() != null && g.getChannel().equalsIgnoreCase(context.getChannel().getName()))
                {
                    group = g.getName();
                    break;
                }
            }
        }

        if (group == null)
        {
            context.getChannel().sendMessage(Dialog.warn("Erreur", args.containsKey("group") ? "Groupe inconnu" : "Le channel dans lequel vous êtes n'est pas un channel de groupe")).queue();
            return;
        }

        Guild guild = context.getChannel().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            context.getChannel().sendMessage(Dialog.warn("Erreur", "Ce groupe est inconnu")).queue();
            return;
        }

        guild.getController().removeRolesFromMember(guild.getMember(context.getUser()), roles.get(0)).complete();
        context.getChannel().sendMessage(Dialog.info(args.containsKey("group") ? "Succès" : "Au revoir", args.containsKey("group") ? "Vous avez bien été supprimé du groupe" : context.getUser().getName() + " a quitté le groupe")).queue();
    }
}
