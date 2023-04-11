package Interfejsi;

import Izuzeci.DifferentAlphabetException;
import Izuzeci.LanguageNotFinallyException;
import Izuzeci.ObjectNotCompletedException;

public interface IRegularLanguage {
    String ARGUMENT_NULL_EXCEPTION = "Operaciju nije moguce primjeniti nad null!";

    boolean checkString(String str) throws ObjectNotCompletedException;

    //Apstraktna metoda koja treba da vrsi konverziju regularnog
    //jezika u dka
    IRegularLanguage toDka();

    //Apstraktna metoda koja treba da vrsi konverziju regularnog
    //jezika u enka
    IRegularLanguage toENka();

    //Apstraktna metoda koja vrsi uniju dva regularna jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u dka i iskoristi implementacija unija za 2 dka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S1*S2*A)
    default IRegularLanguage union(IRegularLanguage other) throws DifferentAlphabetException, IllegalArgumentException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        return this.toDka().union(other);
    }

    //Apstraktna metoda koja vrsi presjek dva regularna jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u dka i iskoristi implementacija presjeka za 2 dka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S1*S2*A)
    default IRegularLanguage intersection(IRegularLanguage other) throws DifferentAlphabetException, IllegalArgumentException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        return this.toDka().intersection(other);
    }

    //Apstraktna metoda koja vrsi razliku dva regularna jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u dka i iskoristi implementacija razlike za 2 dka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S1*S2*A)
    default IRegularLanguage difference(IRegularLanguage other) throws DifferentAlphabetException, IllegalArgumentException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        return this.toDka().difference(other);
    }

    //Apstraktna metoda koja vrsi konkatenaciju dva regularna jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u enka i iskoristi implementacija konkatenacije za 2 dka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toEnka)+O(S)+O(F)
    default IRegularLanguage concatenation(IRegularLanguage other) throws DifferentAlphabetException, IllegalArgumentException {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        return this.toENka().concatenation(other);
    }

    //Apstraktna metoda koja vrsi komplement jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u enka i iskoristi implementacija komplemente za enka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toEnka)+O(S+F)
    default IRegularLanguage complement() {
        return this.toENka().complement();
    }

    //Apstraktna metoda koja vrsi operaciju klinove zvijezde nad jezikom
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u enka i iskoristi implementacija klinove zvijezde za enka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toEnka)+O(S+F)
    default IRegularLanguage kleeneStar() {
        return this.toENka().kleeneStar();
    }

    //Apstraktna metoda koja kao rezultat treba da vrati duzinu najkrace rijeci
    //u jeziku. Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u enka i iskoristi implementacija za enka. Klase nasljednice mogu da
    //redefinisu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toEnka)+O(S*A)
    default int lengthOfMinimalWord() {
        return this.toENka().lengthOfMinimalWord();
    }

    //Apstraktna metoda koja vrsi poredjenja dva regularna jezika po jednakosti
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u dka i iskoristi implementacija za poredjenje dva dka
    //Klase nasljednice mogu da redefinicu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S*A)
    default boolean languageEquals(IRegularLanguage other) {
        if (other == null)
            throw new IllegalArgumentException(ARGUMENT_NULL_EXCEPTION);
        return this.toDka().languageEquals(other);
    }

    //Apstraktna metoda koja kao rezultat treba da vrati duzinu najduze rijeci
    //u jeziku. Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda
    //konvertuje u dka i iskoristi implementacija za dka. Klase nasljednice mogu da
    //redefinisu ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S*A)
    default int lenghtOfLongestWord() throws LanguageNotFinallyException {
        if (!this.finalityOfLanguage())
            throw new LanguageNotFinallyException();
        return this.toDka().lenghtOfLongestWord();
    }

    //Apstraktna metoda koja provjerava konacnost regularnog jezika
    //Podrazumjevano ponasanje je da se objekat nad kojim je pozvana metoda konvertuje
    //u dka i iskoristi implementaciju u dka. Klase nasljednice mogu da redefinisu
    //ovo ponasanje i tako optimizuju operaciju
    //Slozenost:O(toDka)+O(S*A)
    default boolean finalityOfLanguage() {
        return this.toDka().finalityOfLanguage();
    }

}
