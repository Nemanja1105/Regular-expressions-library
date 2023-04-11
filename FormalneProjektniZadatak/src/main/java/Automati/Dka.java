package Automati;

import Interfejsi.IRegularLanguage;
import Izuzeci.DifferentAlphabetException;
import Izuzeci.LanguageNotFinallyException;
import PomocneStrukturePodataka.*;
import Interfejsi.IMinimizable;

import java.util.*;

//S-broj stanja
//A-broj simbola u alfabetu
//F-broj finalnih stanja
//T-broj tranzicija
public class Dka implements IMinimizable, IRegularLanguage {
    protected HashSet<Character> alphabet;
    protected HashSet<String> finalStates;
    protected HashMap<Pair<String, Character>, String> transition;
    protected HashSet<String> states;
    protected String startState;
    protected static final java.lang.String ARGUMENT_STATE_NULL_EXCEPTION = "Stanje ne moze da bude null!!";
    protected static final java.lang.String STATE_NOT_CONTAIN = "Dato stanje ne postoji vec u automatu";
    protected static final java.lang.String ALPHABET_NOT_SUPPORTED = "Simbol ne posotoji u alfabetu!";
    protected static final String STATE_ALREDY_CONTAINS_EXCEPTION = "Stanje vec postoji!!";

    //Konstruktori za pocetnu inicijalizaciju dka,
    //konstruktori su preklopljeni tako da primaju
    //razlicite parametre u zavisnosti od potrebe korisnika biblioteke
    public Dka(Character[] alphabet, String startState) throws IllegalArgumentException {
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.finalStates = new HashSet<>();
        this.transition = new HashMap<>();
        this.states = new HashSet<>();
        this.setStartState(startState);
    }

    public Dka(HashSet<Character> alphabet, String startState) {
        this.alphabet = alphabet;
        this.finalStates = new HashSet<>();
        this.transition = new HashMap<>();
        this.states = new HashSet<>();
        this.setStartState(startState);
    }

    public Dka(HashSet<Character> alphabet) {
        this.alphabet = alphabet;
        this.finalStates = new HashSet<>();
        this.transition = new HashMap<>();
        this.states = new HashSet<>();
        this.startState = null;
    }

    //Funckija za dodavanje nove tranzicije(stanje,simbol)->stanje
    //Tranzicije se cuvaju u mapu gdje je kljuc par(stanje,simbol), a vrijednost stanje
    //Funkcija baca izuzetak ako se dodaje tranzicija koja sadrzi nepodrzani simbol
    //Takodje funkcija baca izuzetak ako se dodaje tranzicija koja sadrzi stanje koje
    //se ne nalazi u skupu stanja automata(stanja mora biti prethodno dodano u automat metodom addState).
    //Slozenost:O(1)
    public void addTransition(Pair<String, Character> oldState, String newState) throws IllegalArgumentException {
        if (!this.alphabet.contains(oldState.second))
            throw new IllegalArgumentException(ALPHABET_NOT_SUPPORTED);
        if (!this.states.contains(oldState.first) || !this.states.contains(newState))
            throw new IllegalArgumentException(STATE_NOT_CONTAIN);
        this.transition.put(oldState, newState);
        // this.states.add(newState);
    }

    //Funkcija za dodavanje novog stanja u skup stanja.
    //Ako stanje vec postoji u skupu stanja baca se izuzetak
    //koji informise korisnika o neuspjesnom dodavanju stanja
    //Slozenost:O(1)
    public void addStates(String state) throws IllegalArgumentException {
        if (this.states.contains(state) && !state.equals(this.startState))
            throw new IllegalArgumentException(STATE_ALREDY_CONTAINS_EXCEPTION);
        this.states.add(state);
    }

    //Funckija za dodavanje niza stanja u skup stanja
    //Ako se neko stanje vec nalazi u skupu stanja baca se izuzetak i prekida
    //se dalje dodavanje stanja
    //Slozenost:O(size(states))
    public void addStates(String[] states) throws IllegalArgumentException {
        for (var state : states)
            this.addStates(state);
    }

    //Funkcija za dodavanje finalnog stanja u skup finalnih stanja
    //Da bi stanje koje je proslijedjeno kao argument, bilo uspjesno dodano u skup finalnih stanja
    //stanje mora vec da postoji u skupu stanja automata
    //Slozenost:O(1)
    public void addFinalState(String state) throws IllegalArgumentException {
        if (!this.states.contains(state))
            throw new IllegalArgumentException(STATE_NOT_CONTAIN);
        this.finalStates.add(state);
    }

