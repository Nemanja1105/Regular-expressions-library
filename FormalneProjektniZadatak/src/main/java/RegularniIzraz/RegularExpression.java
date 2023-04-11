package RegularniIzraz;

import Automati.EpsilonNka;
import Automati.Dka;
import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.ObjectNotCompletedException;
import PomocneStrukturePodataka.Pair;

import java.util.Random;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashSet;

public class RegularExpression implements IRegularLanguage {
    private HashSet<Character> alphabet;
    private String regex;

    //Konstante koje predstavljaju valjane operatore u regularnom izrazu
    protected static final char CONCATENATION = '.';
    protected static final char ALTERNATIVE = '|';
    protected static final char KLEEN_STAR = '*';

    private static final String ILLEGAL_ALPHABET_EXCEPTION = "Alfabet ne moze da bude prazan ili null";
    private static final String ILLEGAL_REGEX_EXCEPTION = "Regularni izraz ne moze da bude null";
    private static final String ALPHABET_NOT_SUPPORTED_EXCEPTION = "Dati regularni izraz sadrzi nepodrzane simbole";

    //Konstruktor koji inicijalizuje regularni izraz
    //odgovarajucim alfabetom
    public RegularExpression(Character[] alphabet) throws IllegalArgumentException {
        if (alphabet == null || alphabet.length <= 0)
            throw new IllegalArgumentException(ILLEGAL_ALPHABET_EXCEPTION);
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
    }

    public RegularExpression(HashSet<Character> alphabet) throws IllegalArgumentException {
        if (alphabet == null || alphabet.size() <= 0)
            throw new IllegalArgumentException(ILLEGAL_ALPHABET_EXCEPTION);
        this.alphabet = (HashSet<Character>) alphabet.clone();
    }


    //Funkcija za setovanje regex-a
    //Ako regex sadrzi simbol koji nije iz alfabeta ili sadrzi
    // operaciju koja nije definisana baca se izuzetak
    //Slozenost:O(size(regex))
    public void setRegex(String regex) throws IllegalArgumentException {
        if (regex == null)
            throw new IllegalArgumentException(ILLEGAL_REGEX_EXCEPTION);
        for (var symbol : regex.toCharArray())
            if (!this.alphabet.contains(symbol) && symbol != CONCATENATION && symbol != ALTERNATIVE && symbol != KLEEN_STAR && symbol != '(' && symbol != ')')
                throw new IllegalArgumentException(ALPHABET_NOT_SUPPORTED_EXCEPTION);
        //this.regex = regex.replaceAll("([a-z]|[*])([a-z])","$1.$2");;
        this.regex = regex;
    }

    //Geter koji vraca regex koji reprezentuje regularni izraz
    //Slozenost:O(1)
    public String getRegex() {
        return this.regex;
    }

    //Funkcija koja ispituje pripadnost rijeci jeziku
    //Na pocetku provjeravamo da li je regularni izraz u potpusnosti kreiran
    //Nasa reprezentacija regex-a sadrzi tacku na mjestu konkatenacije
    //Za provjeru pripadnosti rijeci regexu koristimo funkciju matches koje je ugradjena u java api
    //pa nase tacke moramo prominiti sa prazninama
    ////Slozenost:O(1)
    public boolean checkString(String str) throws ObjectNotCompletedException {
        if (this.regex == null)
            throw new ObjectNotCompletedException("Regularni izraz nije u potpunosti kreiran");
        String reg = this.regex.replaceAll("[.]", "");
        return str.matches(reg);
    }

    //Pomocna funkcija koja imenuje stanje
    //Radi smanjivanje pojave kolicije prilikom operacija
    //sa regularnim jezicima prefiks se generise slucajno
    private String nameState(int num, char c) {
        return c + Integer.toString(num);
    }

