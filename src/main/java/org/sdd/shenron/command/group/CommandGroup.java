package org.sdd.shenron.command.group;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import fr.litarvan.krobot.util.Markdown;
import java.util.Map;
import javax.inject.Inject;
import org.sdd.shenron.Group;

public class CommandGroup implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args) throws Exception
    {
        StringBuilder message = new StringBuilder();

        for (Group group : config.at("groups.groups", Group[].class))
        {
            message.append(" - ").append(group.getName()).append(group.getChannel() != null ? " ( #" + group.getChannel() + " )" : "").append("\n");
        }

        context.getChannel().sendMessage(Dialog.info(Markdown.underline("Liste de groupes :"), message.toString())).queue();
    }
}
