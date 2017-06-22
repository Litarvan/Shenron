package fr.litarvan.shenron.command;

import org.krobot.command.CommandContext;
import org.krobot.command.CommandHandler;
import org.krobot.command.SuppliedArgument;
import fr.litarvan.shenron.TimerManager;
import java.util.Map;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class CommandTimer implements CommandHandler
{
    @Inject
    private TimerManager timer;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args) throws Exception
    {
        String action = args.get("action").getAsString();

        switch (action)
        {
            case "start":
                timer.start(context.getChannel());
                break;
            case "stop":
                timer.stop(context.getChannel());
                break;
        }
    }
}
