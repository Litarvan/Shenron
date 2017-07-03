package fr.litarvan.shenron.command.group;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import fr.litarvan.shenron.Group;
import org.jetbrains.annotations.NotNull;

public class CommandGroupLeave implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String group = args.containsKey("group") ? args.get("group").getAsString() : null;

        if (group == null)
        {
            Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

            if (groups == null)
            {
                context.sendMessage(Dialog.warn("Erreur", "Il n'y a pas encore de groupe sur ce serveur"));
                return;
            }

            for (Group g : groups)
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
            context.sendMessage(Dialog.warn("Erreur", args.containsKey("group") ? "Groupe inconnu" : "Le channel dans lequel vous êtes n'est pas un channel de groupe"));
            return;
        }

        List<Role> roles = context.getGuild().getRolesByName(group, true);

        if (roles.size() == 0)
        {
            context.sendMessage(Dialog.warn("Erreur", "Ce groupe est inconnu"));
            return;
        }

        context.getGuild().getController().removeRolesFromMember(context.getMember(), roles.get(0)).complete();
        context.sendMessage(Dialog.info(args.containsKey("group") ? "Succès" : "Au revoir", args.containsKey("group") ? "Vous avez bien été supprimé du groupe" : context.getUser().getName() + " a quitté le groupe"));
    }
}
