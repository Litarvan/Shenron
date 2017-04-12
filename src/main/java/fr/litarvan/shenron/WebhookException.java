package fr.litarvan.shenron;

public class WebhookException extends Exception
{
    public WebhookException(String action, String channel)
    {
        super("Error while " + action + " webhook for the bot on channel : " + channel);
    }
}
