package RegularniIzraz;

import java.util.Stack;
import java.util.HashMap;

public class RegularToPostfixConvertor {
    //Tabela prioriteta za svaki operator sadrzi dvije vrste
    //prioriteta: prioritet kada operator dolazi na stek, prioritet kada je
    // operator na steku
    class PriorityElement {
        public int inPriority;
        public int stackPriority;

        PriorityElement(int inPriority, int stackPriority) {
            this.inPriority = inPriority;
            this.stackPriority = stackPriority;
        }
    }

    private HashMap<Character, PriorityElement> priorityTable = new HashMap<>();
    private RegularExpression regex;

    //Konstruktor kao parametar prihvata regex
    //kojeg trebamo pretvoriti u postfix.
    //Takodje vrsimo inicijalizaciju tabele prioriteta
    public RegularToPostfixConvertor(RegularExpression regex) {
        this.regex = regex;
        priorityTable.put(RegularExpression.KLEEN_STAR, new PriorityElement(5, 5));
        priorityTable.put(RegularExpression.CONCATENATION, new PriorityElement(4, 4));
        priorityTable.put(RegularExpression.ALTERNATIVE, new PriorityElement(3, 3));
        priorityTable.put('(', new PriorityElement(6, 0));
        priorityTable.put(')', new PriorityElement(1, 0));
    }

    //Algoritam za konverziju za postfix radi na sljedeci nacin
    //Ako je trenutni simbol koji skeniramo operand dodajemo ga u postfix, inace
    //Ako je ulazni prioritet operatora manji nego prioritet operatora na vrhu steka
    //sa steka skidame operatore koji imaju veci stek prioritet jer oni trebaju da dodju
    //prije u izrazu. Kada je ulazni prioritet veci od stek pri el. na vrhu operator se dodaje na stek
    //Ako je ulazni simbol otvorena zagrada sve sto je u zagradi ima veci prioritet od onog sto je izvan zagrada
    //pa lijeva zagrada ima najveci ulazni prioritet i stavlja se na stek
    //Zagrada ima najveci stek prioritet pa sve sto dolazi poslije se stavlja na stek
    //Kada je ulazni simbol ) sa steka skidamo sve operatore do ( nakon cega skidamo i )
    //Slozenost:O(size(regex))
    public String convertToPostfix() {
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (var symbol : this.regex.getRegex().toCharArray()) {
            if (Character.isAlphabetic(symbol) || Character.isDigit(symbol))
                builder.append(symbol);
            else {
                while (!stack.isEmpty() && (priorityTable.get(symbol).inPriority <= priorityTable.get(stack.peek()).stackPriority)) {
                    Character operator = stack.pop();
                    builder.append(operator);
                }
                if (symbol != ')')
                    stack.push(symbol);
                else
                    stack.pop();
            }
        }
        while (!stack.isEmpty()) {
            Character operator = stack.pop();
            builder.append(operator);
        }
        return builder.toString();
    }
}
