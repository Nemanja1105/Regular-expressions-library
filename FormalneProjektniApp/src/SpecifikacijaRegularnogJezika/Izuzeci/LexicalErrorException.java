package SpecifikacijaRegularnogJezika.Izuzeci;

public class LexicalErrorException extends Exception
{
    private int numberOfLine;
    public LexicalErrorException(int numberOfLine)
    {
        super("Leksicka greska u liniji broj:"+numberOfLine);
        this.numberOfLine=numberOfLine;
    }
    public LexicalErrorException(int numberOfLine,String message)
    {
        super(message);
        this.numberOfLine=numberOfLine;
    }
}