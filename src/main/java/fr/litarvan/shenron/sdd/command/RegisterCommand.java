package fr.litarvan.shenron.sdd.command;

import fr.litarvan.shenron.util.Interact;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.MessageUtils;

@UserRequires({Permission.ADMINISTRATOR})
@BotRequires({Permission.MANAGE_ROLES, Permission.MESSAGE_MANAGE})
@Command(value = "register [new-member:user] [presentation-query:string...]", desc = "Ajoute un membre au SDD", aliases = "r")
public class RegisterCommand implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Role memberRole = context.getGuild().getRolesByName("Membre", true).get(0);
        Role developer = context.getGuild().getRolesByName("Développeur", true).get(0);

        Guild guild = context.getGuild();

        Member newMember = args.has("new-member") ? guild.getMember(args.get("new-member")) : null;

        if (!args.has("new-member"))
        {
            List<Message> history = context.getChannel().getHistory().retrievePast(50).complete();
            Optional<Member> member = history
                .stream()
                .map(msg -> guild.getMember(msg.getAuthor()))
                .filter(m -> m.getRoles().stream().noneMatch(r -> r.getId().equals(memberRole.getId())))
                .findFirst();

            if (!member.isPresent())
            {
                return context.warn("Utilisateur introuvable", "Impossible de trouver le membre à ajouter, veuillez le renseigner en argument");
            }

            newMember = member.get();
        }

        String presentationMessage;

        if (args.has("presentation-query"))
        {
            String query = args.get("presentation-query");
            Message msg = MessageUtils.search(context.getChannel(), query, 100);

            if (msg == null)
            {
                return context.warn("Message introuvable", "Impossible de trouver le message de présentation dans les 100 derniers messages avec la recherche '" + query + "'");
            }

            presentationMessage = msg.getContent();
        }
        else
        {
            List<Message> history = context.getChannel().getHistory().retrievePast(100).complete();
            String longest = "";

            for (int i = history.size() - 1; i > 0; i--)
            {
                Message message = history.get(i);

                if (message.getAuthor().getId().equals(newMember.getUser().getId()))
                {
                    if (message.getContent().length() >= longest.length())
                    {
                        longest = message.getContent();
                    }
                }
            }

            if (longest.trim().length() < 5)
            {
                return context.warn("Message introuvable", "Impossible de trouver un message de présentation plausible venant de '" + newMember.getAsMention() + "'");
            }

            presentationMessage = longest;
        }

        Member finalNewMember = newMember;
        Interact.from(context.info("Voulez-vous ajouter '" + newMember.getEffectiveName() + "' ?", presentationMessage))
                .on(Interact.YES, (c) -> {
                    context.info("Ajout en cours", "Ajout de " + finalNewMember.getAsMention() + "...");

                    guild.getController().addRolesToMember(finalNewMember, memberRole, developer).complete();

                    String presentation = config.at("sdd.presentation")
                                                .replace("${user}", finalNewMember.getAsMention())
                                                .replace("${presentation}", presentationMessage);

                    String welcome = config.at("sdd.welcome")
                                           .replace("${user}", finalNewMember.getAsMention())
                                           .replace("${groups}", guild.getTextChannelsByName("groupes", true).get(0).getAsMention());

                    guild.getTextChannelsByName("presentation", true).get(0).sendMessage(presentation).complete();
                    guild.getTextChannelsByName("spam-et-discussion", true).get(0).sendMessage(welcome).queue();

                    context.send("/clear after haskell 100");
                })
                .on(Interact.NO, (c) -> {
                    c.getMessage().delete().queue();
                });

        return null;
    }
}
