package fr.litarvan.shenron;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Music
{
    private static final Logger LOGGER = LogManager.getLogger("Music");

    private String name;
    private String url;

    private File file;

    public Music(String name, String url)
    {
        this.name = name;
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public String getUrl()
    {
        return url;
    }

    public File getFile()
    {
        return file;
    }

    public static String parseTime(long duration)
    {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);

        return String.format("%dm%02ds",minutes , TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes));
    }
}