    //Funkcija za dodavanje niza finalnih stanja
    //Ako neko stanje se ne nalazi u skupu stanja automata
    //baca se izuzetak i prekida se dalje dodavanje stanja
    //Slozenost:O(size(states))
    public void addFinalState(String[] states) throws IllegalArgumentException {
        for (var el : states)
            this.addFinalState(el);
    }

    //Funckija vraca broj stanja automata
    //Slozenost:O(1)
    public int getNumberOfStates() {
        return this.states.size();
    }

    //Geter koji vraca alfabet automata
    //Slozenost:O(1)
    public HashSet<Character> getAlphabet() {
        return this.alphabet;
    }

    //Geter koji vraca skup finalnih stanja
    //Slozenost:O(1)
    public HashSet<String> getFinalStates() {
        return this.finalStates;
    }

    //Funkcija koja vraca odgovarajucu vrijednost iz skupa tranzicija
    //za dati par(stanje, karakter)
    //Slozenost:O(1)
    public String getTranstion(Pair<String, Character> old) {
        return this.transition.get(old);
    }

    //Geter koji vraca pocetno stanja
    //Slozenost:O(1)
    public String getStartState() {
        return this.startState;
    }

    //Geter koji vraca skup stanja automata
    //Slozenost:O(1)
    public HashSet<String> getStates() {
        return this.states;
    }

    //Funkcija za postavljanje pocetnog stanja
    //Ako se pocetno stanje ne nalazi u skupu stanja automata
    //dodaje se u skup stanja inace dodavanje u skup stanja se ignorise
    //Slozenost:O(1)
    public void setStartState(String startState) throws IllegalArgumentException {
        if (startState == null)
            throw new IllegalArgumentException(ARGUMENT_STATE_NULL_EXCEPTION);
        this.startState = startState;
        this.states.add(startState);
    }

    //Funkcija ispituje pripadnost rijeci jeziku
    //koji je reprezentovan deterministickim konacnim automatom
    //Na pocetku metode se provjerava da li je ulazni string prazan(epsilon)
    //Ako jeste vrsimo provjeru da li se pocetno stanje nalazi u skupu finalnih stanja
    //Inace prolazimo kroz ulazni string i mijenjamo trenutno stanja automata
    //na osnovu definisanih tranzicija. Nakon iteracije kroz string provjeravamo da li je
    //automat stigao u finalno stanja, tako sto provjerimo da li je stanje u kojem smo zavrsili
    //nalazi u skupu finalnih stanja
    //Slozenost:O(size(str))
    public boolean checkString(String str) throws IllegalArgumentException {
        if (str.equals(Character.toString(EpsilonNka.EPSILON)))//O(1)
            return this.finalStates.contains(this.startState);
        String currentlyState = this.startState;
        for (var el : str.toCharArray()) {
            if (!this.alphabet.contains(el))
                throw new IllegalArgumentException(ALPHABET_NOT_SUPPORTED);
            currentlyState = this.transition.get(Pair.makePair(currentlyState, el));
            if (currentlyState == null)
                return false;
        }
        return this.finalStates.contains(currentlyState);
    }

    //Svaki dka je ujedno i nka, a svaki nka je ujedno i enka
    //Ova funkcija kreira novi enka na osnovu polja trenutnog dka
    //Implementacija interfejsa IRegularLanguage
    //Slozenost:O(T) - broj prelaza
    @Override
    public EpsilonNka toENka() {
        Character[] temp = new Character[this.alphabet.size()];
        this.alphabet.toArray(temp);
        EpsilonNka enka = new EpsilonNka(temp, this.startState);
        enka.finalStates = this.finalStates;
        enka.states = this.states;
        for (var pair : this.transition.entrySet())
            enka.addTransition(pair.getKey(), pair.getValue());
        return enka;
    }

    //Vraca trenutni automat
    //Implementacija interfejsa IRegularLanguage
    //Slozenost:O(1)
    @Override
    public Dka toDka() {
        return this;
    }

