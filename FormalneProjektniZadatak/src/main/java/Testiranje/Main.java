package Testiranje;

import Automati.DkaMinimization;
import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.ObjectNotCompletedException;
import PomocneStrukturePodataka.Pair;
import Automati.Dka;
import Automati.EpsilonNka;
import RegularniIzraz.RegularExpression;
import RegularniIzraz.RegularToPostfixConvertor;

public class Main {
    public static void main(String[] args)throws Exception {
        // pairTest();
        //dkaTest();
         //epsilonNkaTest();
        //dkaMinimizeTest();
       // toDkaTest();
        //regexTest();
        //languageEqualsTest();
        /*RegularExpression reg1=new RegularExpression(new Character[]{'a','b'});
        RegularExpression reg2=new RegularExpression(new Character[]{'a','b'});
        reg1.setRegex("a.b");
        reg2.setRegex("b.b*");
        IRegularLanguage novi=reg1.concatenation(reg2);
        //IRegularLanguage novi2=reg1.kleeneStar();
        IRegularLanguage novi2=reg1.complement();
        IRegularLanguage novi3=reg1.difference(reg2);
     //   System.out.println(novi2.checkString("ab"));
     //   System.out.println(reg1.languageEquals(reg2));
       // System.out.println(reg1.toENka().union(reg2.toENka()).checkString("bbbbbbb"));
        Dka automat = new Dka(new Character[]{'a', 'b'}, "p0");
        automat.addTransition(Pair.makePair("p0", 'a'), "p0");
        automat.addTransition(Pair.makePair("p0", 'b'), "p1");
        automat.addTransition(Pair.makePair("p1", 'a'), "p1");
        automat.addTransition(Pair.makePair("p1", 'b'), "p0");
        automat.addFinalState("p1");
       // System.out.println(automat.checkString("bbb"));

       IRegularLanguage comp= automat.complement();
       IRegularLanguage temp=automat.union(reg1.toENka());
      // System.out.println(temp.checkString("ab"));
        //System.out.println(comp.checkString("bbb"));
        EpsilonNka automat1 = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
       automat1.addTransition(Pair.makePair("q0",'a'),"q1");
       automat1.addTransition(Pair.makePair("q1",'b'),new String[]{"q2","q4","q5"});
       automat1.addTransition(Pair.makePair("q2",'b'),"q3");
       automat1.addTransition(Pair.makePair("q3",'a'),"q1");
       automat1.addTransition(Pair.makePair("q4",'b'),"q1");
       automat1.addFinalState("q5");
       IRegularLanguage test5=automat.concatenation(automat1);
       IRegularLanguage test6=automat.kleeneStar().complement();
      //System.out.println(test6.checkString("aaa"));
      // System.out.println(automat1.checkString("abb"));
       IRegularLanguage klinova=automat1.kleeneStar();
      // System.out.println(klinova.checkString("abababab"));
       IRegularLanguage conc=automat.concatenation(automat1);
       //System.out.println(conc.checkString("bbb"));
      // IRegularLanguage conc2=reg1.toENka().concatenation(reg2);
       //System.out.println(conc2.checkString("ab"));
        RegularExpression r1=new RegularExpression(new Character[]{'a','b'});
        RegularExpression r2=new RegularExpression(new Character[]{'a','b'});
        r1.setRegex("a*.b");
        r2.setRegex("a.b.a");
        IRegularLanguage testReg=(Dka)(r1.union(r2).toENka().intersection(r1).toDka());
        System.out.println(testReg.checkString("aaaaab"));*/
        /*RegularExpression r1=new RegularExpression(new Character[]{'a','b'});
        r1.setRegex("a.b|a.a.b|b");//true ako je konacan
        System.out.println(r1.finalityOfLanguage());
        System.out.println(r1.lenghtOfLongestWord());*/
        /*Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        System.out.println(automat.finalityOfLanguage());
        System.out.println(automat.lenghtOfLongestWord());*/


       /* }
        catch(DifferentAlphabetException e)
        {

        }*/

        RegularExpression r1=new RegularExpression(new Character[]{'a','b'});
        RegularExpression r2=new RegularExpression(new Character[]{'a','b'});
        RegularExpression r3=new RegularExpression(new Character[]{'a','b'});
        r3.setRegex("a*");
        r1.setRegex("b*.a");
        r2.setRegex("a.b.a");
        try {
            IRegularLanguage testReg = r1.union(r2).kleeneStar().concatenation(r3).toENka();

        }
        catch( DifferentAlphabetException e)
        {

        }




    }

    public static void pairTest() {
        Pair<String, Integer> par = new Pair<String, Integer>("Q1", 1);
        Pair<String, Integer> par1 = new Pair<String, Integer>("Q1", 1);
        System.out.println(par);
        System.out.println(par1);
        System.out.println(par.hashCode());
        System.out.println(par1.hashCode());
        System.out.println(par1.equals(par));
        var par2 = Pair.makePair("Q1", 1);
        System.out.println(par2.hashCode());
    }

    public static void dkaTest() {
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        boolean status = automat.checkString("01011");
        System.out.println(status);
    }

