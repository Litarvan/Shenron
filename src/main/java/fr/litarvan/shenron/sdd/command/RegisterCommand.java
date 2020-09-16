package fr.litarvan.shenron.sdd.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.inject.Inject;

import fr.litarvan.shenron.command.CommandClearWhere;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.command.GuildOnly;
import org.krobot.config.ConfigProvider;
import org.krobot.permission.BotRequires;
import org.krobot.permission.UserRequires;
import org.krobot.util.Interact;
import org.krobot.util.MessageUtils;

@GuildOnly
@UserRequires({ Permission.ADMINISTRATOR })
@BotRequires({ Permission.MANAGE_ROLES, Permission.MESSAGE_MANAGE })
@Command(value = "register [new-member:user] [presentation-query:string...]", desc = "Ajoute un membre au SDD", aliases = "r")
public class RegisterCommand implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Role memberRole = context.getGuild().getRolesByName("Membre", true).get(0);
        Guild guild = context.getGuild();
        Member newMember = args.has("new-member") ? guild.retrieveMember(args.get("new-member")).complete() : null;

        boolean deleteAfter = true;

        if (!args.has("new-member"))
        {
            List<Message> history = context.getChannel().getHistory().retrievePast(50).complete();
            List<Member> members = history
                .stream()
                .map(msg -> {
                    try {
                        return guild.retrieveMember(msg.getAuthor()).complete();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(m -> m != null && m.getRoles().stream().noneMatch(r -> r.getId().equals(memberRole.getId())))
                .collect(Collectors.toList());

            deleteAfter = members.size() > 1;

            Optional<Member> member = members.stream().findFirst();

            if (!member.isPresent())
            {
                return context.warn("Utilisateur introuvable", "Impossible de trouver le membre à ajouter, veuillez le renseigner en argument");
            }

            newMember = member.get();
        }

        if (newMember == null)
        {
            return context.warn("Utilisateur introuvable", "Impossible de trouver cet utilisateur");
        }

        String presentationMessage;

        if (args.has("presentation-query"))
        {
            String query = String.join("", args.get("presentation-query", String[].class));
            Message msg = MessageUtils.search((TextChannel) context.getChannel(), query, 100);

            if (msg == null)
            {
                return context.warn("Message introuvable", "Impossible de trouver le message de présentation dans les 100 derniers messages avec la recherche '" + query + "'");
            }

            presentationMessage = msg.getContentRaw();
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
                    if (message.getContentRaw().length() >= longest.length())
                    {
                        longest = message.getContentRaw();
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
        boolean finalDeleteAfter = deleteAfter;

        Interact.from(context.info("Voulez-vous ajouter '" + newMember.getEffectiveName() + "' ?", presentationMessage).join(), context.getUser(), 15000L)
                .thenDelete()
                .on(Interact.YES, (c) -> {
                    Message message = context.info("Ajout en cours", "Ajout de " + finalNewMember.getAsMention() + "...").join();

                    if (!finalDeleteAfter) {
                        MessageUtils.deleteAfter(message, 2500);
                    }
                    guild.addRoleToMember(finalNewMember, memberRole).complete();

                    String presentation = config.at("sdd.presentation")
                                                .replace("${user}", finalNewMember.getAsMention())
                                                .replace("${presentation}", presentationMessage);

                    String welcome = config.at("sdd.welcome")
                                           .replace("${user}", finalNewMember.getAsMention())
                                           .replace("${groups}", guild.getTextChannelsByName("groupes", true).get(0).getAsMention());

                    guild.getTextChannelById("259072815645327362").sendMessage(presentation).complete();
                    guild.getTextChannelById("186941943941562369").sendMessage(welcome).queue();

                    if (finalDeleteAfter) {
                        CommandClearWhere.clearWhere(true, context, 100, "haskell", false);
                    } else {
                        MessageUtils.deleteAfter(context.info("Messages non supprimés", "Messages non supprimés dû à la présence de plusieurs nouveaux membres").join(), 2500);
                    }
                })
                .on(Interact.NO, (c) -> {});

        return null;
    }
}