    //Funkcija vraca duzinu minimalne rijeci u jeziku koji je reprezentovan sa dka
    //Predstavlja implementacija interfejsa IRegularLanguage
    //Koristi se bfs algoritam za pronalazak
    //minimalne udaljenosti od pocetnog cvora
    //Nivo svakog cvora se pamti u mapi i nivo trenutnog cvora je jednak nivou
    //cvora preko kog smo dosli do trenutnog cvora + 1.
    //Zaustavljamo se kada dodjemo do prvog finalnog stanja
    //Slozenost:O(S*A)
    @Override
    public int lengthOfMinimalWord() {
        Queue<String> toVisit = new LinkedList<>();
        HashMap<String, Integer> levels = new HashMap<>();
        toVisit.add(this.startState);
        levels.put(this.startState, 0);
        if (this.finalStates.contains(this.startState))
            return 0;
        while (!toVisit.isEmpty()) {//O(S)
            String state = toVisit.remove();//o(1)
            for (var symbol : this.alphabet) {//O(al)
                String newState = this.transition.get(Pair.makePair(state, symbol));
                if (!levels.containsKey(newState)) {
                    toVisit.add(newState);
                    levels.put(newState, levels.get(state) + 1);
                    if (this.finalStates.contains(newState))
                        return levels.get(newState);
                }
            }
        }
        return 0;
    }

    //Funkcija koja vrsi provijeru da li su dva jezika jednaka
    //Predstavlja implementacija interfejsa IRegularLanguage
    //Funkcija kao argument prima neki regularni jezik koji ce se porediti
    //po jednakosti sa trenutnim dka.
    //Prvo se vrsi konverzija jezika koji je zadan kao argument u dka metodom toDka
    //koja je specificirana u interfejsu IRegularLanguage(sve reprezentacija imaju implementiranu ovu metodu)
    //Nakon toga radimo provjeru alfabeta, nakon cega radimo modifikovani bfs obilazak oba grafa
    //Krecemo od para pocetnih stanja, i dobijamo stanja u koja prelazimo za dati simbol
    //Da bi jezici bili jednaki oba stanja u paru moraju biti ili finalna ili nefinalna
    //Parove stanja koja nismo posjetili smijestamo u red tako da ih poslije mozemo provjeriti
    //Ako svi parovi stanja zadovoljavaju ovaj uslov jezici su jednaki
    //Slozenost:O(toDka())+O(S*A) - za dka O(S*A)
    @Override
    public boolean languageEquals(IRegularLanguage oth) throws IllegalArgumentException {
        if (oth == null)
            throw new IllegalArgumentException(IRegularLanguage.ARGUMENT_NULL_EXCEPTION);
        Dka other = (Dka) oth.toDka();
        if (!this.alphabet.equals(other.alphabet))//O(A)
            return false;
        Queue<Pair<String, String>> toVisit = new LinkedList<>();
        HashSet<Pair<String, String>> visited = new HashSet<>();
        var start = Pair.makePair(this.startState, other.startState);
        toVisit.add(start);
        visited.add(start);
        while (!toVisit.isEmpty()) {//O(S)
            var pair = toVisit.remove();
            for (var symbol : this.alphabet) {//O(A)
                String state1 = this.transition.get(Pair.makePair(pair.first, symbol));
                String state2 = other.transition.get(Pair.makePair(pair.second, symbol));
                if (!((this.finalStates.contains(state1) && other.finalStates.contains(state2)) ||
                        (!this.finalStates.contains(state1) && !other.finalStates.contains(state2))))
                    return false;
                var newPair = Pair.makePair(state1, state2);
                if (!visited.contains(newPair)) {
                    visited.add(newPair);
                    toVisit.add(newPair);
                }
            }
        }
        return true;
    }

    //pomocna funkcija za generisanje novog imena
    //koja vrsi konkatenaciju dva imena stanja
    //Slozenost:O(1)
    public String generateStateName(Pair<String, String> pair) {
        return pair.first + "-" + pair.second;
    }

    //Funkcionalni interfejs koji se koristi
    //u univerzalnim operacijama
    private interface StdFunction {
        boolean accept(HashSet<String> finalState1, HashSet<String> finalState2, String state1, String state2);
    }