    public static void epsilonNkaTest() {
        EpsilonNka automat = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
       /* automat.addStates(new String[]{"q0","q1","q2","q3","q4","q5"});
        automat.addTransition(Pair.makePair("q0",'a'),"q1");
        automat.addTransition(Pair.makePair("q1",'b'),new String[]{"q2","q4","q5"});
        automat.addTransition(Pair.makePair("q2",'b'),"q3");
        automat.addTransition(Pair.makePair("q3",'a'),"q1");
        automat.addTransition(Pair.makePair("q4",'b'),"q1");
        automat.addFinalState("q5");
        boolean status=automat.checkString("abbabab");
        System.out.println(status);*/
        automat.addStates(new String[]{"q1","q2","q3","q4"});
        automat.addTransition(Pair.makePair("q0", 'a'), "q0");
        automat.addTransition(Pair.makePair("q0", EpsilonNka.EPSILON), "q1");
        automat.addTransition(Pair.makePair("q1", 'b'), new String[]{"q4", "q2"});
        automat.addTransition(Pair.makePair("q1", EpsilonNka.EPSILON), "q0");
        automat.addTransition(Pair.makePair("q4", 'a'), "q1");
        automat.addTransition(Pair.makePair("q2", 'a'), new String[]{"q2", "q3"});
        automat.addTransition(Pair.makePair("q3", 'b'), "q3");
        automat.addTransition(Pair.makePair("q3", EpsilonNka.EPSILON), "q1");
        automat.addFinalState("q1");
        System.out.println(automat.checkString("a"));
        System.out.println(automat.closure("q0"));
    }

    public static void dkaMinimizeTest() {
        Dka automat = new Dka(new Character[]{'a', 'b'}, "q0");
        automat.addTransition(Pair.makePair("q0", 'a'), "q6");
        automat.addTransition(Pair.makePair("q0", 'b'), "q1");

        automat.addTransition(Pair.makePair("q1", 'a'), "q2");
        automat.addTransition(Pair.makePair("q1", 'b'), "q4");

        automat.addTransition(Pair.makePair("q6", 'a'), "q4");
        automat.addTransition(Pair.makePair("q6", 'b'), "q7");

        automat.addTransition(Pair.makePair("q2", 'a'), "q5");
        automat.addTransition(Pair.makePair("q2", 'b'), "q3");

        automat.addTransition(Pair.makePair("q4", 'a'), "q6");
        automat.addTransition(Pair.makePair("q4", 'b'), "q8");

        automat.addTransition(Pair.makePair("q7", 'a'), "q8");
        automat.addTransition(Pair.makePair("q7", 'b'), "q7");

        automat.addTransition(Pair.makePair("q5", 'a'), "q2");
        automat.addTransition(Pair.makePair("q5", 'b'), "q3");

        automat.addTransition(Pair.makePair("q3", 'a'), "q8");
        automat.addTransition(Pair.makePair("q3", 'b'), "q3");

        automat.addTransition(Pair.makePair("q8", 'a'), "q7");
        automat.addTransition(Pair.makePair("q8", 'b'), "q4");

        automat.addTransition(Pair.makePair("q9", 'a'), "q10");
        automat.addTransition(Pair.makePair("q9", 'b'), "q8");

        automat.addTransition(Pair.makePair("q10", 'a'), "q3");
        automat.addTransition(Pair.makePair("q10", 'b'), "q9");
        automat.addFinalState(new String[]{"q1", "q4", "q8"});
        DkaMinimization minimization = new DkaMinimization(automat);
        minimization.minimize();
    }

    public static void toDkaTest() {
        EpsilonNka automat = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
        automat.addStates(new String[]{"q1","q2","q4","q3","q5"});
        automat.addTransition(Pair.makePair("q0", 'b'), "q0");
        automat.addTransition(Pair.makePair("q0", 'a'), "q1");
        automat.addTransition(Pair.makePair("q1", 'b'), "q1");
        automat.addTransition(Pair.makePair("q1", EpsilonNka.EPSILON), new String[]{"q2", "q4"});
        automat.addTransition(Pair.makePair("q2", 'a'), "q2");
        automat.addTransition(Pair.makePair("q2", 'b'), "q5");
        automat.addTransition(Pair.makePair("q4", 'b'), "q3");
        automat.addTransition(Pair.makePair("q4", 'a'), "q5");
        automat.addTransition(Pair.makePair("q3", 'a'), "q4");
        automat.addFinalState("q5");
        Dka dkaAutomat;

        dkaAutomat = automat.toDka();
        System.out.println(dkaAutomat.checkString("bba"));

    }

    public static void regexTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a*b*|bb");
        /*RegularToPostfixConvertor convertor=new RegularToPostfixConvertor(regex);
        System.out.println(convertor.convertToPostfix());*/
        try {
            System.out.println(regex.checkString("aaa"));
            EpsilonNka result = regex.toENka();
            System.out.println(result.checkString("aaa"));
            Dka result2 = result.toDka();
            System.out.println(result2.checkString("aaa"));
        } catch (ObjectNotCompletedException e) {
            System.out.println(e.getMessage());
        }
        Dka result2 = regex.toDka();
        System.out.println(result2.languageEquals(regex.toDka()));
    }

    public static void languageEqualsTest()
    {
        RegularExpression regex=new RegularExpression(new Character[]{'a','b'});
        //regex.setRegex("a*.a.b.a.a*");
        regex.setRegex("a*.a.b.a.b*|b.b");
        Dka result2 = regex.toDka();
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        RegularExpression test=new RegularExpression(new Character[]{'a','b'});
        test.setRegex("a.a");
        /*Dka temp=regex.toDka();
        DkaMinimization minimez=new DkaMinimization(temp);
        minimez.minimize();*/
        System.out.println(result2.languageEquals(regex.toDka()));
    }
}
