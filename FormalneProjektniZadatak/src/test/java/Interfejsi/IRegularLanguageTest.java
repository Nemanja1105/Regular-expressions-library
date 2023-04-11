package Interfejsi;

import Automati.Dka;
import Automati.EpsilonNka;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.ObjectNotCompletedException;
import PomocneStrukturePodataka.Pair;
import RegularniIzraz.RegularExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IRegularLanguageTest {

    @Test
    public void IRegularLanguageChainingOperationTest1() {
        RegularExpression r1 = new RegularExpression(new Character[]{'a', 'b'});
        RegularExpression r2 = new RegularExpression(new Character[]{'a', 'b'});
        RegularExpression r3 = new RegularExpression(new Character[]{'a', 'b'});
        r3.setRegex("a*");
        r1.setRegex("b*.a");
        r2.setRegex("a.b.a");
        try {
            IRegularLanguage testReg = r1.union(r2).kleeneStar().concatenation(r3).toDka();
            assertEquals(true, testReg.checkString("bbabbbabbbbaaaa"));
        } catch (DifferentAlphabetException | ObjectNotCompletedException e) {

        }
    }

    @Test
    public void IRegularLanguageChainingOperationTest2() {
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        RegularExpression regex = new RegularExpression(new Character[]{'0', '1'});
        regex.setRegex("1.1.1.1|1.1");
        RegularExpression regex2 = new RegularExpression(new Character[]{'0', '1'});
        regex2.setRegex("1.0*.1");
        try {
            EpsilonNka result = automat.complement().difference(regex).union(regex2).toENka();
            assertEquals(false, result.checkString("1111"));
            assertEquals(true, result.checkString("110110011"));
            assertEquals(true, result.checkString("100000001"));
        } catch (DifferentAlphabetException e) {

        }
    }

}