    //Univerzalna funkcija pomocu koje ne moramo pisati istu programsku logiku
    //za operacije koje se razlikuju po uslovu za dodavanje finalnih stanja
    //Ova funkcija se koristi za uniju presjek i razliku regularnih jezika
    //Na pocetku izvrsimo odgovarajucu konverziju drugog regularnog jezika u dka
    //Gore pomenute operacije mozemo izvrsiti samo ako su azbuke jednake pa vrsimo provjerimo azbuka
    //nakon cega pokrecemo modifikovani bfs algoritam.
    //Nova stanja i tranzicije se dodaju u odgovarajuci automat
    //a finalna stanja se dodaju na osnovu rezultata proslijedjenog delegata
    //Slozenost:O(toDka())+O(S1*S2*A) - za dka O(S1*S2*A)
    private Dka universalOperation(IRegularLanguage other, StdFunction func) throws DifferentAlphabetException {
        if (other == null)
            throw new IllegalArgumentException(IRegularLanguage.ARGUMENT_NULL_EXCEPTION);
        Dka dkaOther = (Dka) other.toDka();
        if (!this.alphabet.equals(dkaOther.alphabet))//O(A)
            throw new DifferentAlphabetException();
        HashMap<Pair<String, String>, String> visited = new HashMap<>();
        Queue<Pair<String, String>> toVisit = new LinkedList<>();
        //pocetno stanje
        var start = Pair.makePair(this.startState, dkaOther.startState);
        String newState = generateStateName(start);
        Dka newDka = new Dka((HashSet<Character>) this.alphabet.clone(), newState);//O(A)
        if (func.accept(this.finalStates, dkaOther.finalStates, this.startState, dkaOther.startState))//O(1)
            newDka.addFinalState(newState);

        visited.put(start, newState);
        toVisit.add(start);
        while (!toVisit.isEmpty()) {//0(S)
            var states = toVisit.remove();
            for (var symbol : this.alphabet) {//O(A)
                String state1 = this.transition.get(Pair.makePair(states.first, symbol));
                String state2 = dkaOther.transition.get(Pair.makePair(states.second, symbol));
                String name = "";
                var newPair = Pair.makePair(state1, state2);
                if (visited.containsKey(newPair))
                    name = visited.get(newPair);
                else {
                    name = generateStateName(Pair.makePair(state1, state2));
                    newDka.addStates(name);
                }

                newDka.addTransition(Pair.makePair(visited.get(states), symbol), name);
                if (func.accept(this.finalStates, dkaOther.finalStates, state1, state2))
                    newDka.addFinalState(name);
                if (!visited.containsKey(newPair)) {
                    visited.put(newPair, name);
                    toVisit.add(newPair);
                }
            }
        }
        return newDka;
    }

    //Funkcija za uniju dva regularna jezika
    //Predstavlja implementaciju IRegularLanguage interfejsa
    //Funckija se oslanja na funkctionalnosti funkcije za univerzalnu operacija
    //u koju samo ubrizgamo odgovarajuci kriterijum za dodavanje finalnih stanja
    ///Slozenost:O(toDka())+O(S1*S2*A) - za dka O(S1*S2*A)
    @Override
    public Dka union(IRegularLanguage other) throws DifferentAlphabetException {
        return this.universalOperation(other, (set1, set2, state1, state2) -> {
            return set1.contains(state1) ||
                    set2.contains(state2);
        });
    }

    //Funkcija za presjek dva regularna jezika
    //Predstavlja implementaciju IRegularLanguage interfejsa
    //Funckija se oslanja na funkctionalnosti funkcije za univerzalnu operacija
    //u koju samo ubrizgamo odgovarajuci kriterijum za dodavanje finalnih stanja
    //Slozenost:O(toDka())+O(S1*S2*A) - za dka O(S1*S2*A)
    @Override
    public Dka intersection(IRegularLanguage other) throws DifferentAlphabetException {
        return this.universalOperation(other, (set1, set2, state1, state2) -> {
            return set1.contains(state1) &&
                    set2.contains(state2);
        });
    }

    //Funkcija za razliku dva regularna jezika
    //Predstavlja implementaciju IRegularLanguage interfejsa
    //Funckija se oslanja na funkctionalnosti funkcije za univerzalnu operacija
    //u koju samo ubrizgamo odgovarajuci kriterijum za dodavanje finalnih stanja
    //Slozenost:O(toDka())+O(S1*S2*A) - za dka O(S1*S2*A)
    @Override
    public Dka difference(IRegularLanguage other) throws DifferentAlphabetException {
        return this.universalOperation(other, (set1, set2, state1, state2) -> {
            return set1.contains(state1) &&
                    !set2.contains(state2);
        });
    }

    //Funkcija koja realizuje komplement jezika
    //Funkcija vraca novi dka koji ima invertovan skup finalnih stanja
    //O(T+S+F)
    @Override
    public Dka complement() {
        Dka newDka = new Dka(this.alphabet, this.startState);
        newDka.transition = (HashMap<Pair<String, Character>, String>) this.transition.clone();
        newDka.states = (HashSet<String>) this.states.clone();
        HashSet<String> newFinalStates = new HashSet<>(this.states);
        newFinalStates.removeAll(this.finalStates);
        newDka.finalStates = newFinalStates;
        return newDka;
    }

