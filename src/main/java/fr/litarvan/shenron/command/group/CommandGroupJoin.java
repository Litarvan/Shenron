package fr.litarvan.shenron.command.group;

import net.dv8tion.jda.core.Permission;
import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.util.Dialog;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import fr.litarvan.shenron.Group;
import org.jetbrains.annotations.NotNull;

@BotRequires({Permission.MANAGE_ROLES})
public class CommandGroupJoin implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String group = args.get("group").getAsString();
        Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

        if (groups == null)
        {
            context.sendMessage(Dialog.warn("Erreur", "Il n'y a pas encore de groupe sur ce serveur"));
            return;
        }

        boolean exists = false;

        for (Group g : groups)
        {
            if (g.getName().equalsIgnoreCase(group))
            {
                exists = true;
            }
        }

        if (!exists)
        {
            context.sendMessage(Dialog.warn("Erreur", "Ce groupe est inconnu"));
            return;
        }

        List<Role> roles = context.getGuild().getRolesByName(group, true);

        if (roles.size() == 0)
        {
            context.sendMessage(Dialog.error("Erreur", "Ce groupe a été supprimé\nL'admin devrait le supprimer de la configuration"));
            return;
        }

        Role role = roles.get(0);
        context.getGuild().getController().addRolesToMember(context.getMember(), role).queue();

        context.getChannel().sendMessage(Dialog.info("Succès", "Vous avez bien été ajouté au groupe")).queue();
    }
}
