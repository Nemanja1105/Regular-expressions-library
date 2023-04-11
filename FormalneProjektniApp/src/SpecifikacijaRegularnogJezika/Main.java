package SpecifikacijaRegularnogJezika;

import Interfejsi.IRegularLanguage;
import SpecifikacijaRegularnogJezika.Izuzeci.LexicalErrorException;
import SpecifikacijaRegularnogJezika.Izuzeci.SyntaxErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import Automati.Dka;
import SpecifikacijaRegularnogJezika.RegularLanguageCreator;
import SpecifikacijaRegularnogJezika.SpecificationAnalizer;

public class Main
{
    public static void main(String[]args)throws IOException
    {

        List<String> lines= Files.readAllLines(Paths.get("dkaProba.txt"));
        SpecificationAnalizer analizer=new SpecificationAnalizer(lines);
        IRegularLanguage  automat=null;
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
    }
}
