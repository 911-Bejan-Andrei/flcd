import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomata {
    private Set<String> states;
    private Set<String> alphabet;
    private Map<String, Map<String, String>> transitions;
    private String initialState;
    private Set<String> finalStates;

    public FiniteAutomata() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transitions = new HashMap<>();
        finalStates = new HashSet<>();
    }

    public void loadFromFile(String filename) {
        try {
            String line = "";
            String aux = line;
            Scanner scanner = new Scanner(new File(filename));
            line = scanner.nextLine();
            while (scanner.hasNextLine()) {
                if (line.startsWith("States:")) {
                    states = new HashSet<>(Arrays.asList(line.split(":")[1].split("\\s+")));
                    line = scanner.nextLine().trim();
                } else if (line.startsWith("Alphabet:")) {
                    alphabet = new HashSet<>(Arrays.asList(line.split(":")[1].split("\\s+")));
                    line = scanner.nextLine().trim();
                } else if (line.startsWith("Transitions:")) {
                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine().trim();
                        if (line.isEmpty() || line.startsWith("Initial State:")) {
                            break;
                        }
                        String[] parts = line.split("\\s+");
                        String source = parts[0];
                        String symbol = parts[1];
                        String target = parts[2];
                        transitions.computeIfAbsent(source, k -> new HashMap<>()).put(symbol, target);
                    }
                } else if (line.startsWith("Initial State:")) {
                    initialState = line.split(":")[1].trim();
                    line = scanner.nextLine().trim();
                } else if (line.startsWith("Final States:")) {
                    finalStates = new HashSet<>(Arrays.asList(line.split(":")[1].split("\\s+")));
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    public void displayElements() {
        System.out.println("Set of States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Transitions:");
        for (String source : transitions.keySet()) {
            for (String symbol : transitions.get(source).keySet()) {
                String target = transitions.get(source).get(symbol);
                System.out.println(source + " --(" + symbol + ")--> " + target);
            }
        }
        System.out.println("Initial State: " + initialState);
        System.out.println("Set of Final States: " + finalStates);
    }

    public boolean isAcceptedSequence(String sequence) {
        String currentState = initialState;
        for (char symbol : sequence.toCharArray()) {
            String symbolStr = String.valueOf(symbol);
            if (!alphabet.contains(symbolStr)) {
                return false;
            }
            if (transitions.containsKey(currentState) && transitions.get(currentState).containsKey(symbolStr)) {
                currentState = transitions.get(currentState).get(symbolStr);
            }
        }
        return finalStates.contains(currentState);
    }

    public static void main(String[] args) {
        FiniteAutomata fa = new FiniteAutomata();
        fa.loadFromFile("C:\\Users\\andya\\Desktop\\lab4\\fa_input.txt");
        fa.displayElements();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a sequence to check if it is accepted by the FA: ");
        String sequence = scanner.nextLine();
        if (fa.isAcceptedSequence(sequence)) {
            System.out.println("Sequence is accepted by the FA.");
        } else {
            System.out.println("Sequence is not accepted by the FA.");
        }
    }
}
