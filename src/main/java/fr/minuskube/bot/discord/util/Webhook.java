package fr.minuskube.bot.discord.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Webhook
{
    private static final Logger LOGGER = LogManager.getLogger("MK-Webhook");

    private static Map<TextChannel, Webhook> webhooks = new HashMap<>();
    private TextChannel channel;
    private String id;
    private String token;

    private Webhook(TextChannel channel, String id, String token)
    {
        this.channel = channel;

        this.id = id;
        this.token = token;
    }

    private static Webhook createBotHook(JDAImpl jda, TextChannel channel) throws ExecutionException, InterruptedException
    {
        String avatar;

        try
        {
            BufferedImage img = ImageIO.read(Webhook.class.getResourceAsStream("/shenron.jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(img, "jpg", baos);
            baos.flush();

            Base64 base = new Base64(false);
            avatar = base.encodeToString(baos.toByteArray());
            baos.close();
        }
        catch (IOException e)
        {
            avatar = "";
            LOGGER.error("Can't retrieve the logo: ", e);
        }

        String finalAvatar = avatar;

        RestAction<JSONObject> action = new RestAction<JSONObject>(jda, Route.Custom.POST_ROUTE.compile("channels/" + channel.getId() + "/webhooks"), new JSONObject(new HashMap<String, Object>()
        {
            {
                put("name", "Shenron Hook");
                put("avatar", "data:image/jpeg;base64," + finalAvatar);
            }
        }))
        {

            @SuppressWarnings("unchecked")
            @Override
            protected void handleResponse(Response response, Request request)
            {
                if (response.isOk())
                {
                    request.onSuccess(response.getObject());
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };

        JSONObject hook = action.complete();
        webhooks.put(channel, new Webhook(channel, hook.getString("id"), hook.getString("token")));

        return webhooks.get(channel);
    }

    public static void initBotHooks(JDAImpl jda) throws ExecutionException, InterruptedException
    {
        for (Guild guild : jda.getGuilds())
        {
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS))
                continue;

            RestAction<JSONArray> action = new RestAction<JSONArray>(jda, Route.Custom.GET_ROUTE.compile("guilds/" + guild.getId() + "/webhooks"), null)
            {

                @SuppressWarnings("unchecked")
                @Override
                protected void handleResponse(Response response, Request request)
                {
                    if (response.isOk())
                    {
                        request.onSuccess(response.getArray());
                    }
                    else
                    {
                        request.onFailure(response);
                    }
                }
            };

            JSONArray hooks = action.complete();

            for (Object obj : hooks)
            {
                JSONObject hook = (JSONObject) obj;
                JSONObject user = hook.getJSONObject("user");

                String channelId = hook.getString("channel_id");
                String id = hook.getString("id");
                String token = hook.getString("token");

                TextChannel channel = jda.getTextChannelById(channelId);

                if (user.getString("id").equals(jda.getSelfUser().getId()))
                {
                    webhooks.put(channel, new Webhook(channel, id, token));
                }
            }
        }
    }

    public static Webhook getBotHook(JDAImpl jda, TextChannel channel) throws ExecutionException, InterruptedException
    {
        return !webhooks.containsKey(channel) ? createBotHook(jda, channel) : webhooks.get(channel);
    }

    public static Map<TextChannel, Webhook> getBotHooks()
    {
        return webhooks;
    }

    public boolean execute(JDAImpl jda, JSONObject obj) throws ExecutionException, InterruptedException
    {
        RestAction<Void> action = new RestAction<Void>(jda, Route.Custom.POST_ROUTE.compile("webhooks/" + id + "/" + token), obj)
        {

            @SuppressWarnings("unchecked")
            @Override
            protected void handleResponse(Response response, Request request)
            {
                if (response.isOk())
                {
                    request.onSuccess(null);
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };

        try
        {
            action.queue();
            return true;
        }
        catch (ErrorResponseException e)
        {
            if (e.getErrorResponse().getCode() == 10015 || e.getErrorResponse().getCode() == 50027)
            {
                LOGGER.info("Creating new webhook.");
                return createBotHook(jda, channel).execute(jda, obj);
            }

            LOGGER.error("Can't execute webhook: ", e);
            return false;
        }
    }

    public String getId()
    {
        return id;
    }

    public String getToken()
    {
        return token;
    }

}