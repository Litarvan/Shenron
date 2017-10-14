package fr.litarvan.shenron;

import fr.litarvan.shenron.command.AddCommand;
import fr.litarvan.shenron.command.CommandClear;
import fr.litarvan.shenron.command.CommandClearWhere;
import fr.litarvan.shenron.command.MemeCommand;
import fr.litarvan.shenron.command.SudoCommand;
import fr.litarvan.shenron.command.TimerCommand;
import fr.litarvan.shenron.command.WordReactCommand;
import fr.litarvan.shenron.group.GroupModule;
import fr.litarvan.shenron.model.Meme;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.support.SupportModule;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.User;
import org.krobot.Bot;
import org.krobot.KrobotModule;
import org.krobot.command.ArgumentMap;
import org.krobot.command.HelpCommand;
import org.krobot.config.ConfigProvider;
import org.krobot.console.ConsoleCommand;
import org.krobot.module.Include;
import org.krobot.util.ColoredLogger;

@Include(
    imports = {
        MusicModule.class
    },
    commands = {
        HelpCommand.class,
        AddCommand.class,
        WordReactCommand.class,
        TimerCommand.class,
        SudoCommand.class
    }
)
@Bot(name = "Shenron", version = Shenron.VERSION, author = "Adrien 'Litarvan' Navratil")
public class Shenron extends KrobotModule
{
    public static final String VERSION = "1.0.0";

    @Inject
    private ConfigProvider configs;

    @Override
    public void preInit()
    {
        folder("config/")
            .configs("groups", "shenron", "sdd", "support", "triggers", "memes")
            .withDefaultsIn().classpathFolder("/");

        from(GroupModule.class)
            .asSubsOf("group", "list");
    }

    @Override
    public void init()
    {
        prefix(configs.at("shenron.prefix"));

        for (Meme meme : configs.at("memes.memes", Meme[].class))
        {
            command(meme.getCommand(), new MemeCommand(meme))
                .description("Affiche le même '" + meme.getName() + "'");
        }

        command("clear <amount:number>", CommandClear.class)
            .description("Supprime le nombre de message donnés à partir du dernier posté")
            .alias("c")
            .sub("before <query> <amount:number>", new CommandClearWhere(false))
                .alias("b")
                .description("Supprime le nombre de message donné avant un certains message (query correspond à une partie du contenu de ce dernier)")
                .then()
            .sub("after <query> <amount:number>", new CommandClearWhere(true))
                .alias("a")
                .description("Supprime le nombre de message donné après un certains message (query correspond à une partie du contenu de ce dernier)");
    }

    @Override
    public void postInit()
    {

    }
}
