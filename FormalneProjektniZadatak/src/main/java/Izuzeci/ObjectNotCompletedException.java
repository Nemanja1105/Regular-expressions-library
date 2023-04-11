package Izuzeci;

public class ObjectNotCompletedException extends Exception
{
    public ObjectNotCompletedException()
    {
        super("Objekat nije u potpunosti kreiran!!");
    }
    public ObjectNotCompletedException(String message)
    {
        super(message);
    }
}
