package Automati;

import PomocneStrukturePodataka.Pair;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;

public class DkaMinimization {
    private Dka automat;

    //Klasa za minimizaciju dka u konstruktoru prima dka nad kojim
    //cemo izvrsiti minimizaciju
    public DkaMinimization(Dka automat) {
        this.automat = automat;
    }

    //Funkcija koja obavlja uklanjanje nedostizih stanja
    //Koristimo bfs algoritam da bi smo pronasli koja su stanja
    //dostizna od pocetnog stanja
    //Nakon pronalaska dostiznih stanja vrsimo presjek sa skupom stanja
    //i dobijamo stanja koja nisu dostizna
    //iz skupa stanja i tranzicija automata uklanjamo sva nedostizna stanja
    //tranzicije od nedostiznih stanja ka ostalim stanjima
    //Slozenost:O(S*A)
    public void removeUnreachableStates() {
        HashSet<String> visitedStates = new HashSet<>();
        Queue<String> toVisit = new LinkedList<>();
        toVisit.add(this.automat.startState);
        visitedStates.add(this.automat.startState);
        while (!toVisit.isEmpty())//bfs
        {
            String curr = toVisit.remove();
            for (var symbol : automat.alphabet) {
                String temp = this.automat.transition.get(Pair.makePair(curr, symbol));
                if (temp != null && !visitedStates.contains(temp)) {
                    toVisit.add(temp);
                    visitedStates.add(temp);
                }
            }
        }
        HashSet<String> unreachableStates = new HashSet<>(this.automat.states);//O(s)
        unreachableStates.removeAll(visitedStates);//O(s)
        if (unreachableStates.size() == 0)
            return;
        this.automat.states.removeAll(unreachableStates);//O(s)
        //brisanje iz transition
        for (var unState : unreachableStates)
            for (var symbol : this.automat.alphabet)
                this.automat.transition.remove(Pair.makePair(unState, symbol));
    }

