package Izuzeci;

public class DifferentAlphabetException extends Exception
{
    public DifferentAlphabetException()
    {
        super("Operacije nad jezicima nisu podrzane za jezike sa razlicitim alfabetom");
    }
    public DifferentAlphabetException(String message)
    {
        super(message);
    }
}
