package fr.litarvan.shenron.command.group;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Dialog;
import org.krobot.util.Markdown;
import fr.litarvan.shenron.Group;
import java.util.Map;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class CommandGroup implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        StringBuilder message = new StringBuilder();

        Group[] groups = config.at("groups." + context.getGuild().getId(), Group[].class);

        if (groups == null)
        {
            context.sendMessage(Dialog.warn("Erreur", "Il n'y a pas encore de groupe sur ce serveur"));
            return;
        }

        for (Group group : groups)
        {
            message.append(" - ").append(group.getName()).append(group.getChannel() != null ? " ( #" + group.getChannel() + " )" : "").append("\n");
        }

        context.sendMessage(Dialog.info(Markdown.underline("Liste des groupes :"), message.toString()));
    }
}
