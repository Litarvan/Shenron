package org.sdd.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class CommandFAQ implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(CommandContext context, Map<String, SuppliedArgument> args)
    {
        String link = config.at("support.faq");

        if (!args.containsKey("target") || config.at("support.admins." + context.getUser().getId()) == null)
        {
            context.getChannel().sendMessage("FAQ : " + link).queue();
            return;
        }

        Guild guild = context.getChannel().getGuild();
        Member member = guild.getMember(args.get("target").getAsUser());

        context.getChannel().sendMessage(config.at("support.message"), member.getUser().getAsMention(), link).queue();

        Role moche = guild.getRolesByName("Pabo", true).get(0);
        Role hyperMoche = guild.getRolesByName("Hyper Pabo", true).get(0);
        Role ultraMoche = guild.getRolesByName("Ultra Pabo", true).get(0);

        if (guild.getMembersWithRoles(ultraMoche).contains(member))
        {
            context.getChannel().sendMessage("En plus t'es Ultra Pabo, t'es vraiment le pire des pabo omg").queue();
        }
        else if (guild.getMembersWithRoles(hyperMoche).contains(member))
        {
            guild.getController().addRolesToMember(member, ultraMoche).queue();
        }
        else if (guild.getMembersWithRoles(moche).contains(member))
        {
            guild.getController().addRolesToMember(member, hyperMoche).queue();
        }
        else
        {
            guild.getController().addRolesToMember(member, moche).queue();
        }
    }
}
