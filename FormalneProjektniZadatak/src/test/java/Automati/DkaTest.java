package Automati;

import Izuzeci.DifferentAlphabetException;
import Izuzeci.LanguageNotFinallyException;
import PomocneStrukturePodataka.Pair;
import RegularniIzraz.RegularExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class DkaTest {

    private Dka testResources1() {
        //automat koji prihvata rijeci koji pocinju sa 0
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addStates("q2");
        automat.addTransition(Pair.makePair("q0", '0'), "q1");
        automat.addTransition(Pair.makePair("q0", '1'), "q2");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q1");
        automat.addTransition(Pair.makePair("q2", '0'), "q2");
        automat.addTransition(Pair.makePair("q2", '1'), "q2");
        automat.addFinalState("q1");
        return automat;
    }

    private Dka testResources2() {
        //automat koji prihvata rijeci koje imaju neparan broj jedinica
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        return automat;
    }

    private Dka testResources3() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a.b.a|a*.b");
        return regex.toDka();
    }

    private Dka testResources4() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a.b.a|a.a.b.b");
        return regex.toDka();
    }

    @Test
    public void dkaAddStatesTest() {
        Dka mashine = new Dka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addStates("q1");
        });
    }

    @Test
    public void dkaAddFinalStatesTest() {
        Dka mashine = new Dka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        mashine.addFinalState("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addFinalState("q5");
        });
    }

    @Test
    public void dkaAddTransitionUnsupportedAlphabetTest() {
        Dka mashine = new Dka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addTransition(Pair.makePair("q0", 'a'), "q1");
        });
    }

    @Test
    public void dkaAddTransitionNonExistingState() {
        Dka mashine = new Dka(new Character[]{'0', '1'}, "q0");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addTransition(Pair.makePair("q0", 'a'), "q1");
        });
    }

    @Test
    public void dkaCheckStringTest() {
        //automat koji prihvata rijeci koje imaju neparan broj jedinica
        Dka automat = this.testResources2();
        assertEquals(true, automat.checkString("11100011"));
    }

    @Test
    public void dkaCheckStringUnsupportedAlphabetTest() {
        Dka automat = this.testResources2();
        assertThrows(IllegalArgumentException.class, () -> {
            automat.checkString("adasda32");
        });
    }

    @Test
    public void dkaToEnkaTest() {
        Dka automat = this.testResources2();
        EpsilonNka enka = automat.toENka();
        assertEquals(true, enka.checkString("11100011"));
    }

    @Test
    public void dkaLengthOfMinimalWordTest() {
        //automat koji prihvata rijeci koji pocinju sa 0
        Dka automat = this.testResources1();
        assertEquals(1, automat.lengthOfMinimalWord());

    }

    @Test
    public void dkaLanguageEqualsTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a*.a.b.a.b*|b.b");
        Dka automat = this.testResources2();
        assertEquals(false, automat.languageEquals(regex.toDka()));
        assertEquals(true, automat.languageEquals(automat));
    }

    @Test
    public void dkaUniversalOperationDifferentAlphabetTest() {
        Dka automat1 = new Dka(new Character[]{'a', 'b'}, "q0");
        Dka automat2 = new Dka(new Character[]{'a', '1'}, "q1");
        assertThrows(DifferentAlphabetException.class, () -> {
            automat1.union(automat2);
        });
    }

    @Test
    public void dkaUnionTest() {
        //rijeci koje pocinju sa 0
        Dka automat1 = this.testResources1();
        //rijeci koje imaju neparan broj jedinica
        Dka automat2 = this.testResources2();
        try {
            Dka union = automat1.union(automat2);
            assertEquals(true, union.checkString("01111"));
            assertEquals(true, union.checkString("111011"));
        } catch (DifferentAlphabetException e) {

        }
    }

    @Test
    public void dkaIntersectionTest() {
        Dka automat1 = this.testResources3();
        Dka automat2 = this.testResources4();
        try {
            Dka intersection = automat1.intersection(automat2);
            assertEquals(true, intersection.checkString("aba"));

        } catch (DifferentAlphabetException e) {

        }
    }

    @Test
    public void dkaDifferenceTest() {
        Dka automat1 = this.testResources3();
        Dka automat2 = this.testResources4();
        try {
            Dka intersection = automat1.difference(automat2);
            assertEquals(true, intersection.checkString("aaaab"));

        } catch (DifferentAlphabetException e) {

        }
    }

    @Test
    public void dkaComplementTest() {
        //neparan broj jedinica
        Dka automat = this.testResources2();
        Dka result = automat.complement();
        assertEquals(true, result.checkString("11011"));
    }

    @Test
    public void dkaLenghtOfLongestWordTest() {
        Dka automat = this.testResources4();
        try {
            assertEquals(4, automat.lenghtOfLongestWord());
        } catch (LanguageNotFinallyException e) {

        }
    }

    @Test
    public void dkaFinalityOfLanguageTest() {
        //neparan broj jedinica
        Dka automat = this.testResources2();
        assertEquals(false, automat.finalityOfLanguage());
    }

    @Test
    public void dkaRemoveUnreachableStatesTest() {
        //neparan broj jedinica
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addStates("q2");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addTransition(Pair.makePair("q2", '0'), "q1");
        automat.addTransition(Pair.makePair("q2", '1'), "q1");
        automat.addFinalState("q1");
        assertEquals(true, automat.checkString("1101"));
        DkaMinimization minimization = new DkaMinimization(automat);
        minimization.removeUnreachableStates();
        assertEquals(2, automat.getNumberOfStates());
        assertEquals(true, automat.checkString("1101"));
    }

    @Test
    public void dkaMinimizationTest() {
        Dka automat = new Dka(new Character[]{'a', 'b'}, "q0");
        automat.addStates(new String[]{"q1", "q6", "q2", "q4", "q7", "q5", "q3", "q8", "q9", "q10"});
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
        assertEquals(true, automat.checkString("bbb"));
        DkaMinimization minimization = new DkaMinimization(automat);
        minimization.minimize();
        assertEquals(true, automat.checkString("bbb"));
    }


}