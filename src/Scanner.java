import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    int stPos;

    SymbolTable st;

    PIF pif;

    public Scanner() {
        this.stPos = 1;
        this.st = new SymbolTable();
    }

    /*
    2-13 = separators
    14-23 = operators
    24-37 = reserved words
    */
    List<String> tokens = readTokens();
    List<String> separators = readTokensElements(2, 13);
    List<String> operators = readTokensElements(14, 23);
    List<String> reservedWords = readTokensElements(24, 37);


    public void scan(String file) throws IOException {
        String fileContent = readFile(file);
        int index = 0;
        Tupple tupple;
        while (index < fileContent.length()) {
            if (String.valueOf(fileContent.charAt(index)).equals(" ")) {
                index++;
            }
            tupple = scanToken(index, fileContent);
            System.out.println(tupple.x + "  " + tupple.y);
            if(separators.contains(tupple.x) || operators.contains(tupple.x) || reservedWords.contains(tupple.x)) {
                //PIFEntry pifEntry = new PIFEntry(tokens.indexOf(tupple.x), );
            } else {
                TuppleDAO tuppleDAO = new TuppleDAO(tupple.x, tupple.y);
                st.insert(tuppleDAO);
                //PIFEntry pifEntry = new PIFEntry();
            }
            index = Integer.parseInt(tupple.i) + 1;
            if (String.valueOf(fileContent.charAt(index)).equals("\n")) {
                index++;
            }
        }
    }

    public Tupple scanToken(int index, String file) {
        int aux = index;
        String str = "";

        if (aux - 1 > 0 && String.valueOf(file.charAt(aux - 1)).equals("#")) {
            while (!String.valueOf(file.charAt(aux)).equals("#")) {
                str += file.charAt(aux);
                aux++;
            }
            return new Tupple(str, String.valueOf(this.stPos++), String.valueOf(aux - 1));
        } else {

            if (separators.contains(String.valueOf(file.charAt(aux)))) {
                return new Tupple(String.valueOf(file.charAt(aux)), "-2", String.valueOf(aux));
            }

            if (operators.contains((String.valueOf(file.charAt(aux))))) {
                return new Tupple(String.valueOf(file.charAt(aux)), "-1", String.valueOf(aux));
            }

            while (!String.valueOf(file.charAt(aux)).equals(" ") &&
                    !separators.contains(String.valueOf(file.charAt(aux))) &&
                    !operators.contains((String.valueOf(file.charAt(aux))))) {

                str += (file.charAt(aux));
                aux++;
            }

            if (reservedWords.contains(str)) {
                return new Tupple(str, "0", String.valueOf(aux - 1));
            }
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
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\andya\\Desktop\\lab3\\src\\token.in"))) {
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
