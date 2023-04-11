package Izuzeci;

public class LanguageNotFinallyException extends Exception
{
    public LanguageNotFinallyException()
    {
        super("Jezik nije konacan, pa nema najduzu rijec");
    }
    public LanguageNotFinallyException(String message)
    {
        super(message);
    }
}
