import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO - class untuk Lantai
class Lantai {
    String element;
    Lantai next;

    // Untuk doubly linked list
    Lantai before;

    // Constructors
    public Lantai(String theElement, Lantai before, Lantai next) {
        this.element = theElement;
        this.before = before;
        this.next = next;
    }

    public Lantai(String theElement, Lantai before) {
        this(theElement, before, null);
    }

    public Lantai(String theElement) {
        this(theElement, null, null);
    }

    public Lantai() {
        this(null, null, null);
    }

    // Getter
    public String getValue() {
        return this.element;
    }

    public Lantai getNext() {
        return this.next;
    }

    public Lantai getBefore() {
        return this.before;
    }

    // Setter
    public void setNext(Lantai next) {
        this.next = next;
    }

    public void setBefore(Lantai before) {
        this.before = before;
    }

}

// TODO - class untuk Gedung
class Gedung {
    Lantai first;
    Lantai last;
    int size;

    // Constructors
    public Gedung() {
        // Gimmick aja buat masukin ke hashmap pas fondasi B1.
        this(null);
    }

    public Gedung(Lantai first) { // Bakal dipanggil kalo pengecekan hashmap pertama ternyata this.first == null.
        this.first = first;
        this.last = first;
        size++;
    }

    public void bangun(String input) {
        Lantai lantaiBangun;

        // Instantiate lantai baru (Node) (Nilai before adalah pointer sebelumnya)
        lantaiBangun = new Lantai(input, this.last);

        // Arahkan next ke node baru yang telah dibuat
        this.last.setNext(lantaiBangun);

        // Pindahkan pointer
        this.last = lantaiBangun;

        // Tambah panjang dari linked list
        size++;
    }

    public void lift(String input) {
        // Memindahkan pointer jika
        if (input.equals("BAWAH")) {
            // Memindahkan pointer ke bawah
            this.last = this.last.getBefore();
        } else {
            // Memindahkan pointer ke atas
            this.last = this.last.getNext();
        }
    }

    public void hancurkan() {
        // Untuk dicetak
        Lantai lantaiDihancurkan = this.last;

        // Pindahkan pointer ke bawah
        if (size > 1) {
            this.last = this.last.getBefore();
            this.last.setNext(null);
        } else {
            this.last = null;
            this.first = null;
        }

        // Pencetakan
        System.out.println(lantaiDihancurkan.getValue());

        size--;
    }

    public void timpa(String input) {

    }

    public String sketsa() {
        StringBuilder sketsa = new StringBuilder("");

        Lantai first = this.first;
        for (int i = 0; i < size; i++) {
            // Append stringbuilder
            sketsa.append(first.getValue());

            // Memindahkan pointer
            first = first.getNext();
        }

        return sketsa.toString();
    }

    public Lantai getFirst() {
        return this.first;
    }

    public int size() {
        return this.size;
    }

}

public class Lab4 {
    private static InputReader in;
    public static PrintWriter out;
    public static Gedung Gedung;

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Struktur data tambahan
        Map<String, Gedung> seluruhGedung = new HashMap<String, Gedung>();

        // N operations
        int N = in.nextInt();
        String cmd;

        // TODO - handle inputs
        for (int zz = 0; zz < N; zz++) {

            cmd = in.next();

            if (cmd.equals("FONDASI")) {
                String A = in.next();

                // Initialize gedung
                seluruhGedung.put(A, new Gedung());

            } else if (cmd.equals("BANGUN")) {
                String A = in.next();
                String X = in.next();

                // Bangun paling pertama
                if (seluruhGedung.get(A).getFirst() == null) {
                    // Overwrite data di hashmap.
                    seluruhGedung.put(A, new Gedung(new Lantai(X)));
                } else { // Jika sudah ada yang dibangun sebelumnya.
                    seluruhGedung.get(A).bangun(X);
                }

            } else if (cmd.equals("LIFT")) {
                String A = in.next();
                String X = in.next();

                seluruhGedung.get(A).lift(X);

            } else if (cmd.equals("SKETSA")) {
                String A = in.next();
                out.println(seluruhGedung.get(A).sketsa());

            } else if (cmd.equals("TIMPA")) {
                String A = in.next();
                String B = in.next();

                /**
                 * Memindahkan last next A ke first B, dan before first B ke last A.
                 */
                seluruhGedung.get(A).timpa(B);

            } else if (cmd.equals("HANCURKAN")) {
                String A = in.next();

                seluruhGedung.get(A).hancurkan();
            }
        }

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}