    //Funkcija koja obavlja minimizaciju automata
    //Koraci algoritma ce biti opisani u samom algoritmu
    //Slozenost:0(S^3)
    public void minimize() {
        //Prvi korak je algoritma uklanjanje nedostiznih stanja gore pomenutim algoritmom
        this.removeUnreachableStates();
        //Sljedeci korak je napravimo parove svih stanja
        //i razvrstamo ih prema tome da li je neki od dva stanja u paru finalno
        //ako jeste dodajemo ga u oneFinal, ako oba nisu finalna dodajemo par u otherPair
        //U skupove parova stanja ne stavljamo parove istih stanja i takodje
        //ne razlikujemo koje je prvo a koje drugo stanje
        HashSet<Pair<String, String>> oneFinal = new HashSet<>();
        HashSet<Pair<String, String>> otherPair = new HashSet<>();
        for (var state1 : this.automat.states)
            for (var state2 : this.automat.states) {
                if (!state1.equals(state2)) {
                    var pair1 = Pair.makePair(state1, state2);
                    var pair2 = Pair.makePair(state2, state1);
                    if ((this.automat.finalStates.contains(state1) ^ this.automat.finalStates.contains(state2))) {
                        if (!oneFinal.contains(pair1) && !oneFinal.contains(pair2))
                            oneFinal.add(pair1);
                    } else {
                        if (!otherPair.contains(pair1) && !otherPair.contains(pair2))
                            otherPair.add(pair1);
                    }
                }
            }

        //Sljedeci dio koda je glavni dio u postupku minimizacije
        //Za sva stanja koja se ne nalaze u skupu cije je jedno stanja finalno
        //provjeravamo da li postoji simbol za koji ce se rezultantna stanja
        //nalaziti u skupu stanja sa bar jednim finalnim. Ako se par rez. stanja nalazi u tom skupu
        //dodajemo trenutna stanja u dati skup i uklanjamo iz skupa otherPair
        //Postupak ponavljamo sve dok se skup stanja kod kojeg je jedno finalno prosiruje
        boolean status = true;
        while (status) {
            HashSet<Pair<String, String>> otherTemp = new HashSet<>(otherPair);
            status = false;
            for (var states : otherPair) {
                for (var symbol : this.automat.alphabet) {
                    String s1 = this.automat.transition.get(Pair.makePair(states.first, symbol));
                    String s2 = this.automat.transition.get(Pair.makePair(states.second, symbol));
                    var pair1 = Pair.makePair(s1, s2);
                    var pair2 = Pair.makePair(s2, s1);
                    if (oneFinal.contains(pair1) || oneFinal.contains(pair2)) {
                        oneFinal.add(states);
                        otherTemp.remove(states);
                        status = true;
                        break;
                    }
                }
            }
            otherPair = otherTemp;
        }
        //Nakon zavrstetka gornje postupka stanja koja su ostala u skupu otherState
        //predstavljaju stanja koja se mogu grupisati u klase ekvivalencije
        //Prolazimo kroz svaki par stanja nakon cega iteriramo kroz klase ekvivalencija
        //koje smo do sada kreirali
        //ako se neko stanje iz para nalazi u klasi klasi ekvivalnecije u istu klasu
        //dodajemo drugo stanje
        //ako par ne pripada ni jednoj klasi dodajemo novu klasu ekvivalencije sa datim stanjima
        HashSet<HashSet<String>> equivalenceClass = new HashSet<>();
        HashSet<String> eqvStates = new HashSet<>();
        for (var pair : otherPair) {
            boolean added = false;
            for (var classes : equivalenceClass) {
                if (classes.contains(pair.first) || classes.contains(pair.second)) {
                    classes.add(pair.first);
                    classes.add(pair.second);
                    added = true;
                    break;
                }
            }
            if (!added) {
                HashSet<String> temp = new HashSet<>();
                temp.add(pair.first);
                temp.add(pair.second);
                equivalenceClass.add(temp);
            }
            eqvStates.add(pair.first);
            eqvStates.add(pair.second);
        }
        //U skupu eqvStates smo sacuvali stanja koja se nalaze u nekim od klasa ekvivalencija
        //Sljedeci korak je da u klase ekvivalencije dodamo jedinicne klase
        //od svih preostalih stanja
        HashSet<String> otherClasses = new HashSet<>(this.automat.states);
        otherClasses.removeAll(eqvStates);
        for (var state : otherClasses) {
            HashSet<String> temp = new HashSet<>();
            temp.add(state);
            equivalenceClass.add(temp);
        }

        //Funkcija koja na osnovu klasa ekvivalencija
        //formira nova finalna i nefinalna stanja
        HashSet<String> newFinalStates = new HashSet<>();
        HashSet<String> newStates = new HashSet<>();
        String newStartState = this.automat.startState;
        for (var state : equivalenceClass) {
            var temp = new HashSet<>();
            temp.addAll(state);
            temp.retainAll(this.automat.finalStates);
            String newState = String.join("-", state);
            newStates.add(newState);
            if (temp.size() > 0)
                newFinalStates.add(newState);
            if (state.contains(this.automat.startState))
                newStartState = newState;
        }

        //Nakon sto smo odredili nova stanja kreiramo novu delta funkciju
        //Iz svake klase ekvivalencija uzimamo jednog predstavnika i iteriramo kroz simbole
        //pronalazimo rezultantna stanja na osnovu pivota i trenutnog simbola
        //i trazimo kojoj klasi ekv pripada rezultantno stanje
        //Nakon sto smo pronasli odgovarajuci klasu ekv. dodajemo novu tranziciju
        HashMap<Pair<String, Character>, String> newTransition = new HashMap<>();
        for (var state : equivalenceClass) {
            String pivot = state.iterator().next();
            for (var symbol : this.automat.alphabet) {
                String oldState = this.automat.transition.get(Pair.makePair(pivot, symbol));
                for (var temp : equivalenceClass)
                    if (temp.contains(oldState)) {
                        newTransition.put(Pair.makePair(String.join("-", state), symbol), String.join("-", temp));
                        break;
                    }
            }
        }
        //automat dobija nove minimizovane atribute
        this.automat.states = newStates;
        this.automat.finalStates = newFinalStates;
        this.automat.transition = newTransition;
        this.automat.startState = newStartState;
    }
}
