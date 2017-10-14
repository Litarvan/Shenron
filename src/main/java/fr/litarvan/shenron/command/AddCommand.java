package fr.litarvan.shenron.command;


import javax.inject.Inject;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.ConfigProvider;

@Command(value = "add", desc = "Affiche le lien d'ajout du bot")
public class AddCommand implements CommandHandler
{
    @Inject
    private ConfigProvider configs;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        return configs.at("shenron.add-link");
    }
}
