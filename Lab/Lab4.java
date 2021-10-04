import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

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

class Gedung {
    Lantai first;
    Lantai last;
    Lantai pointer;
    int size;
    int pointerInt;

    // Constructors
    public Gedung() {
        // Gimmick aja buat masukin ke hashmap pas fondasi B1.
        this(null);
    }

    public Gedung(Lantai first) { // Bakal dipanggil kalo pengecekan hashmap pertama ternyata this.first == null.
        this.first = first;
        this.last = first;
        this.pointer = first;
        size++;
        pointerInt = 1;
    }

    // Getter
    public Lantai getFirst() {
        return this.first;
    }

    public Lantai getLast() {
        return this.last;
    }

    public Lantai getPointer() {
        return this.pointer;
    }

    public int getPointerInt() {
        return this.pointerInt;
    }

    public int size() {
        return this.size;
    }

    // Questions
    public void bangun(String input) {
        // Instantiate lantai baru (Node) (Nilai before adalah pointer sebelumnya)
        Lantai lantaiBangun = new Lantai(input, this.last);

        if (pointerInt == size) {
            // Arahkan next ke node baru yang telah dibuat
            this.pointer.setNext(lantaiBangun);

            // Pindahkan pointer
            this.pointer = lantaiBangun;
            this.last = lantaiBangun;

            pointerInt++;
        } else {
            // Mengarahkan yang baru agar tidak melepas referensi
            lantaiBangun.setNext(this.pointer.getNext());

            this.pointer.setNext(lantaiBangun);

            // Memindahkan pointer
            this.pointer = lantaiBangun;

            pointerInt++;
        }

        // Tambah panjang dari linked list
        size++;
    }

    public String lift(String input) {
        // Memindahkan pointer jika
        if (input.equals("BAWAH")) {
            // Memindahkan pointer ke bawah
            this.pointer = this.pointer.getBefore();
            pointerInt--;
        } else {
            // Memindahkan pointer ke atas
            this.pointer = this.pointer.getNext();
            pointerInt++;
        }

        // Mencetak letak pointer
        return this.pointer.getValue();
    }

    public String hancurkan() {
        // Untuk dicetak
        Lantai lantaiDihancurkan = this.pointer;

        // Pindahkan pointer ke bawah
        if (size > 1) {
            if (pointerInt == size) {
                this.pointer = this.pointer.getBefore();
                this.pointer.setNext(null);
                this.last = this.pointer;
            } else {
                Lantai nextBaru = this.pointer.getNext();

                // Agar tidak hilang referensi
                this.pointer.getBefore().setNext(nextBaru);
                this.pointer = this.pointer.getBefore();
            }
        } else {
            this.first = null;
            this.last = null;
            this.pointer = null;
        }

        size--;

        // Pencetakan
        return lantaiDihancurkan.getValue();

    }

    public void timpa(Gedung gedungNimpa) {
        // Menyambungkan pointer
        this.last.setNext(gedungNimpa.getFirst());
        gedungNimpa.getFirst().setBefore(this.last);

        // Menambahkan size gedung
        size += gedungNimpa.size();
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

                out.println(seluruhGedung.get(A).lift(X));

            } else if (cmd.equals("SKETSA")) {
                String A = in.next();
                out.println(seluruhGedung.get(A).sketsa());

            } else if (cmd.equals("TIMPA")) {
                String A = in.next();
                String B = in.next();

                /**
                 * Memindahkan last next A ke first B, dan before first B ke last A.
                 */
                seluruhGedung.get(A).timpa(seluruhGedung.get(B));

                // Menghapus gedung B
                seluruhGedung.remove(B);

            } else if (cmd.equals("HANCURKAN")) {
                String A = in.next();

                out.println(seluruhGedung.get(A).hancurkan());
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