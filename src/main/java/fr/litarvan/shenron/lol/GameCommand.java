package fr.litarvan.shenron.lol;

import java.util.Calendar;
import javax.inject.Inject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.league.dto.LeaguePosition;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.static_data.constant.Locale;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.constant.Platform;
import org.krobot.MessageContext;
import org.krobot.command.ArgumentMap;
import org.krobot.command.Command;
import org.krobot.command.CommandHandler;
import org.krobot.util.Dialog;
import org.krobot.util.Markdown;

@Command(value = "game [username]", desc = "Affiche les informations de la partie de League Of Legends en cours de 'username'", aliases = "l")
public class GameCommand implements CommandHandler
{
    @Inject
    private LolApi api;

    @Override
    public Object handle(MessageContext context, ArgumentMap args) throws Exception
    {
        Message message = context.info("Veuillez patienter", "Réception des informations...").get();

        this.api.apply(context, args.get("username"), (ctx, name) -> {
            try
            {
                printInfos(ctx, name);
            }
            catch (Exception e)
            {
                context.error("Erreur", "Impossible de recevoir les informations ! " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }

            message.delete().queue();
        });
        return null;
    }

    protected void printInfos(MessageContext context, String name) throws RiotApiException
    {
        CurrentGameInfo infos;
        try
        {
            long id = this.api.getApi().getSummonerByName(Platform.EUW, name).getId();
            infos = this.api.getApi().getActiveGameBySummoner(Platform.EUW, id);
        }
        catch (RiotApiException e)
        {
            if (e.getMessage().contains("404"))
            {
                context.error("Erreur", "Le joueur '" + name + "' n'éxiste pas ou n'est pas en game");
                return;
            }

            throw e;
        }

        String redTeam = "";
        String blueTeam = "";

        for (CurrentGameParticipant participant : infos.getParticipants())
        {
            Champion champion = this.api.getApi().getDataChampion(Platform.EUW, participant.getChampionId(), Locale.FR_FR, null);

            String string = Markdown.bold(champion.getName());

            for (LeaguePosition position : this.api.getApi().getLeaguePositionsBySummonerId(Platform.EUW, participant.getSummonerId()))
            {
                if (!position.getQueueType().equalsIgnoreCase("RANKED_SOLO_5x5"))
                {
                    continue;
                }

                string += " - " + Markdown.bold(position.getTier() + " " + position.getRank());
            }

            string += " - " + participant.getSummonerName();

            if (participant.getTeamId() == 200)
            {
                blueTeam += string + "\n";
            }
            else
            {
                redTeam += string + "\n";
            }

            try
            {
                Thread.sleep(200L); // Rate limit until project is validated
            }
            catch (Exception e) {}
        }

        long start = infos.getGameStartTime();
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(start);

        String startString = parseDate(startCal, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);

        Calendar lengthCal = Calendar.getInstance();
        lengthCal.set(Calendar.SECOND, (int) infos.getGameLength());

        String title = "Partie en cours de '" + name + "' ";
        title += Markdown.bold("[" + parseDate(lengthCal, Calendar.MINUTE, Calendar.SECOND) + "]");
        title += " depuis " + startString + "";

        context.send(new EmbedBuilder()
                         .setTitle(title)
                         .setColor(Dialog.INFO_COLOR)
                         .setImage(Dialog.INFO_ICON)
                         .addField("Équipe bleue", blueTeam, false)
                         .addField("Équipe rouge", redTeam, false));
    }

    protected String parseDate(Calendar calendar, int... fields)
    {
        String result = "";

        for (int i = 0; i < fields.length; i++)
        {
            String string = String.valueOf(calendar.get(fields[i]));

            if (string.length() == 1)
            {
                string = "0" + string;
            }

            result += string;

            if (i + 1 < fields.length)
            {
                result += ":";
            }
        }

        return result;
    }
}
