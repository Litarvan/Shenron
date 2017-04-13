package fr.litarvan.shenron.command;

import fr.litarvan.krobot.command.CommandContext;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.SuppliedArgument;
import fr.litarvan.krobot.config.ConfigProvider;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

public class CommandFAQ implements CommandHandler
{
    @Inject
    private ConfigProvider config;

    @Override
    public void handle(@NotNull CommandContext context, @NotNull Map<String, SuppliedArgument> args)
    {
        String link = config.at("support.faq");

        if (!args.containsKey("target") || !context.getMember().hasPermission(Permission.ADMINISTRATOR))
        {
            context.sendMessage("FAQ : " + link);
            return;
        }

        Member member = context.getGuild().getMember(args.get("target").getAsUser());

        context.sendMessage(config.at("support.message"), member.getUser().getAsMention(), link);

        Role moche = context.getGuild().getRolesByName("Pabo", true).get(0);
        Role hyperMoche = context.getGuild().getRolesByName("Hyper Pabo", true).get(0);
        Role ultraMoche = context.getGuild().getRolesByName("Ultra Pabo", true).get(0);

        if (context.getGuild().getMembersWithRoles(ultraMoche).contains(member))
        {
            context.sendMessage("En plus t'es Ultra Pabo, t'es vraiment le pire des pabo omg");
        }
        else if (context.getGuild().getMembersWithRoles(hyperMoche).contains(member))
        {
            context.getGuild().getController().addRolesToMember(member, ultraMoche).queue();
        }
        else if (context.getGuild().getMembersWithRoles(moche).contains(member))
        {
            context.getGuild().getController().addRolesToMember(member, hyperMoche).queue();
        }
        else
        {
            context.getGuild().getController().addRolesToMember(member, moche).queue();
        }
    }
}
