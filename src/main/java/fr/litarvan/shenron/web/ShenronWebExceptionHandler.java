package fr.litarvan.shenron.web;

import java.io.File;
import java.io.IOException;

import fr.litarvan.paladin.ExceptionHandler;
import fr.litarvan.paladin.Paladin;
import fr.litarvan.paladin.http.Request;
import fr.litarvan.paladin.http.Response;
import fr.litarvan.paladin.http.routing.RouteNotFoundException;
import org.apache.commons.io.FileUtils;

public class ShenronWebExceptionHandler extends ExceptionHandler
{
    // TODO: ...

    private String webRoot;

    public ShenronWebExceptionHandler(Paladin paladin)
    {
        this.webRoot = paladin.getConfigManager().at("app.webRoot");
    }

    @Override
    public Object handle(Exception exception, Request request, Response response)
    {
        if (exception instanceof RouteNotFoundException && !request.getUri().startsWith("/api"))
        {
            File file = new File(webRoot, request.getUri());

            if (!file.exists() || file.isDirectory())
            {
                file = new File(webRoot, "index.html");
            }

            try
            {
                return FileUtils.readFileToByteArray(file);
            }
            catch (IOException e)
            {
                exception = e;
            }
        }

        return super.handle(exception, request, response);
    }
}