    //Funkcija obavlja konverzija regularnog izraza u enka
    //Predstavlja implementaciju funkcije iz IRegularLanguage interfejsa
    //Regex se pretvara u postfiksni oblik nakon cega se vrsi evaluacija izraza
    //Vrsimo skeniranje postfiksnog izraza i ukoliko naidjemo na simbol
    //kreiramo dva nova stanja i dodajemo tranziciju (state1,symbol)->state2
    //nakon cega na stek stavljamo par pocetno-finalno stanje
    //Ako je simbol . vrsi se konkatenacija, sa steka se skidaju dva para
    //i dodajemo tranziciju koja povezuje finalno prvog sa pocetnim drugim
    //na stek vracamo novi par koji sadrzi novo pocetno i novo finalno
    //Za preostale operacije postupak je slican samo se dodaju drugacije tranzicije
    //Poslije skeniranje postfiksnog izraza na steku treba da se nalazi jedan par
    //i dodajemo pocetno i finalno stanje u enka koji predstavljaju elemente para
    //Slozenost(size(regex))
    @Override
    public EpsilonNka toENka() {
        Random r = new Random();
        char c = (char) (r.nextInt() % 26 + 'a');
        RegularToPostfixConvertor convertor = new RegularToPostfixConvertor(this);
        String postfix = convertor.convertToPostfix();
        Character[] temp = new Character[this.alphabet.size()];
        this.alphabet.toArray(temp);
        EpsilonNka automat = new EpsilonNka(temp);
        Stack<Pair<String, String>> stack = new Stack<>();//pocetno-finalno
        int stateNameCounter = 1;
        for (var symbol : postfix.toCharArray()) {//size(regex)
            if (Character.isAlphabetic(symbol) || Character.isDigit(symbol)) {
                String state1 = this.nameState(stateNameCounter++, c);
                String state2 = this.nameState(stateNameCounter++, c);
                automat.addStates(new String[]{state1, state2});
                automat.addTransition(Pair.makePair(state1, symbol), state2);
                stack.push(Pair.makePair(state1, state2));
            } else {
                switch (symbol) {
                    case CONCATENATION:
                        var operand2 = stack.pop();
                        var operand1 = stack.pop();
                        automat.addTransition(Pair.makePair(operand1.second, EpsilonNka.EPSILON), operand2.first);
                        stack.push(Pair.makePair(operand1.first, operand2.second));
                        break;
                    case ALTERNATIVE:
                        operand2 = stack.pop();
                        operand1 = stack.pop();
                        String state1 = this.nameState(stateNameCounter++, c);
                        String state2 = this.nameState(stateNameCounter++, c);
                        automat.addStates(new String[]{state1, state2});
                        automat.addTransition(Pair.makePair(state1, EpsilonNka.EPSILON), operand1.first);
                        automat.addTransition(Pair.makePair(state1, EpsilonNka.EPSILON), operand2.first);
                        automat.addTransition(Pair.makePair(operand1.second, EpsilonNka.EPSILON), state2);
                        automat.addTransition(Pair.makePair(operand2.second, EpsilonNka.EPSILON), state2);
                        stack.push(Pair.makePair(state1, state2));
                        break;
                    case KLEEN_STAR:
                        var operand = stack.pop();
                        state1 = this.nameState(stateNameCounter++, c);
                        state2 = this.nameState(stateNameCounter++, c);
                        automat.addStates(new String[]{state1, state2});
                        automat.addTransition(Pair.makePair(operand.second, EpsilonNka.EPSILON), operand.first);
                        automat.addTransition(Pair.makePair(state1, EpsilonNka.EPSILON), operand.first);
                        automat.addTransition(Pair.makePair(state1, EpsilonNka.EPSILON), state2);
                        automat.addTransition(Pair.makePair(operand.second, EpsilonNka.EPSILON), state2);
                        stack.push(Pair.makePair(state1, state2));
                        break;
                }
            }
        }
        Pair<String, String> result = stack.pop();
        automat.setStartState(result.first);
        automat.addFinalState(result.second);
        return automat;
    }

    //Funkcija koja vrsi konverziju u dka
    //Prvo izvrsimo konverziju u enka nakon toga
    //iz enka u dka
    //Slozenost:O(toEnka)+O(toDka)
    @Override
    public Dka toDka() {
        return this.toENka().toDka();
    }

    //Predstavlja implementaciju metode iz IRegularLanguage interfejsa
    //Iako u IRegularLanguage postoji podrazumjevana implementacija unije
    //koja predstavlja konverziju u dka i pozivanje unije nad ekv dka
    //Umjesto toga radimo optimizaciju gdje kreiramo novi regularni izraz
    //koji je dobijen konkatenacijom stringova
    //Slozenost:O(A)-A alfabet za Regex
    @Override
    public IRegularLanguage union(IRegularLanguage other) throws IllegalArgumentException, DifferentAlphabetException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        if (!(other instanceof RegularExpression))
            return IRegularLanguage.super.union(other);
        RegularExpression otherRegex = (RegularExpression) other;
        if (!this.alphabet.equals(otherRegex.alphabet))//O(A)
            throw new DifferentAlphabetException();
        RegularExpression newRegex = new RegularExpression(this.alphabet);
        newRegex.setRegex(this.regex + ALTERNATIVE + otherRegex.regex);
        return newRegex;
    }

    //Predstavlja implementaciju metode iz IRegularLanguage interfejsa
    //Iako u IRegularLanguage postoji podrazumjevana implementacija konkatenacija
    //koja predstavlja konverziju u enka i pozivanje konkatenacije nad ekv enka
    //Umjesto toga radimo optimizaciju gdje kreiramo novi regularni izraz
    //koji je dobijen konkatenacijom stringova i umetanjem operator konkatenacije
    //Slozenost:O(A)-A alfabet za Regex
    @Override
    public IRegularLanguage concatenation(IRegularLanguage other) throws IllegalArgumentException, DifferentAlphabetException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        if (!(other instanceof RegularExpression))
            return IRegularLanguage.super.union(other);
        RegularExpression otherRegex = (RegularExpression) other;
        if (!this.alphabet.equals(otherRegex.alphabet))
            throw new DifferentAlphabetException();
        RegularExpression newRegex = new RegularExpression(this.alphabet);
        newRegex.setRegex(this.regex + CONCATENATION + otherRegex.regex);
        return newRegex;
        //return this.universalOperation(other,(s1,s2)->{return s1+"."+s2;});
    }

    //Predstavlja implementaciju metode iz IRegularLanguage interfejsa
    //Iako u IRegularLanguage postoji podrazumjevana implementacija klinove zvijezde
    //koja predstavlja konverziju u enka i pozivanje klinove zvijezde nad ekv enka
    //Umjesto toga radimo optimizaciju gdje kreiramo novi regularni izraz
    //koji je dobijen jednostavnim operacijama sa stringovima
    //Slozenost:O(1)
    @Override
    public IRegularLanguage kleeneStar() {
        RegularExpression newRegex = new RegularExpression(this.alphabet);
        newRegex.setRegex("(" + this.regex + ")" + KLEEN_STAR);
        return newRegex;
    }


}
