import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
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
    0-11 = separators
    12-23 = operators
    24-39 = reserved words
    */
    List<String> tokens = readTokens();
    List<String> separators = readTokensElements(0, 11);
    List<String> operators = readTokensElements(12, 22);
    List<String> reservedWords = readTokensElements(23, 39);

    public StringBuilder scan(String file) throws IOException {
        String fileContent = readFile(file);
        int index = 0;
        Tupple tupple;
        while (index < fileContent.length()) {
            if (String.valueOf(fileContent.charAt(index)).equals(" ")) {
                index++;
            }
            tupple = scanToken(index, fileContent);
            if (String.valueOf(tupple.x.charAt(0)).equals("#")) {
                tupple.x = tupple.x.replace("#", "");

                PIFEntry pifEntry1 = new PIFEntry(tokens.indexOf("#"), -2);
                pif.add(pifEntry1);
                TuppleDAO tuppleDAO = new TuppleDAO(tupple.x, tupple.y);
                st.insert(tuppleDAO);
                PIFEntry pifEntry2 = new PIFEntry(2, Integer.parseInt(tupple.y));
                pif.add(pifEntry2);
                pif.add(pifEntry1);

                tupple.i = String.valueOf(Integer.parseInt(tupple.i) + 1);
            } else if (separators.contains(tupple.x) || operators.contains(tupple.x) || reservedWords.contains(tupple.x)) {
                PIFEntry pifEntry = new PIFEntry(tokens.indexOf(tupple.x), Integer.parseInt(tupple.y));
                pif.add(pifEntry);
            } else { //this is for identifiers and constants to add in st
                TuppleDAO tuppleDAO = new TuppleDAO(tupple.x, tupple.y);
                if (st.lookup(tuppleDAO.x)) {
                    stPos--;
                } else {
                    st.insert(tuppleDAO);
                }
                PIFEntry pifEntry;
                if (tupple.x.matches("[0-9]+")) {
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

        StringBuilder str = new StringBuilder();
        str.append("PIF table [ac] [stPos]:\n");

        for (PIFEntry pifEntry : pif) {
            str.append(pifEntry.atomCode).append(" -> ").append(pifEntry.stPosition);
            str.append("\n");
        }

        List<TuppleDAO> list = st.getValues();

        str.append("\nSymbol table [symbol] [stPos]:\n");

        for (TuppleDAO el : list) {
            str.append(el.x).append(" -> ").append(el.y);
            str.append("\n");
        }

        return str;
    }

    public Tupple scanToken(int index, String file) {
        int aux = index;
        String str = "";

        if (String.valueOf(file.charAt(aux)).equals("#")) {
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

        if (operators.contains(String.valueOf(file.charAt(aux)) + String.valueOf(file.charAt(aux+1)))) {
            return new Tupple((String.valueOf(file.charAt(aux)) + String.valueOf(file.charAt(aux+1))), "-1", String.valueOf(aux + 1));
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
             BufferedReader reader = new BufferedReader(isr)
        ) {
            StringBuilder actualContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                actualContent.append(line);
                actualContent.append('\n');
            }
            fis.close();
            isr.close();
            reader.close();
            return actualContent.toString();
        }
    }


    public List<String> readTokens() {
        List<String> lines = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(
                        new FileReader("token.in")
                )
        ) {
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
