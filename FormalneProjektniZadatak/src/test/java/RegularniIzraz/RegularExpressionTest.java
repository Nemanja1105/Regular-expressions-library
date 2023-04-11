package RegularniIzraz;

import Automati.Dka;
import Automati.EpsilonNka;
import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.ObjectNotCompletedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class RegularExpressionTest {

    @Test
    public void regularExpressionSetRegexTest() {
        RegularExpression regularExpression1 = new RegularExpression(new Character[]{'a', 'b'});
        assertThrows(IllegalArgumentException.class, () -> {
            regularExpression1.setRegex("c.d*");
        });
        RegularExpression regularExpression2 = new RegularExpression(new Character[]{'a', 'b'});
        assertThrows(IllegalArgumentException.class, () -> {
            regularExpression2.setRegex("a.b.[a-b]+");
        });
    }

    @Test
    public void regularExpressionCheckStringTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("(a*.a.b).a.b*|(b.a*)");
        try {
            assertEquals(true, regex.checkString("aabab"));
            assertEquals(true, regex.checkString("baaaaa"));
        } catch (ObjectNotCompletedException e) {

        }
    }

    @Test
    public void regularExpressionToENkaTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a*.a.b.a.b*|b.a*");
        EpsilonNka enka = regex.toENka();
        try {
            assertEquals(true, regex.checkString("aabab"));
            assertEquals(true, enka.checkString("aabab"));
        } catch (ObjectNotCompletedException e) {

        }
    }

    @Test
    public void regularExpressionToDkaTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a*.a.b.a.b*|b.a*");
        Dka dka = regex.toDka();
        try {
            assertEquals(true, regex.checkString("aabab"));
            assertEquals(true, dka.checkString("aabab"));
        } catch (ObjectNotCompletedException e) {

        }
    }

    @Test
    public void regularExpressionUnionTest() {
        RegularExpression regex1 = new RegularExpression(new Character[]{'a', 'b'});
        regex1.setRegex("a*.a.b.a.b*");
        RegularExpression regex2 = new RegularExpression(new Character[]{'a', 'b'});
        regex2.setRegex("a.a.a");
        try {
            IRegularLanguage result = regex1.union(regex2);
            assertEquals(true, result.checkString("aabab"));
            assertEquals(true, result.checkString("aaa"));
        } catch (ObjectNotCompletedException | DifferentAlphabetException e) {

        }
    }

    @Test
    public void regularExpressionConcatenationTest() {
        RegularExpression regex1 = new RegularExpression(new Character[]{'a', 'b'});
        regex1.setRegex("a*.a.b.a.b*");
        RegularExpression regex2 = new RegularExpression(new Character[]{'a', 'b'});
        regex2.setRegex("a.a.a");
        try {
            IRegularLanguage result = regex1.concatenation(regex2);
            assertEquals(true, result.checkString("abaaaa"));

        } catch (ObjectNotCompletedException | DifferentAlphabetException e) {

        }
    }

    @Test
    public void regularExpressionKleeneStarTest() {
        RegularExpression regex2 = new RegularExpression(new Character[]{'a', 'b'});
        regex2.setRegex("a.b.a");
        IRegularLanguage result = regex2.kleeneStar();
        try {
            assertEquals(true, result.checkString("abaabaaba"));
        } catch (ObjectNotCompletedException e) {

        }
    }
}