package fr.litarvan.shenron;

import fr.litarvan.shenron.command.*;
import fr.litarvan.shenron.event.TriggerListener;
import fr.litarvan.shenron.group.GroupModule;
import fr.litarvan.shenron.sdd.SDDModule;
import fr.litarvan.shenron.model.Meme;
import fr.litarvan.shenron.music.MusicModule;
import fr.litarvan.shenron.support.SupportModule;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Game;
import org.krobot.Bot;
import org.krobot.KrobotModule;
import org.krobot.command.HelpCommand;
import org.krobot.config.ConfigProvider;
import org.krobot.module.Include;

@Include(
    imports = {
        MusicModule.class,
        SupportModule.class,
        SDDModule.class,
        GroupModule.class
    },
    commands = {
        HelpCommand.class,
        AddCommand.class,
        WordReactCommand.class,
        TimerCommand.class,
        SudoCommand.class
    },
    listeners = {
        TriggerListener.class
    }
)
@Bot(name = "Shenron", version = Shenron.VERSION, author = "Adrien 'Litarvan' Navratil")
public class Shenron extends KrobotModule
{
    public static final String VERSION = "3.2.1";

    @Inject
    private ConfigProvider configs;

    @Override
    public void preInit()
    {
        folder("config/")
            .configs("shenron", "triggers", "memes")
            .withDefaultsIn().classpathFolder("/");
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
        jda().getPresence().setGame(Game.of("/help - v" + VERSION));
    }
}
