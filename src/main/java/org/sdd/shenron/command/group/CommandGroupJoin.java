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

public class CommandGroupJoin implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        String group = args.get("group").getAsString();
        Group[] groups = config.at("groups.groups", Group[].class);

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
            context.getChannel().sendMessage(Dialog.warn("Erreur", "Ce groupe est inconnu")).queue();
            return;
        }

        Guild guild = context.getMessage().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            context.getChannel().sendMessage(Dialog.error("Erreur", "Ce groupe a été supprimé\nL'admin devrait le supprimer de la configuration")).queue();
            return;
        }

        Role role = roles.get(0);
        guild.getController().addRolesToMember(guild.getMember(context.getUser()), role).queue();

        context.getChannel().sendMessage(Dialog.info("Succès", "Vous avez bien été ajouté au groupe")).queue();
    }
}
