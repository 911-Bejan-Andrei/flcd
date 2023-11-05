import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner1 = new Scanner();
        Scanner scanner2 = new Scanner();
        Scanner scanner3 = new Scanner();

        StringBuilder p1out = scanner1.scan("p1.txt");
        StringBuilder p2out = scanner2.scan("p2.txt");
        StringBuilder p3out = scanner3.scan("p3.txt");

        FileWriter myWriter1 = new FileWriter("p1out.txt");
        myWriter1.write(p1out.toString());
        myWriter1.close();

        FileWriter myWriter2 = new FileWriter("p2out.txt");
        myWriter2.write(p2out.toString());
        myWriter2.close();

        FileWriter myWriter3 = new FileWriter("p3out.txt");
        myWriter3.write(p3out.toString());
        myWriter3.close();
    }
}
