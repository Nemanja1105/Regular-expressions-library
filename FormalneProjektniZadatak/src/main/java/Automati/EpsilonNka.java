package Automati;

import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import PomocneStrukturePodataka.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class EpsilonNka implements IRegularLanguage {
    public static final char EPSILON = 'ε';
    protected HashSet<Character> alphabet;
    protected HashSet<String> finalStates;
    protected HashSet<String> states;
    protected HashMap<Pair<String, Character>, HashSet<String>> transition;
    protected String startState;
    private static final java.lang.String ARGUMENT_STATE_NULL_EXCEPTION = "Stanje ne moze da bude null!!";
    private static final java.lang.String ALPHABET_NOT_SUPPORTED = "Data tranzicija sadrzi ne podrzani simbol alfabeta";
    private static final java.lang.String FINAL_STATE_NOT_CONTAIN = "Dato stanje ne postoji vec u automatu";
    private static final String STATE_NOT_CONTAIN = "Stanje ne postoji!!";

    //Konstruktori za pocetnu inicijalizaciju enka
    //Konstruktori su preklopljeni tako da mozemo enka
    //instancirati na vise nacina u zavisnosti od potreba
    public EpsilonNka(Character[] alphabet) {
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.alphabet.add(EPSILON);
        this.finalStates = new HashSet<>();
        this.transition = new HashMap<>();
        this.states = new HashSet<>();
    }

    public EpsilonNka(HashSet<Character> alphabet) {
        this.alphabet = alphabet;
        this.alphabet.add(EPSILON);
        this.finalStates = new HashSet<>();
        this.transition = new HashMap<>();
        this.states = new HashSet<>();
    }

    public EpsilonNka(Character[] alphabet, String startState) {
        this(alphabet);
        this.setStartState(startState);
    }

    //Funkcija za setovanje pocetnog stanja
    //Pocetno stanje se dodaje u skup stanja automata
    //Ako je proslijedjeno null kao pocetno stanja
    //baca se izuzetak kojeg korisnik biblioteke treba obraditi
    //Slozenost:0(1)
    public void setStartState(String startState) throws IllegalArgumentException {
        if (startState == null)
            throw new IllegalArgumentException(ARGUMENT_STATE_NULL_EXCEPTION);
        this.startState = startState;
        this.states.add(startState);
    }

    //Geter koji vraca pocetno stanja automata
    //Slozenost:0(1)
    public String getStartState() {
        return this.startState;
    }

    //Funckija za dodavanje nove tranzicije(stanje,simbol)->skup stanja
    //Tranzicije se cuvaju u mapu gdje je kljuc par(stanje,simbol), a vrijednost skup stanja
    //Funkcija baca izuzetak ako se dodaje tranzicija koja sadrzi nepodrzani simbol
    //Takodje funkcija baca izuzetak ako se dodaje tranzicija koja sadrzi stanje koje
    //se ne nalazi u skupu stanja automata(stanja mora biti prethodno dodano u automat metodom addState).
    //Slozenost:O(1)
    public void addTransition(Pair<String, Character> oldState, String newState) throws IllegalArgumentException {
        if (!this.alphabet.contains(oldState.second))
            throw new IllegalArgumentException(ALPHABET_NOT_SUPPORTED);
        if (!this.states.contains(oldState.first) || !this.states.contains(newState))
            throw new IllegalArgumentException(STATE_NOT_CONTAIN);
        if (this.transition.containsKey(oldState))
            this.transition.get(oldState).add(newState);
        else {
            this.transition.put(oldState, new HashSet<>());
            this.transition.get(oldState).add(newState);
        }
        //this.states.add(newState);
    }

    //Funkcija koja dodaje novo stanje u skup stanja automata
    //Ako stanje vec postoji u skupu stanja baca se odgovarajuci izuzetak
    //Slozenost:0(1)
    public void addStates(String state) throws IllegalArgumentException {
        if (this.states.contains(state) && !state.equals(this.startState))
            throw new IllegalArgumentException(Dka.STATE_ALREDY_CONTAINS_EXCEPTION);
        this.states.add(state);
    }

    //Funkcija koja dodaje niz novih stanja u skup stanja automata
    //Ako se neko od stanja vec nalazi u skupu stanja baca se izuzetak
    //i prekida se dalje dodavanje stanja
    //Slozenost:0(size(states))
    public void addStates(String[] states) throws IllegalArgumentException {
        for (var state : states)
            this.addStates(state);
    }

    //Funkcija u kojoj se za jedno stanje i jedan simbol prelazi u skup stanja
    //Slozenost:0(size(newState))
    public void addTransition(Pair<String, Character> oldState, String[] newStates) {
        for (var el : newStates)
            this.addTransition(oldState, el);
    }

    //Funkcija koja dodaje stanje u skup finalnih stanja
    //Ako stanje koje je zadano kao argument ne postoji u skupu stanja baca se izuzetak
    //Slozenost:0(1)
    public void addFinalState(String state) throws IllegalArgumentException {
        if (!this.states.contains(state))
            throw new IllegalArgumentException(FINAL_STATE_NOT_CONTAIN);
        this.finalStates.add(state);
    }

    //Geter koji vraca broj stanja automata
    //Slozenost:0(1)
    public int getNumberOfStates() {
        return this.states.size();
    }

    //Funkcija koja dodaje niz stanja u skup finalnih stanja
    //Ako se neko od stanja ne nalazi u skupu stanja baca se izuzetak
    //i prekida se dalje dodavanje finalnih stanja
    //Slozenost:0(size(states))
    public void addFinalState(String[] states) throws IllegalArgumentException {
        for (var el : states)
            this.addFinalState(el);
    }

    //Funkcija koje za neko stanje vraca closure za to stanje
    //Za realizaciju se koristi modifikacija bfs algoritma
    //U skup closure se dodaju sva stanja do kojih mozemo doci od pocetnog
    //upotrebom samo epsilon prelaza
    ////Slozenost:0(S^2)
    public HashSet<String> closure(String state) {
        HashSet<String> closureStates = new HashSet<>();
        Queue<String> states = new LinkedList<String>();
        states.add(state);
        while (!states.isEmpty()) //najgori slucaj sva stanja su povezana sa e
        {
            String temp = states.remove();
            closureStates.add(temp);
            var rez = this.transition.get(Pair.makePair(temp, EPSILON));
            if (rez != null) {
                rez.removeAll(closureStates);//O(size(rez))
                states.addAll(rez);//0(size(rez))
            }
        }
        return closureStates;
    }

    //Funkcija koja nam vraca closure skupa stanja
    //Funkcija koristi funkcionalnosti closure funkcije koja prima jedno stanje
    ////Slozenost:0(size(States)*S^2)
    private HashSet<String> closure(HashSet<String> states) {
        HashSet<String> result = new HashSet<>();
        for (var el : states)
            result.addAll(this.closure(el));
        return result;
    }

    //Funkcija ispituje pripadnost rijeci jeziku
    //koji je reprezentovan e nedeterministickim konacnim automatom
    //U slucaju da ulazni rijeci sadrzi simbol koji nije iz alfabeta baca se izuzetak
    //Na pocetku algoritma pronalazimo closure od pocetnog stanja
    //Nakon cega prolazimo kroz ulaznu rijec
    //U svakoj iteraciji pronalazimo skup stanja na osnovu trenutnog simbola
    //i skupa trenutnog stanja(u novo trenutno stanje dodajemo stanja koja dobijamo delta funkcijom)
    //Na kraju algoritma napravimo presjek skupa trenutnih stanja sa skupom finalnih stanja
    //ako presjek nije prazan prihvatamo rijec
    ////Slozenost:0(size(str)*S^3)
    public boolean checkString(java.lang.String str) throws IllegalArgumentException {
        HashSet<String> currentStates = this.closure(this.startState);//S^2
        for (var symbol : str.toCharArray()) {//size(str)
            if (!this.alphabet.contains(symbol))
                throw new IllegalArgumentException(ALPHABET_NOT_SUPPORTED);
            HashSet<String> temp = new HashSet<>();
            for (var state : currentStates) {
                HashSet<String> tempStates = this.transition.get(Pair.makePair(state, symbol));
                if (tempStates != null)
                    temp.addAll(tempStates);
            }
            currentStates = this.closure(temp);
        }
        currentStates.retainAll(this.finalStates);
        return currentStates.size() > 0;
    }

    //Funkcija za konverziju enka u dka
    //Predstavlja implementaciju metode iz IRegularLanguage interfejsa
    //Na pocetku algoritma pronalazimo closure od pocetnog tranja
    //i od datog skupa kreiramo novo pocetno stanje i vrsimo pocetnu inicijalizaciju dka
    //Za realizaciju algoritma koristimo modifikaciju bfs algoritma pri cemu pamtimo
    //skup stanja i njihovo ime. Na osnovu funkcije getStateBySymbol dobijamo skup stanja
    //u koja prelazimo za dati simbol. Ako je skup prazan dodaje se tranzicija
    // (trenutno stanje,simbol)->deadstate, inace dodajemo tranziciju na osnovu imena skupa stanja
    //Ako novi skup stanja nije posjecen dodajemo ga u red za posjetu
    //Postupak se ponavlja dok god imamo prelaze u nove skupove stanja
    //Svaki skup stanja u ekvivalentnom dka predstavlja jedno stanje
    //Slozenost:0(S^3*A)
    @Override
    public Dka toDka() //throws ObjectNotCompletedException
    {
        //konstanta za dead state
        final String deadState = "DEAD_STATE";
        //novo pocetno stanje
        String newStartState = "";
        HashSet<String> startStates = closure(this.startState);
        newStartState = this.getStateName(startStates);
        //inicijalizacija
        HashSet<String> dkaStates = new HashSet<>();
        HashSet<String> dkaFinalStates = new HashSet<>();
        HashMap<Pair<String, Character>, String> dfaTransition = new HashMap<>();
        HashSet<String> temp = finalStatesIntersection(startStates);
        if (temp.size() > 0)
            dkaFinalStates.add(startState);
        dkaStates.add(newStartState);
        //bfs algoritam
        Queue<Pair<HashSet<String>, String>> toVisit = new LinkedList<>();
        toVisit.add(Pair.makePair(startStates, newStartState));
        var alpha = new HashSet<>(this.alphabet);
        alpha.remove(EPSILON);
        while (!toVisit.isEmpty()) {
            var states = toVisit.remove();
            for (var symbol : alpha) {
                HashSet<String> newStates = this.getStateBySymbol(states.first, symbol);
                String stateName = "";
                if (newStates.size() == 0) {
                    stateName = deadState;
                    dkaStates.add(stateName);
                } else {
                    stateName = getStateName(newStates);
                    if (!dkaStates.contains(stateName)) {
                        dkaStates.add(stateName);
                        HashSet<String> tempFinal = finalStatesIntersection(newStates);
                        if (tempFinal.size() > 0)
                            dkaFinalStates.add(stateName);
                        toVisit.add(Pair.makePair(newStates, stateName));
                    }
                }
                dfaTransition.put(Pair.makePair(states.second, symbol), stateName);
            }
        }
        if (dkaStates.contains(deadState))
            for (var symbol : alpha)
                dfaTransition.put(Pair.makePair(deadState, symbol), deadState);
        return createDka(alpha, newStartState, dkaStates, dkaFinalStates, dfaTransition);
    }

    //Funkcija vraca rezultantni skup stanja koji se dobije
    //unijom skupova stanja dobijenih delta funkcijom za stanja iz pocetkog skupa i trenutnog simbola
    ////Slozenost:0(S^2)
    private HashSet<String> getStateBySymbol(HashSet<String> states, Character symbol) {
        HashSet<String> result = new HashSet<>();
        for (var state : states) {
            var temp = this.transition.get(Pair.makePair(state, symbol));
            if (temp != null)
                result.addAll(temp);
        }
        return this.closure(result);
    }

    //Pomocna funkcija za generisanje imena novog stanja
    //koje se dobija konkatenacijom imena stanja iz skupa stanja
    //Slozenost:0(1)
    private String getStateName(HashSet<String> states) {
        return String.join("-", states);
    }

    //Pomocna funkcija koja nam prava presjek skupa stanja kojeg smo proslijedili funkciji
    //i skupa finalnih stanja
    //O(size(states))
    private HashSet<String> finalStatesIntersection(HashSet<String> states) {
        HashSet<String> tempFinal = new HashSet<>(states);
        tempFinal.retainAll(this.finalStates);
        return tempFinal;
    }

    //Pomocna funkcija koja provjerava da li je objekat kreiran u potpunosti
    //Nije moguce koristiti mogucnosti biblioteke nad nepotpunim objektom
    //Slozenost:0(1)
    private boolean checkField() {
        return !(this.alphabet == null || this.startState == null || this.finalStates == null || this.transition == null || this.states == null);
    }

    //Pomocna funkcija koja na osnovu parametara koji su dobijene u procesu transformacije enka u dka
    //formira ekvivalentni dka
    //Slozenost:0(1)
    private Dka createDka(HashSet<Character> alphabet, String startState, HashSet<String> states, HashSet<String> finalStates, HashMap<Pair<String, Character>, String> dkaTransition) {
        Character[] alphaArray = new Character[alphabet.size()];
        alphabet.toArray(alphaArray);
        Dka result = new Dka(alphaArray, startState);
        result.transition = dkaTransition;
        result.finalStates = finalStates;
        result.states = states;
        return result;
    }

    //Funkcija vraca duzinu minimalne rijeci u jeziku koji je reprezentovan sa enka
    //Predstavlja implementacija interfejsa IRegularLanguage
    //Na pocetku se vrsi provijera da li je jezik konacan
    //Ako jezik jeste konacan, koristi se bfs algoritam za pronalazak
    //minimalne udaljenosti od pocetnog cvora
    //Nivo svakog cvora se pamti u mapi i nivo trenutnog cvora je jednak nivou
    //cvora preko kog smo dosli do trenutnog cvora + 1. Izuzetak je
    //ako smo do trenutnog cvora dosli upotrebom epsilon prelaza
    //tada je nivo novog cvora jednak nivou cvora preko kojeg smo dosli do cvora
    //Zaustavljamo se kada dodjemo do prvog finalnog stanja
    //Slozenost:0(S*A)
    @Override
    public int lengthOfMinimalWord() //throws LanguageNotFinallyException
    {
       /* if (!this.finalityOfLanguage())
            throw new LanguageNotFinallyException();*/
        Queue<String> toVisit = new LinkedList<>();
        HashMap<String, Integer> levels = new HashMap<>();
        toVisit.add(this.startState);
        levels.put(this.startState, 0);
        while (!toVisit.isEmpty()) {
            String state = toVisit.remove();
            for (var symbol : this.alphabet) {
                HashSet<String> newStates = this.transition.get(Pair.makePair(state, symbol));
                if (newStates != null) {
                    for (var stat : newStates) {
                        if (!levels.containsKey(stat)) {
                            toVisit.add(stat);
                            if (symbol == EPSILON)
                                levels.put(stat, levels.get(state));
                            else
                                levels.put(stat, levels.get(state) + 1);
                            if (this.finalStates.contains(stat))
                                return levels.get(stat);
                        }
                    }
                }
            }
        }
        return 0;
    }

    @Override
    //Predstavlja implementacija interfejsa IRegularLanguage
    //Kao rezultat vraca trenutni enka(nema potrebe za konverzijom
    //funkcija ce biti pozvana nad enka objektom);
    //Slozenost:0(1)
    public EpsilonNka toENka() {
        return this;
    }


    //Funkcija koja obavlja konkatenaciju dva regularna jezika
    //Predstavlja implementaciju funkcije iz IRegularLanguage interfejsa
    //Na pocetku vrsimo odgovarajucu konverziju regularnog jezika u enka
    //nakon cega provjeravamo da li oba jezika imaju isti alfabet.
    //Ako imaju nastavljamo dalje. U novi enka dodajemo sva stanja,finalna stanja
    //sve tranzicije iz enka kad kojima radimo konkatenaciju
    //Novo pocetno stanje je jednako pocetnom stanju enka nad kojim je pozvana funkcija
    //U skup tranzicija se dodaju epsilon prelazi od svih finalnih prvog automata ka pocetnom drugog automata
    //Slozenost:0(toEnka()) + O(S)+O(F) za enka priblizno O(S+F)
    @Override
    public IRegularLanguage concatenation(IRegularLanguage other) throws IllegalArgumentException, DifferentAlphabetException {
        if (other == null)
            throw new IllegalArgumentException(IRegularLanguage.ARGUMENT_NULL_EXCEPTION);
        EpsilonNka otherENka = (EpsilonNka) other.toENka();
        if (!this.alphabet.equals(otherENka.alphabet))
            throw new DifferentAlphabetException();

        EpsilonNka newENka = new EpsilonNka(this.alphabet);
        newENka.setStartState(this.startState);
        newENka.transition.putAll(this.transition);
        newENka.states.addAll(this.states);
        newENka.states.addAll(otherENka.states);
        newENka.finalStates.addAll(otherENka.finalStates);
        newENka.transition.putAll(otherENka.transition);
        for (var state : this.finalStates)
            newENka.addTransition(Pair.makePair(state, EPSILON), otherENka.startState);
        return newENka;
    }


    //Funkcija realizuje klinovu zvijezdu nad regularnim jezikom
    //Uvodimo novo pocetno i finalno stanje
    //U delta funkciju ubacujemo odgovarajuce tranzicije
    //tako da mozemo realizovati ovu operaciju
    ////Slozenost:0(S+F)
    @Override
    public IRegularLanguage kleeneStar() {
        final String startState = "START_STATE";
        final String finalState = "FINAL_STATE";
        EpsilonNka newENka = new EpsilonNka(this.alphabet);
        newENka.setStartState(startState);
        newENka.states.add(finalState);
        newENka.addFinalState(finalState);
        newENka.states.addAll(this.states);
        newENka.transition.putAll(this.transition);
        newENka.addTransition(Pair.makePair(startState, EPSILON), this.startState);
        newENka.addTransition(Pair.makePair(startState, EPSILON), finalState);
        for (var state : this.finalStates) {
            newENka.addTransition(Pair.makePair(state, EPSILON), finalState);
            newENka.addTransition(Pair.makePair(state, EPSILON), this.startState);
        }
        return newENka;
    }

    //Funkcija koja realizuje komplement jezika reprezentovanog sa enka
    //Funkcija kreira novi enka sa istim vrijednostima atributa
    //izuzev skupa finalnih stanja kojeg invertujemo
    //Mogli smo da koristimo podrazumjevano ponasanje
    //ali zbog perfomanski redefinisali smo metodu
    //Slozenost:O(S+F)
    @Override
    public EpsilonNka complement() {
        EpsilonNka newENka = new EpsilonNka(this.alphabet);
        newENka.setStartState(this.startState);
        newENka.transition = (HashMap<Pair<String, Character>, HashSet<String>>) this.transition.clone();
        newENka.states = (HashSet<String>) this.states.clone();
        HashSet<String> newFinalStates = new HashSet<>(this.states);
        newFinalStates.removeAll(this.finalStates);
        newENka.finalStates = newFinalStates;
        return newENka;
    }

/*
    private static String generateStateName(int counter) {
        return "p" + Integer.toString(counter);
    }
    //u drugim verzijama
    private static int renameStates(EpsilonNka newENka, EpsilonNka oldNka, int stateCounter, HashMap<String, String> names) {
        for (var pair : oldNka.transition.entrySet()) {
            String stateOld = pair.getKey().first;
            String newOldState = "";
            if (names.containsKey(stateOld))
                newOldState = names.get(stateOld);
            else
                newOldState = generateStateName(stateCounter++);
            for (var state : pair.getValue()) {
                String newState = "";
                if (names.containsKey(state))
                    newState = names.get(stateOld);
                else
                    newState = generateStateName(stateCounter++);
                newENka.addTransition(Pair.makePair(newOldState, pair.getKey().second), newState);
                if (oldNka.finalStates.contains(state))
                    newENka.addFinalState(newState);
            }
        }
        return stateCounter;
    }*/
}
