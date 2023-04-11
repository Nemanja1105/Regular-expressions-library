import GenerisanjeKoda.JavaCodeGen;
import Interfejsi.IRegularLanguage;
import SpecifikacijaRegularnogJezika.Izuzeci.LexicalErrorException;
import SpecifikacijaRegularnogJezika.Izuzeci.SyntaxErrorException;
import SpecifikacijaRegularnogJezika.RegularLanguageCreator;
import SpecifikacijaRegularnogJezika.SpecificationAnalizer;
import Automati.Dka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Program
{
    public static void main(String[]args)
    {
        List<String> lines=null;
        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        }
        catch(IOException e)
        {
            System.out.println("Greska prilikom otvaranja fajla");
            System.exit(-1);
        }

        SpecificationAnalizer analizer=new SpecificationAnalizer(lines);
        IRegularLanguage automat=null;
        try
        {
            RegularLanguageCreator creator=new RegularLanguageCreator(analizer.getAllTokens());
            automat=creator.getRegularLanguage();
        }
        catch(SyntaxErrorException e)
        {
            System.out.println(e.getMessage());
        }
        catch(LexicalErrorException e)
        {
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        JavaCodeGen gen = new JavaCodeGen((Dka)automat.toDka(), "");
        try {
            gen.genCode();
        }
        catch(IOException e)
        {
            System.out.println("Greska prilikom otvaranja fajla");
            System.exit(-1);
        }
    }
}
