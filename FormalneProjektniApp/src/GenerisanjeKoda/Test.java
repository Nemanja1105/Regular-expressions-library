package GenerisanjeKoda;

import Automati.Dka;
import PomocneStrukturePodataka.Pair;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Dka automat = new Dka(new Character[]{'0', '1'}, "q0");
        automat.addStates("q1");
        automat.addTransition(Pair.makePair("q0", '0'), "q0");
        automat.addTransition(Pair.makePair("q0", '1'), "q1");
        automat.addTransition(Pair.makePair("q1", '0'), "q1");
        automat.addTransition(Pair.makePair("q1", '1'), "q0");
        automat.addFinalState("q1");
        JavaCodeGen gen = new JavaCodeGen(automat, "");
        gen.genCode();
    }
}
