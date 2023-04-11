package SpecifikacijaRegularnogJezika;

import Automati.EpsilonNka;
import Interfejsi.IRegularLanguage;
import Automati.Dka;
import Izuzeci.ObjectNotCompletedException;
import PomocneStrukturePodataka.Pair;
import RegularniIzraz.RegularExpression;
import SpecifikacijaRegularnogJezika.Izuzeci.SyntaxErrorException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RegularLanguageCreator {
    private ArrayList<ArrayList<String>> tokens = new ArrayList<>();

    public RegularLanguageCreator(ArrayList<ArrayList<String>> tokens) {
        this.tokens = tokens;
    }

    public IRegularLanguage getRegularLanguage() throws IllegalArgumentException, SyntaxErrorException {
        IRegularLanguage language = null;
        if (tokens.get(0).get(0).equals("DKA"))
            language = getDka();
        else if (tokens.get(0).get(0).equals("ENKA"))
            language = getENka();
        else if (tokens.get(0).get(0).equals("REGEX"))
            language = getRegex();
        return language;
    }

    private HashSet<Character> getAlphabet(ArrayList<String> list) {
        HashSet<Character> alphabet = new HashSet<>();
        for (var ch : list)
            alphabet.add(ch.charAt(0));
        return alphabet;
    }

    private String[] getStringArray(ArrayList<String> list) {
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        return arr;
    }

    private Dka getDka() throws IllegalArgumentException, SyntaxErrorException {
        HashSet<Character> alphabet = getAlphabet(this.tokens.get(1));
        Dka automat = new Dka(alphabet);
        automat.addStates(this.getStringArray(this.tokens.get(2)));
        automat.setStartState(this.tokens.get(3).get(0));
        for (var transition : this.tokens.get(4)) {
            String[] args = transition.split("-");
            automat.addTransition(Pair.makePair(args[0], args[1].charAt(0)), args[2]);
        }
        automat.addFinalState(this.getStringArray(this.tokens.get(5)));
        if (this.tokens.size() > 6)
            this.checkWord(automat, this.tokens.get(6));
        return automat;
    }

    private void checkWord(IRegularLanguage automat, ArrayList<String> list) throws SyntaxErrorException {
        for (var str : list) {
            boolean status;
            try {
                status = automat.checkString(str);
            } catch (ObjectNotCompletedException e) {
                throw new SyntaxErrorException("Nije moguce kreirati automat od date specifikacije");
            }
            if (status)
                System.out.println("Rijec " + str + " pripada jeziku");
            else
                System.out.println("Rijec " + str + " ne pripada jeziku");
        }
    }

    private EpsilonNka getENka() throws IllegalArgumentException, SyntaxErrorException {
        HashSet<Character> alphabet = getAlphabet(this.tokens.get(1));
        EpsilonNka automat = new EpsilonNka(alphabet);
        automat.addStates(this.getStringArray(this.tokens.get(2)));
        automat.setStartState(this.tokens.get(3).get(0));
        automat.setStartState(this.tokens.get(3).get(0));
        for (var transition : this.tokens.get(4)) {
            String[] args = transition.split("-");
            String line = args[2].replaceAll("[()]", "");
            String[] newState = line.split("\\|");
            automat.addTransition(Pair.makePair(args[0], args[1].charAt(0)), newState);
        }
        automat.addFinalState(this.getStringArray(this.tokens.get(5)));
        if (this.tokens.size() > 6)
            this.checkWord(automat, this.tokens.get(6));
        return automat;
    }

    private RegularExpression getRegex() throws IllegalArgumentException, SyntaxErrorException {
        HashSet<Character> alphabet = getAlphabet(this.tokens.get(1));
        RegularExpression regex = new RegularExpression(alphabet);
        regex.setRegex(this.tokens.get(2).get(0));
        if (this.tokens.size() > 3) {
            this.checkWord(regex, this.tokens.get(3));
        }
        return regex;
    }
}
