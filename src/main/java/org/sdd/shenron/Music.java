package org.sdd.shenron;

import com.github.axet.vget.VGet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

    public void download() throws IOException
    {
        file = new File("cache/mp3/" + url.substring(url.lastIndexOf("=") + 1));

        if (file.exists())
        {
            return;
        }

        File videoFolder = new File("cache/video/", file.getName());

        videoFolder.mkdirs();
        file.getParentFile().mkdirs();

        URL url = new URL(this.url);
        LOGGER.info("Downloading " + url + " to " + videoFolder.getAbsolutePath());

        VGet vget = new VGet(url, videoFolder);
        vget.download();

        LOGGER.info("Downloaded " + url + " to " + videoFolder.getAbsolutePath());

        boolean moved = false;

        for (File file : videoFolder.listFiles())
        {
            if (file.getName().endsWith(".webm"))
            {
                file.renameTo(this.file);
                moved = true;
            }
        }

        if (!moved)
        {
            throw new IllegalStateException("Can't find the video file (files: " + Arrays.toString(videoFolder.listFiles()) + ")");
        }
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
