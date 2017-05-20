package fr.litarvan.shenron;

public class Trigger
{
    private String phrase;
    private String image;
    private String message;

    public Trigger(String phrase, String image, String message)
    {
        this.phrase = phrase;
        this.image = image;
    }

    public String getPhrase()
    {
        return phrase;
    }

    public String getImage()
    {
        return image;
    }

    public String getMessage()
    {
        return message;
    }
}
