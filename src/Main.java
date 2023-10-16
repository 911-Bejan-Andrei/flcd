import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.insert("x");
        symbolTable.insert("y");

        Enumeration<Integer> keys = symbolTable.getKeys();
        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            System.out.println("Key: " + key + ", Value: " + symbolTable.getValue(key));
        }

        symbolTable.delete("x");

        keys = symbolTable.getKeys();

        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            System.out.println("Key: " + key + ", Value: " + symbolTable.getValue(key));
        }

        try {
            symbolTable.lookup("x");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Output: Key 'x' not found in the symbol table.
        }
    }
}
