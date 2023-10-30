import java.util.*;

public class SymbolTable {

    private final int size;
    private final Hashtable<Integer, TuppleDAO> table;

    public SymbolTable() {
        this.size = 100;
        this.table = new Hashtable<>(size);
    }

    public void insert(TuppleDAO value) {
        int key = calculateIndex(value.x);
        table.put(key, value);
    }

    public void delete(String value) {
        int key = calculateIndex(value);
        if (table.containsKey(key)) {
            table.remove(key);
        } else {
            throw new IllegalArgumentException("Key '" + key + "' not found in the symbol table.");
        }
    }

    public int calculateIndex(String value) {
        return Math.abs(value.hashCode()) % size;
    }

    public void lookup(String value) {
        int key = calculateIndex(value);
        if (table.containsKey(key)) {
            table.get(key);
        } else {
            throw new IllegalArgumentException("Key '" + key + "' not found in the symbol table.");
        }
    }

    public Enumeration<Integer> getKeys() {
        return this.table.keys();
    }

    public TuppleDAO getValue(int key) {
        return this.table.get(key);
    }
}
