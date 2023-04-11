package SpecifikacijaRegularnogJezika;

import Interfejsi.IRegularLanguage;
import SpecifikacijaRegularnogJezika.Izuzeci.LexicalErrorException;
import SpecifikacijaRegularnogJezika.Izuzeci.SyntaxErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SpecificationAnalizer {
    private List<String> lines;

    public SpecificationAnalizer(List<String> input) {
        this.lines = input;
    }


    public ArrayList<ArrayList<String>> getAllTokens() throws SyntaxErrorException, LexicalErrorException {
        if (lines.size() == 0)
            throw new SyntaxErrorException();
        if ("DKA;".equals(lines.get(0))) {
            if (lines.size() < 6)
                throw new SyntaxErrorException();
            return getDkaTokens();
        } else if ("ENKA;".equals(lines.get(0))) {
            if (lines.size() < 6)
                throw new SyntaxErrorException();
            return getENkaTokens();
        } else if ("REGEX;".equals(lines.get(0))) {
            if (lines.size() < 3)
                throw new SyntaxErrorException();
            return getRegexTokens();
        } else
            throw new LexicalErrorException(1);
    }

    public ArrayList<ArrayList<String>> getDkaTokens() throws LexicalErrorException {
        return this.getDkaEnkaTokens(false);
    }

    //0 dka
    //1 dka
    private ArrayList<ArrayList<String>> getDkaEnkaTokens(boolean type) throws LexicalErrorException {
        ArrayList<ArrayList<String>> tokens = new ArrayList<>();
        int numLine = 2;
        if (type == false)
            tokens.add(new ArrayList<>(Arrays.asList("DKA")));
        else
            tokens.add(new ArrayList<>(Arrays.asList("ENKA")));

        while (numLine != 4) {
            String line = this.getLineContent(this.lines.get(numLine - 1), numLine);
            tokens.add(this.getLineTokens(line, numLine));
            numLine++;
        }

        //pocetno stanje
        String line = getLineContent(this.lines.get(numLine - 1), numLine);
        if (line.contains(","))
            throw new LexicalErrorException(numLine);
        tokens.add(new ArrayList<>(Arrays.asList(line)));
        numLine++;

        line = getLineContent(this.lines.get(numLine - 1), numLine);
        if (type == false) {
            tokens.add(this.getTransitionTokens(line, numLine));
            numLine++;
        } else {
            tokens.add(this.getTransitionTokensEnka(line, numLine));
            numLine++;
        }
        //za finalna stanja
        line = getLineContent(this.lines.get(numLine - 1), numLine);
        tokens.add(this.getLineTokens(line, numLine));
        numLine++;

        if (this.lines.size() > 6) {
            List<String> temp = this.lines.subList(6, this.lines.size());
            tokens.add(new ArrayList<>());
            for (var str : temp)
                tokens.get(6).add(this.getLineContent(str, numLine++));
        }
        return tokens;
    }
    //dobijamo izraz do ;
    private String getLineContent(String line, int number) throws LexicalErrorException {
        if (!line.endsWith(";") || line.endsWith(";;"))
            throw new LexicalErrorException(number);
        String[] lines = line.split(";");
        if (lines.length != 1)
            throw new LexicalErrorException(number);
        return lines[0];
    }

    private ArrayList<String> getLineTokens(String line, int number) throws LexicalErrorException {
        ArrayList<String> tokens = new ArrayList<>();
        if (line.endsWith(","))
            throw new LexicalErrorException(number);
        String[] arg2 = line.split(",");
        for (var str : arg2) {
            String temp = str.trim();
            if (temp.length() == 0)
                throw new LexicalErrorException(number);
            tokens.add(temp);
        }
        return tokens;
    }

    private ArrayList<String> getTransitionTokens(String line, int number) throws LexicalErrorException {
        //na nama je da provjerimo format tokena
        ArrayList<String> tokens = this.getLineTokens(line, number);
        for (var str : tokens)
            if (!str.matches("[\\w]+-[\\w]-[\\w]+"))
                throw new LexicalErrorException(number);
        return tokens;
    }

    private ArrayList<String> getTransitionTokensEnka(String line, int number) throws LexicalErrorException {
        ArrayList<String> tokens = this.getLineTokens(line, number);
        for (var str : tokens)
            if (!str.matches("[\\w]+-[\\w]-\\(([\\w]+\\|)*[\\w]+\\)$"))
                throw new LexicalErrorException(number);
        return tokens;
    }


    private ArrayList<ArrayList<String>> getENkaTokens() throws LexicalErrorException {
        return this.getDkaEnkaTokens(true);
    }

    private ArrayList<ArrayList<String>> getRegexTokens() throws LexicalErrorException {
        ArrayList<ArrayList<String>> tokens = new ArrayList<>();
        tokens.add(new ArrayList<>(Arrays.asList("REGEX")));
        int numLine = 2;
        //alfabet
        String line = this.getLineContent(this.lines.get(numLine - 1), numLine);
        tokens.add(this.getLineTokens(line, numLine++));
        //regex
        line = this.getLineContent(this.lines.get(numLine - 1), numLine);
        checkRegexLine(line, numLine);
        tokens.add(new ArrayList<>(Arrays.asList(line)));
        numLine++;

        if (this.lines.size() > 3) {
            List<String> temp = this.lines.subList(3, this.lines.size());
            tokens.add(new ArrayList<>());
            for (var str : temp)
                tokens.get(3).add(this.getLineContent(str, numLine++));
        }
        return tokens;
    }

    private void checkRegexLine(String line, int numLine) throws LexicalErrorException {
        if (line.contains("..") || line.contains("**") || line.contains("||"))
            throw new LexicalErrorException(numLine);
        try {
            Pattern temp = Pattern.compile(line);
        } catch (PatternSyntaxException e) {
            throw new LexicalErrorException(numLine);
        }
    }
}
