package fr.litarvan.shenron.lol;

import com.google.inject.Inject;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import org.krobot.MessageContext;
import org.krobot.config.ConfigProvider;
import org.krobot.util.Interact;

public class LolApi
{
    private ConfigProvider config;
    private RiotApi api;

    @Inject
    public LolApi(ConfigProvider config)
    {
        this.config = config;
        this.api = new RiotApi(new ApiConfig().setKey(config.at("lol.key")));
    }

    public void apply(MessageContext context, String given, BiConsumer<MessageContext, String> after)
    {
        String name = given;
        String saved = get(context.getUser().getIdLong());

        if (name == null)
        {
            name = saved;

            if (name == null)
            {
                context.warn("Erreur", "Veuillez fournir un nom d'utilisateur");
                return;
            }

            after.accept(context, name);
        }
        else if (saved == null)
        {
            String finalName = name;
            Interact.from(context.info("Retenir ?", "Voulez vous associer le pseudo '" + name + "' à votre compte ?"))
                    .on(Interact.YES, ctx -> {
                        set(ctx.getUser().getIdLong(), finalName);
                        after.accept(ctx, finalName);
                    })
                    .on(Interact.NO, ctx -> {
                        ctx.getMessage().delete().queue();
                        after.accept(ctx, finalName);
                    });
        }
    }

    public String get(long id)
    {
        for (SummonerName name : getNames())
        {
            if (name.getId() == id)
            {
                return name.getName();
            }
        }

        return null;
    }

    public void set(long id, String username)
    {
        config.get("lol").append("summoners", SummonerName[].class, new SummonerName(id, username));
    }

    public RiotApi getApi()
    {
        return api;
    }

    protected SummonerName[] getNames()
    {
        return config.at("lol.summoners", SummonerName[].class);
    }

    protected static class SummonerName
    {
        private long id;
        private String name;

        public SummonerName(long id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public long getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }
    }
}
