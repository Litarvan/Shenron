package fr.litarvan.shenron;

import fr.litarvan.krobot.IBot;
import fr.litarvan.krobot.Krobot;
import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandManager;
import fr.litarvan.krobot.command.HelpCommand;
import fr.litarvan.krobot.config.ConfigProvider;
import fr.litarvan.krobot.util.Markdown;
import fr.litarvan.shenron.command.CommandClear;
import fr.litarvan.shenron.command.CommandClearWhere;
import fr.litarvan.shenron.command.CommandSimpleLink;
import fr.litarvan.shenron.command.group.CommandGroup;
import fr.litarvan.shenron.command.group.CommandGroupCreate;
import fr.litarvan.shenron.command.group.CommandGroupJoin;
import fr.litarvan.shenron.command.group.CommandGroupLeave;
import fr.litarvan.shenron.command.group.CommandGroupTrigger;
import fr.litarvan.shenron.command.music.CommandMusicPlay;
import fr.litarvan.shenron.command.music.CommandMusicPop;
import fr.litarvan.shenron.command.music.CommandMusicQueue;
import fr.litarvan.shenron.command.music.CommandMusicSearch;
import fr.litarvan.shenron.middleware.AdminMiddleware;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.litarvan.shenron.command.CommandAddAdmin;
import fr.litarvan.shenron.command.CommandFAQ;
import fr.litarvan.shenron.command.music.CommandMusic;
import fr.litarvan.shenron.command.CommandWordReact;
import fr.litarvan.shenron.command.music.CommandMusicVolume;
import fr.litarvan.shenron.middleware.SDDMiddleware;
import fr.litarvan.shenron.middleware.SupportMiddleware;

public class Shenron implements IBot
{
    public static final String VERSION = "1.0.0";
    public static final String TRIGGERED_LINK = "https://www.growtopiagame.com/forums/attachment.php?attachmentid=132753&d=1469397141";
    public static final String OSEF_LINK = "https://www.youtube.com/watch?v=XoDY9vFAaG8";

    private static final Logger LOGGER = LogManager.getLogger("Shenron");

    @Inject
    private CommandManager commands;

    @Inject
    private ConfigProvider configs;

    @Override
    public void init()
    {
        // Bot initializing
        LOGGER.info("Loading Shenron v" + VERSION);

        // Setting up configs
        configs.from("config/admins.json");
        configs.from("config/groups.json");
        configs.from("config/shenron.json");
        configs.from("config/support.json");
        configs.from("config/youtube.json");

        // Loading commands
        commands.group().prefix(configs.at("shenron.prefix")).apply(this::commands);

        LOGGER.info("-> Loaded !");
    }

    private void commands()
    {
        commands.make("help", HelpCommand.class)
                .description("Affiche la liste des commandes")
                .register();

        music();

        commands.group().middlewares(SupportMiddleware.class).apply(() -> {
            commands.make("faq [target:user]", CommandFAQ.class)
                    .description(Markdown.bold("Support-Launcher only:") + " Affiche le lien de la FAQ (Admin: Si target renseigné, le blame et lui met le grade Pabo suivant)")
                    .register();
        });

        commands.group().middlewares(SDDMiddleware.class).apply(() -> {
            group();
        });

        commands.make("add-admin <scope:main|sdd|support> <targets:user...>", CommandAddAdmin.class)
                .description("Ajoute un administrateur")
                .middlewares(AdminMiddleware.class).register();

        commands.make("wr <message>", CommandWordReact.class)
                .description("Ajoute le message donné sous forme de réaction au dernier message")
                .register();

        commands.make("osef", new CommandSimpleLink(OSEF_LINK))
                .description("Affiche la vidéo 'On s'en bat les couilles'")
                .register();

        commands.make("triggered", new CommandSimpleLink(TRIGGERED_LINK))
                .description("Affiche le même 'triggered'")
                .register();

        commands.make("clear <amount:number>", CommandClear.class)
                .description("Supprime le nombre de message donnés à partir du dernier posté")
                .register()
                    .sub("where <where:before|after> <query> <amount:number>", CommandClearWhere.class)
                    .description("Supprime le nombre de message donné après ou avant un certains message (query correspond à une partie de son contenu, pour le rechercher)")
                    .register();
    }

    private void music()
    {
        Command music = commands.make("music [action:pause|unpause|next]", CommandMusic.class)
                                .description("Met pause/Enlève pause/Passe à la chanson suivante")
                                .register();

        music.sub("search <query>", CommandMusicSearch.class)
             .description("Fait une recherche de 'query' sur YouTube")
             .register();

        music.sub("play <url>", CommandMusicPlay.class)
             .description("Joue une musique YouTube depuis un url")
             .register();

        music.sub("volume [value:number]", CommandMusicVolume.class)
             .description("Affiche le volume actuel, ou si 'value' est donnée, le modifie")
             .register();

        music.sub("pop", CommandMusicPop.class)
             .description("Fait popper Shenron dans le channel vocal où vous êtes")
             .register();

        music.sub("queue", CommandMusicQueue.class)
             .description("Affiche la file d'attente des musiques")
             .register();
    }

    private void group()
    {
        Command group = commands.make("group", CommandGroup.class)
                                .register();

        group.sub("join <group>", CommandGroupJoin.class)
             .register();

        group.sub("leave [group]", CommandGroupLeave.class)
             .register();

        group.sub("create <name> [channel]", CommandGroupCreate.class)
             .register();

        group.sub("trigger <message> [emote#group...]", CommandGroupTrigger.class)
             .register();
    }

    public static void main(String[] args) throws LoginException, InterruptedException, RateLimitedException
    {
        if (args.length == 0)
        {
            LOGGER.fatal("You need to provide a bot token in argument");
            System.exit(1);
        }

        Krobot.start(args[0], Shenron.class);
    }
}