    //Funkcija koja vraca duzinu najduze rijeci u jeziku
    //Predstavlja implementaciju metode iz IRegularLanguage interfejsa
    //Na pocetku se provjerava konacnost jezika, ako je jezik konacan nastavljamo dalje
    //Koristimo modifikovani bfs algoritam pri cemu za svako stanje pamtimo trenutnu najvecu udaljenost
    //od pocetnog cvora
    //Za svaki cvor do kojeg dodjemo provjeravamo da li je duzina puta do njega veca preko cvora
    //do kojeg smo trenutno dosli do njega
    //Kada smo pronasli najduzi put od pocetnog cvora za svaki cvor
    //trazimo maksimalnu duzinu medju finalnim stanjima
    ////Slozenost:0(S*A)
    public int lenghtOfLongestWord() throws LanguageNotFinallyException {
        if (!this.finalityOfLanguage())//O(S*A)
            throw new LanguageNotFinallyException();
        Queue<String> toVisit = new LinkedList<>();
        HashMap<String, Integer> visited = new HashMap<>();
        toVisit.add(this.startState);
        visited.put(this.startState, 0);
        while (!toVisit.isEmpty()) { //O(s)
            String state = toVisit.remove();
            for (var symbol : this.alphabet) {//O(A)
                String newState = this.transition.get(Pair.makePair(state, symbol));
                if (!visited.containsKey(newState)) {
                    toVisit.add(newState);
                    visited.put(newState, visited.get(state) + 1);
                } else if (visited.get(state) + 1 > visited.get(newState))
                    visited.put(newState, visited.get(state) + 1);
            }
        }
        int max = 0;
        for (var pair : visited.entrySet())//O(s)-posjetili sva stanja
            if (this.finalStates.contains(pair.getKey()))
                max = Math.max(pair.getValue(), max);

        return max;
    }

    //Wrap klasa koja omogucava vracanje boolean vrijednosti
    //preko argumente funkcija(ideja iz c jezika gdje se pokazivaci koriste
    //kada trebamo da vratimo vise od jedne vrijednosti
    private class StatusWrapper {
        public boolean status;
    }

    //Funkcija koja provjerava konacnog regularnog jezika
    //Ideja algoritma je da pokusamo detektovati ciklus u nasem automatu
    //Ako smo detektovali ciklus zapocinjemo potragu za finalnim stanjem
    //od bilo kojeg stanja iz ciklusa. Ako iz nekog stanja u ciklusu mozemo
    //da dodjemo do finalnog jezik nije konacan.
    //Algoritam se bazira na dfs obilasku pri cemu se posjeceni cvor pamti na steku
    //Ako preko nekog stanja ponovo dodjemo do stanja koje se nalazi na steku(imamo povratnu granu)
    //detektovali smo ciklus i pokrecemo funkciju reachableToFinal()
    //Slozenost:0(S*A)
    @Override
    public boolean finalityOfLanguage() {
        HashSet<String> visited = new HashSet<>();
        Stack<String> cycleState = new Stack<>();
        StatusWrapper status = new StatusWrapper();
        status.status = true;
        this.dfs(visited, cycleState, this.startState, status);
        return status.status;
    }

    //Funkcija koja provjerava da li se od datog stanja
    //moze doci do finalnog stanja
    //algoritam je baziran na bfs obilasku pri cemu se kao pocetak uzima
    //proslijedjeno stanje
    //Ako u procesu obilaska naidjemo na finalnom stanje zaustavljamo se i vracamo true
    //Slozenost:0(S*A)
    private boolean reachableToFinal(String state) {
        Queue<String> toVisit = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        toVisit.add(state);
        visited.add(state);
        if (this.finalStates.contains(state))
            return true;
        while (!toVisit.isEmpty()) {
            String oldState = toVisit.remove();
            for (var symbol : this.alphabet) {
                String newState = this.transition.get(Pair.makePair(oldState, symbol));
                if (!visited.contains(newState)) {
                    toVisit.add(newState);
                    visited.add(newState);
                }
                if (this.finalStates.contains(newState))
                    return true;
            }
        }
        return false;
    }

    //Dfs obilazak koji se koristi za realizaciju funkcije finalityOfLanguage()
    //Slozenost:0(S*A)
    private void dfs(HashSet<String> visited, Stack<String> cycleStack, String startState, StatusWrapper status) {
        visited.add(startState);
        cycleStack.push(startState);//O(1)
        for (var symbol : this.alphabet)//O(A)
            {
            var newState = this.transition.get(Pair.makePair(startState, symbol));
            if (!visited.contains(newState))
                dfs(visited, cycleStack, newState, status);
            else if (cycleStack.search(newState) != -1) {
                boolean status1 = this.reachableToFinal(newState);//O(S*A)
                if (status1)
                    status.status = false;
            }
        }
        cycleStack.pop();
    }


}
