package fr.litarvan.shenron.middleware;

import com.google.inject.Inject;
import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.Middleware;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Dialog;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public class SDDAdminMiddleware implements Middleware
{
    @Inject
    private ConfigProvider config;

    @Override
    public boolean handle(Command command, @Nullable Map<String, SuppliedArgument> args, CommandContext context)
    {
        if (!ArrayUtils.contains(config.at("admins.sdd", String[].class), context.getUser().getId()))
        {
            context.getChannel().sendMessage(Dialog.error("Non-autorisé", "Seul un administrateur du SDD peut faire ça")).queue();
            return false;
        }

        return true;
    }
}
