package Automati;

import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.ObjectNotCompletedException;
import PomocneStrukturePodataka.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpsilonNkaTest {

    private EpsilonNka resources1() {
        EpsilonNka automat = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
        automat.addStates(new String[]{"q1", "q2", "q3", "q4"});
        automat.addTransition(Pair.makePair("q0", 'a'), "q0");
        automat.addTransition(Pair.makePair("q0", EpsilonNka.EPSILON), "q1");
        automat.addTransition(Pair.makePair("q1", 'b'), new String[]{"q4", "q2"});
        automat.addTransition(Pair.makePair("q1", EpsilonNka.EPSILON), "q0");
        automat.addTransition(Pair.makePair("q4", 'a'), "q1");
        automat.addTransition(Pair.makePair("q2", 'a'), new String[]{"q2", "q3"});
        automat.addTransition(Pair.makePair("q3", 'b'), "q3");
        automat.addTransition(Pair.makePair("q3", EpsilonNka.EPSILON), "q1");
        automat.addFinalState("q1");
        return automat;
    }

    private EpsilonNka resources2() {
        //abab
        EpsilonNka automat = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
        automat.addStates(new String[]{"q1", "q2", "q4", "q3", "q5"});
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
        return automat;
    }

    private EpsilonNka resources3() {
        //aba(a|b)*
        EpsilonNka automat = new EpsilonNka(new Character[]{'a', 'b'}, "q0");
        automat.addStates(new String[]{"q1", "q2", "q3"});
        automat.addTransition(Pair.makePair("q0", 'a'), "q1");
        automat.addTransition(Pair.makePair("q1", 'b'), "q2");
        automat.addTransition(Pair.makePair("q2", 'a'), "q3");
        automat.addTransition(Pair.makePair("q3", 'a'), "q3");
        automat.addTransition(Pair.makePair("q3", 'b'), "q3");
        automat.addFinalState("q3");
        return automat;
    }

    @Test
    public void eNkaAddStatesTest() {
        EpsilonNka mashine = new EpsilonNka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addStates("q1");
        });
    }

    @Test
    public void eNkaAddFinalStatesTest() {
        EpsilonNka mashine = new EpsilonNka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        mashine.addFinalState("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addFinalState("q5");
        });
    }

    @Test
    public void eNkaAddTransitionUnsupportedAlphabetTest() {
        EpsilonNka mashine = new EpsilonNka(new Character[]{'0', '1'}, "q0");
        mashine.addStates("q1");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addTransition(Pair.makePair("q0", 'a'), "q1");
        });
    }

    @Test
    public void eNkaAddTransitionNonExistingState() {
        EpsilonNka mashine = new EpsilonNka(new Character[]{'0', '1'}, "q0");
        assertThrows(IllegalArgumentException.class, () -> {
            mashine.addTransition(Pair.makePair("q0", 'a'), "q1");
        });
    }

    @Test
    public void eNkaClosureTest() {
        EpsilonNka automat = this.resources1();
        var list = automat.closure("q0");
        var iter = list.iterator();
        assertEquals("q1", iter.next());
        assertEquals("q0", iter.next());
    }

    @Test
    public void eNkaCheckStringTest() {
        EpsilonNka automat = this.resources2();
        assertEquals(true, automat.checkString("abab"));
        assertEquals(false, automat.checkString(Character.toString(EpsilonNka.EPSILON)));
    }

    @Test
    public void eNkaToDkaTest() {
        EpsilonNka automat = this.resources2();
        Dka dka = automat.toDka();
        assertEquals(true, automat.checkString("abab"));
        assertEquals(true, dka.checkString("abab"));
    }

    @Test
    public void eNkaLengthOfMinimalWordTest() {
        EpsilonNka automat = this.resources2();
        assertEquals(2, automat.lengthOfMinimalWord());
    }

    @Test
    public void eNkaConcatenationTest() {
        EpsilonNka automat1 = this.resources1();
        EpsilonNka automat2 = this.resources2();
        try {
            IRegularLanguage result = automat1.concatenation(automat2);
            assertEquals(true, result.checkString("abab"));

        } catch (DifferentAlphabetException | ObjectNotCompletedException e) {

        }
    }

    @Test
    public void eNkaKleeneStarTest() {
        EpsilonNka automat = this.resources3();
        IRegularLanguage result = automat.kleeneStar();
        try {
            assertEquals(true, result.checkString("abaabaababa"));
        } catch (ObjectNotCompletedException e) {

        }
    }

    @Test
    public void eNkaComplementTest() {
        EpsilonNka automat = this.resources3();
        IRegularLanguage result = automat.complement();
        try {
            assertEquals(false, result.checkString("abaabaababa"));
        } catch (ObjectNotCompletedException e) {

        }
    }


}