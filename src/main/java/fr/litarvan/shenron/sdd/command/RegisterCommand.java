package fr.litarvan.shenron.sdd.command;

import java.util.List;
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
import org.krobot.permission.UserRequires;
import org.krobot.util.MessageUtils;

@UserRequires({Permission.ADMINISTRATOR})
@Command(value = "register [new-member:user] [presentation-query:string...]", desc = "Ajoute un membre au SDD", aliases = "r")
public class RegisterCommand implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Role member = context.getGuild().getRolesByName("Membre", true).get(0);
        Role developer = context.getGuild().getRolesByName("Développeur", true).get(0);

        Guild guild = context.getGuild();

        Member newMember = args.get("new-member");

        if (!args.has("new-member"))
        {
            main:
            for (Message message : context.getChannel().getHistory().retrievePast(50).complete())
            {
                Member m = guild.getMember(message.getAuthor());
                List<Role> roles = m.getRoles();

                for (Role role : roles)
                {
                    if (role.getId().equals(member.getId()))
                    {
                        continue main;
                    }
                }

                newMember = m;
                break;
            }

            if (newMember == null)
            {
                return context.warn("Utilisateur introuvable", "Impossible de trouver le membre à ajouter, veuillez le renseigner en argument");
            }
        }

        context.info("Ajout en cours", "Ajout de " + newMember.getAsMention() + "...");

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

        guild.getController().addRolesToMember(newMember, member, developer).complete();

        String presentation = config.at("sdd.presentation")
                                    .replace("${user}", newMember.getAsMention())
                                    .replace("${presentation}", presentationMessage);

        String welcome = config.at("sdd.welcome")
                               .replace("${user}", newMember.getAsMention())
                               .replace("${groups}", guild.getTextChannelsByName("groupes", true).get(0).getAsMention());

        guild.getTextChannelsByName("presentation", true).get(0).sendMessage(presentation).complete();
        guild.getTextChannelsByName("spam-et-discussion", true).get(0).sendMessage(welcome).queue();

        context.getChannel().sendMessage("/clear after Haskell 250").queue();

        return null;
    }
}
