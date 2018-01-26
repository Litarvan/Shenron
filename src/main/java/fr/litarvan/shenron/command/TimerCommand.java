package fr.litarvan.shenron.command;

import fr.litarvan.shenron.util.TimerManager;
import javax.inject.Inject;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;

@Command(value = "timer <action:start|stop>", desc = "Affiche un timer **animé** en emoji")
public class TimerCommand implements CommandHandler
{
    @Inject
    private TimerManager timer;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        switch (args.get("action", String.class))
        {
            case "start":
                timer.start(context.getChannel());
                break;
            case "stop":
                timer.stop(context.getChannel());
                break;
        }

        return null;
    }
}
