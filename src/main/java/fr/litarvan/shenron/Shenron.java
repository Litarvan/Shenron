package fr.litarvan.shenron;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.krobot.IBot;
import org.krobot.Krobot;
import org.krobot.command.Command;
import org.krobot.command.CommandManager;
import org.krobot.command.HelpCommand;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Markdown;
import fr.litarvan.shenron.command.*;
import fr.litarvan.shenron.command.group.*;
import fr.litarvan.shenron.command.music.*;
import fr.litarvan.shenron.middleware.*;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shenron implements IBot
{
    public static final String VERSION = "2.1.0";
    public static final String TRIGGERED_LINK = "https://www.growtopiagame.com/forums/attachment.php?attachmentid=132753&d=1469397141";
    public static final String OSEF_LINK = "https://www.youtube.com/watch?v=XoDY9vFAaG8";

    private static final Logger LOGGER = LogManager.getLogger("Shenron");

    @Inject
    private JDA jda;

    @Inject
    private CommandManager commands;

    @Inject
    private ConfigProvider configs;

    @Override
    public void init()
    {
        // Bot initializing
        LOGGER.info("Loading Shenron v" + VERSION);

        jda.addEventListener(this, Krobot.injector().getInstance(GroupListener.class));

        // Setting up configs
        configs.from("config/groups.json");
        configs.from("config/shenron.json");
        configs.from("config/support.json");
        configs.from("config/youtube.json");
        configs.from("config/triggers.json");

        if (!configs.from("config/youtube.json").getFile().exists())
        {
            LOGGER.fatal("You need to create config/youtube.json file like :\n{\n    \"app-name\": \"your-google-app-name\",\n    \"api-key\": \"your-google-api-key\"\n}");
            System.exit(1);
        }

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

        group();

        commands.make("wr <message>", CommandWordReact.class)
                .description("Ajoute le message donné sous forme de réaction au dernier message")
                .register();

        commands.make("osef", new CommandSimpleLink(OSEF_LINK))
                .description("Affiche la vidéo 'On s'en bat les couilles'")
                .register();

        commands.make("triggered", new CommandSimpleLink(TRIGGERED_LINK))
                .description("Affiche le même 'triggered'")
                .register();

        commands.make("timer <action:start|stop>", CommandTimer.class)
                .description("Display an animated emoji timer on this channel")
                .register();

        commands.make("add", (context, args) -> context.sendMessage(configs.at("shenron.add-link")))
                .description("Affiche le lien d'ajout du bot")
                .register();

        Command clear = commands.make("clear <amount:number>", CommandClear.class)
                .description("Supprime le nombre de message donnés à partir du dernier posté")
                .register();

        clear.sub("before <query> <amount:number>", new CommandClearWhere(false))
             .description("Supprime le nombre de message donné avant un certains message (query correspond à une partie de son contenu, pour le rechercher)")
             .register();

        clear.sub("after <query> <amount:number>", new CommandClearWhere(true))
             .description("Supprime le nombre de message donné après un certains message (query correspond à une partie de son contenu, pour le rechercher)")
             .register();
    }

    private void music()
    {
        Command music = commands.make("music [action:pause|unpause|next|stop]", CommandMusic.class)
                                .description("Met pause/Enlève pause/Stop/Passe à la chanson suivante")
                                .register();

        music.sub("search <query...>", CommandMusicSearch.class)
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

        music.sub("drop", CommandMusicDrop.class)
             .description("Fait quitter Shenron du channel vocal")
             .register();

        music.sub("queue", CommandMusicQueue.class)
             .description("Affiche la file d'attente des musiques")
             .register();
    }

    private void group()
    {
        Command group = commands.make("group", CommandGroup.class)
                                .description("Affiche la liste des groupes")
                                .register();

        group.sub("join <group>", CommandGroupJoin.class)
             .description("Vous ajoute dans le groupe donné")
             .register();

        group.sub("leave [group]", CommandGroupLeave.class)
             .description("Quitte le groupe donné ou le groupe où le message est envoyé")
             .register();

        group.sub("create <name> [channel]", CommandGroupCreate.class)
             .description("(Admin) Créé un groupe")
             .register();

        group.sub("trigger <message> [emote#group...]", CommandGroupTrigger.class)
             .description("Créé un message avec des réactions permettant de rejoindre des groupes")
             .register();
    }

    @SubscribeEvent
    public void onMessage(MessageReceivedEvent event)
    {
       for (Trigger trigger : configs.at("triggers.triggers", Trigger[].class))
       {
           if (StringUtils.getLevenshteinDistance(trigger.getPhrase().toLowerCase(), event.getMessage().getContent().toLowerCase()) < 5)
           {
               event.getChannel().sendMessage(trigger.getImage()).queue();

               if (trigger.getMessage() != null)
               {
                   event.getChannel().sendMessage(trigger.getMessage()).queue();
               }

               return;
           }
       }
    }

    public static void main(String[] args) throws LoginException, InterruptedException, RateLimitedException, IOException
    {
        String token = null;
        File file = new File("shenron.token");

        if (file.exists())
        {
            token = FileUtils.readFileToString(file, Charset.defaultCharset());
        }

        if (args.length > 0)
        {
            token = args[0];
        }

        if (token == null)
        {
            LOGGER.fatal("You need to provide a bot token in argument or in a shenron.token file");
            System.exit(1);
        }

        Krobot.start(token, Shenron.class);
    }
}
