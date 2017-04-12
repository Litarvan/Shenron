package fr.litarvan.shenron;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandContext;
import java.util.List;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ExceptionHandler extends fr.litarvan.krobot.ExceptionHandler
{
    @Override
    public void handle(Throwable throwable, Command command, List<String> args, CommandContext context)
    {
        if (throwable instanceof PermissionException)
        {
            if (throwable.getMessage().contains("MESSAGE_MANAGE"))
            {
                return;
            }
        }

        super.handle(throwable, command, args, context);
    }
}
