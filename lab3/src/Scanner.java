import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    int stPos;

    SymbolTable st;

    List<PIFEntry> pif;

    public Scanner() {
        this.stPos = 1;
        this.st = new SymbolTable();
        this.pif = new ArrayList<>();
    }

    /*
    0-9 = separators
    10-21 = operators
    22-36 = reserved words
    */
    List<String> tokens = readTokens();
    List<String> separators = readTokensElements(0, 9);
    List<String> operators = readTokensElements(10, 21);
    List<String> reservedWords = readTokensElements(22, 36);


    public void scan(String file) throws IOException {
        String fileContent = readFile(file);
        int index = 0;
        Tupple tupple;
        while (index < fileContent.length()) {
            if (String.valueOf(fileContent.charAt(index)).equals(" ")) {
                index++;
            }
            tupple = scanToken(index, fileContent);
            if(String.valueOf(tupple.x.charAt(0)).equals("#")) {
                tupple.x = tupple.x.replace("#", "");

                PIFEntry pifEntry1 = new PIFEntry(tokens.indexOf("#"), -2);
                pif.add(pifEntry1);
                TuppleDAO tuppleDAO = new TuppleDAO(tupple.x, tupple.y);
                st.insert(tuppleDAO);
                PIFEntry pifEntry2 = new PIFEntry(2, Integer.parseInt(tupple.y));
                pif.add(pifEntry2);
                pif.add(pifEntry1);

                tupple.i = String.valueOf(Integer.parseInt(tupple.i) + 1);
            }
            else if(separators.contains(tupple.x) || operators.contains(tupple.x) || reservedWords.contains(tupple.x)) {
                PIFEntry pifEntry = new PIFEntry(tokens.indexOf(tupple.x), Integer.parseInt(tupple.y));
                pif.add(pifEntry);
            } else { //this is for identifiers and constants to add in st
                TuppleDAO tuppleDAO = new TuppleDAO(tupple.x, tupple.y);
                st.insert(tuppleDAO);
                PIFEntry pifEntry;
                if(tupple.x.matches("[0-9]+")) {
                    pifEntry = new PIFEntry(1, Integer.parseInt(tupple.y));
                } else {
                    pifEntry = new PIFEntry(2, Integer.parseInt(tupple.y));
                }
                pif.add(pifEntry);
            }
            index = Integer.parseInt(tupple.i) + 1;
            if (String.valueOf(fileContent.charAt(index)).equals("\n")) {
                index++;
            }
        }

        System.out.println("Symbol table:\n");
        System.out.println("PIF table:\n");
        System.out.println("[ac]   [stPos]");
        for (PIFEntry pifEntry: pif) {
            System.out.println(pifEntry.atomCode + "        " + pifEntry.stPosition);
        }
        System.out.println("done");
    }

    public Tupple scanToken(int index, String file) {
        int aux = index;
        String str = "";

        if(String.valueOf(file.charAt(aux)).equals("#")) {
            aux++;
            str += "#";
            while (aux < file.length() - 1 && !String.valueOf(file.charAt(aux)).equals("#")) {
                str += file.charAt(aux);
                aux++;
            }
            return new Tupple(str, String.valueOf(this.stPos++), String.valueOf(aux - 1));
        }

            if (separators.contains(String.valueOf(file.charAt(aux)))) {
                return new Tupple(String.valueOf(file.charAt(aux)), "-2", String.valueOf(aux));
            }

            if (operators.contains((String.valueOf(file.charAt(aux))))) {
                return new Tupple(String.valueOf(file.charAt(aux)), "-1", String.valueOf(aux));
            }

            while (aux < file.length() && !String.valueOf(file.charAt(aux)).equals(" ") &&
                    !separators.contains(String.valueOf(file.charAt(aux))) &&
                    !operators.contains((String.valueOf(file.charAt(aux))))) {

                str += (file.charAt(aux));
                aux++;
            }

            if (reservedWords.contains(str)) {
                return new Tupple(str, "0", String.valueOf(aux - 1));
            }

        return new Tupple(str, String.valueOf(this.stPos++), String.valueOf(aux - 1));
    }

    public String readFile(String file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {
            StringBuilder actualContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                actualContent.append(line);
                actualContent.append('\n');
            }
            return actualContent.toString();
        }
    }


    public List<String> readTokens() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\lab3\\src\\token.in"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public List<String> readTokensElements(int i, int j) {
        List<String> lst = new ArrayList<>();
        for (int p = i; p <= j; p++) {
            lst.add(tokens.get(p));
        }
        return lst;
    }
}
