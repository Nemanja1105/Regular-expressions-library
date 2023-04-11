package SpecifikacijaRegularnogJezika.Izuzeci;

public class SyntaxErrorException extends Exception
{
    public SyntaxErrorException()
    {
        super("Globalna sintaksna greska u specifikaciji");
    }

    public SyntaxErrorException(String message)
    {
        super(message);
    }
}