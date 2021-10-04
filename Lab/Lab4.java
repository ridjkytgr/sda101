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

    public Lantai(String theElement) {
        this(theElement, null, null);
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
        size = 1;
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
        Lantai lantaiBangun = new Lantai(input);

        if (pointerInt == size && this.pointer != null) { // Agen paling atas
            // Arahkan next ke node baru yang telah dibuat
            this.pointer.setNext(lantaiBangun);
            lantaiBangun.setBefore(this.pointer);

            // Pindahkan pointer
            this.pointer = lantaiBangun;
            this.last = lantaiBangun;

            pointerInt++;
        } else if (pointerInt != size && this.pointer != null) { // Agen di tengah
            // Mengarahkan yang baru agar tidak melepas referensi
            lantaiBangun.setNext(this.pointer.getNext());
            this.pointer.getNext().setBefore(lantaiBangun);

            // Memindahkan pointer
            this.pointer.setNext(lantaiBangun);
            lantaiBangun.setBefore(this.pointer);

            // Memindahkan pointer
            this.pointer = lantaiBangun;

            pointerInt++;
        }

        // Tambah panjang dari linked list
        size++;
    }

    public String lift(String input) {
        if (this.pointer != null) {
            if (size == 1) { // Jika hanya 1 lantai
                return this.pointer.getValue();
            } else {
                // Memindahkan pointer jika
                if (input.equals("BAWAH")) {
                    if (pointerInt == 1) { // Jika agen ada di paling bawah
                        return this.pointer.getValue();
                    } else {
                        // Memindahkan pointer ke bawah
                        this.pointer = this.pointer.getBefore();
                        pointerInt--;

                        return this.pointer.getValue();
                    }
                } else { // Jika naik ke atas
                    if (pointerInt == size) { // Jika agen ada di paling atas
                        return this.pointer.getValue();
                    } else {
                        // Memindahkan pointer ke atas
                        this.pointer = this.pointer.getNext();
                        pointerInt++;

                        return this.pointer.getValue();
                    }
                }
            }
        }
        return "";
    }

    public String hancurkan() {
        if (this.pointer != null) {
            // Untuk dicetak
            Lantai lantaiDihancurkan = this.pointer;

            // Pindahkan pointer ke bawah
            if (size > 1) {
                if (pointerInt == size) { // Agen di paling atas
                    Lantai beforeBaru = this.pointer.getBefore();

                    this.pointer = null;
                    this.pointer = beforeBaru;
                    this.last = this.pointer;

                    pointerInt--;
                } else if (pointerInt == 1) { // Agen di paling bawah
                    this.first = this.pointer.getNext();
                    this.pointer = null;
                    this.pointer = this.first;

                    pointerInt = 1;
                } else { // Agen di tengah
                    Lantai nextBaru = this.pointer.getNext();

                    // Agar tidak hilang referensi
                    this.pointer.getBefore().setNext(nextBaru);
                    nextBaru.setBefore(this.pointer.getBefore());

                    this.pointer = this.pointer.getBefore();

                    pointerInt--;
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
        return "";

    }

    public void timpa(Gedung gedungNimpa) {
        // Menyambungkan pointer
        if (this.last != null && gedungNimpa.getFirst() != null) {
            this.last.setNext(gedungNimpa.getFirst());
            gedungNimpa.getFirst().setBefore(this.last);

            // Pindahkan last ke paling atas
            this.last = gedungNimpa.getLast();

            // Menambahkan size gedung
            size += gedungNimpa.size();
        }
    }

    public String sketsa() {
        StringBuilder sketsa = new StringBuilder("");

        Lantai first = this.first;
        for (int i = 0; i < size; i++) {
            if (first != null) {
                // Append stringbuilder
                sketsa.append(first.getValue());

                // Memindahkan pointer
                first = first.getNext();
            }
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

                String output = seluruhGedung.get(A).lift(X);

                out.println(output);

            } else if (cmd.equals("SKETSA")) {
                String A = in.next();

                String output = seluruhGedung.get(A).sketsa();

                out.println(output);

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

                String output = seluruhGedung.get(A).hancurkan();

                out.println(output);
            }
        }

        // don't forget to close/flush the output
        out.flush();
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