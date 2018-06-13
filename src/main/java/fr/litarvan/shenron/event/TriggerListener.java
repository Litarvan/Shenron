package fr.litarvan.shenron.event;

import fr.litarvan.shenron.model.Trigger;
import javax.inject.Inject;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.krobot.config.ConfigProvider;

public class TriggerListener
{
    private ConfigProvider configs;

    @Inject
    public TriggerListener(ConfigProvider configs)
    {
        this.configs = configs;
    }

    @SubscribeEvent
    public void onMessage(MessageReceivedEvent event)
    {
        
        if(event.getMessage().getContent().toLowerCase().indexOf("gratuit") != -1 || event.getMessage().getContent().toLowerCase().indexOf("gratis") != -1 || event.getMessage().getContent().toLowerCase().indexOf("free") != -1){
            event.getGuild().getController().kick(event.getAuthor()).queue();
            return;
        }else{
            for (Trigger trigger : configs.at("triggers.triggers", Trigger[].class))
            {
                if (StringUtils.getLevenshteinDistance(trigger.getPhrase().toLowerCase(), event.getMessage().getContent().toLowerCase()) < trigger.getSensitivity())
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
    }
}
