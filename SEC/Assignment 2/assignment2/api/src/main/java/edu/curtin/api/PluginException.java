package edu.curtin.api;

public class PluginException extends Exception
{
    public PluginException(String errorMessage, Throwable err)
    {
        super(errorMessage, err);
    }

    public PluginException(String errorMessage)
    {
        super(errorMessage);
    }
}
