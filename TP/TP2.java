import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Dataran {
    String namaKuil;
    int tinggi;
    Dataran next;

    // Kuil kalau element first
    boolean isKuil;

    // Untuk doubly linked list
    Dataran previous;

    // Constructors
    public Dataran(String namaKuil, int tinggi, Dataran previous, Dataran next) {
        this.namaKuil = namaKuil;
        this.tinggi = tinggi;
        this.previous = previous;
        this.next = next;
    }

    public Dataran(String namaKuil, int tinggi) {
        this(namaKuil, tinggi, null, null);
    }

    public Dataran(int tinggi) {
        this(null, tinggi, null, null);
    }

    public Dataran(String namaKuil) {
        this(namaKuil, 0, null, null);
    }

    // Getter
    public String getNamaKuil() {
        return this.namaKuil;
    }

    public int getTinggi() {
        return this.tinggi;
    }

    public Dataran getNext() {
        return this.next;
    }

    public Dataran getPrevious() {
        return this.previous;
    }

    public boolean getIsKuil() {
        return this.isKuil;
    }

    // Setter
    public void setNext(Dataran next) {
        this.next = next;
    }

    public void setPrevious(Dataran previous) {
        this.previous = previous;
    }

    public void setIsKuil(boolean truthValue) {
        this.isKuil = truthValue;
    }
}

class Pulau {
    Dataran first;
    Dataran last;
    Dataran raiden;
    int size;

    // Constructors
    public Pulau() {
        this.first = null;
        this.last = null;
        this.raiden = null;
        size = 0;
    }

    public Pulau(Dataran first) { // Bakal dipanggil kalo pengecekan hashmap pertama ternyata this.first == null.
        this.first = first;
        this.last = first;
        this.raiden = first;
        size = 1;
    }

    // Getter
    public Dataran getFirst() {
        return this.first;
    }

    public Dataran getLast() {
        return this.last;
    }

    public Dataran getRaiden() {
        return this.raiden;
    }

    public int size() {
        return this.size;
    }

    // Setter
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Meng-construct linked list Pulau dari berbagai Dataran.
     * 
     * @param namaKuil Nama kuil pada Dataran tersebut (jika merupakan Dataran yang
     *                 paling kiri)
     * @param tinggi   Tinggi dari Dataran tersebut
     */
    public void bangun(String namaKuil, int tinggi) {
        Dataran pulauBangun;
        // Instantiate Dataran baru (Node) (Nilai before adalah pointer sebelumnya)
        if (this.first == null) { // Untuk Dataran yang paling kiri (memiliki kuil)
            pulauBangun = new Dataran(namaKuil, tinggi);
            this.first = pulauBangun;
            this.last = pulauBangun;
            this.raiden = pulauBangun;
        } else { // Untuk Dataran yang bukan paling kiri (non-kuil)
            pulauBangun = new Dataran(namaKuil, tinggi);

            // Perpanjang linked list
            this.last.setNext(pulauBangun);
            pulauBangun.setPrevious(this.last);
            this.last = pulauBangun;
        }

        // Tambah panjang dari linked list
        this.size++;
    }

    public void unifikasi(Pulau pulauNimpa) {
        // Menyambungkan pointer
        if (this.last != null && pulauNimpa.getFirst() != null) {
            this.last.setNext(pulauNimpa.getFirst());
            pulauNimpa.getFirst().setPrevious(this.last);

            // Pindahkan last ke paling atas
            this.last = pulauNimpa.getLast();

            // Menambahkan size gedung
            this.size += pulauNimpa.size();
        }
    }

    public Pulau[] pisah(String namaKuil) {
        // Current pointer
        Dataran traverse = this.first;

        // Pulau yang berada di kiri kuil U.
        Pulau pulauKiri = new Pulau(traverse);

        // Membentuk pulau kiri
        while (traverse.getNext() != null && !traverse.getNext().getNamaKuil().equals(namaKuil)) {
            traverse = traverse.getNext();
            pulauKiri.bangun(traverse.getNamaKuil(), traverse.getTinggi());
        }

        // Pulau yang berada di kanan kuil U (termasuk kuil U).
        traverse = traverse.getNext();
        Pulau pulauKanan = new Pulau(traverse);

        // Membuat pulau kanan
        while (traverse.getNext() != null) {
            traverse = traverse.getNext();
            pulauKanan.bangun(traverse.getNamaKuil(), traverse.getTinggi());
        }
        return new Pulau[] { pulauKiri, pulauKanan };
    }

    public int gerakKiri(int s) {
        for (int step = 0; step < s; step++) {
            this.raiden = this.raiden.getPrevious();

            // Jika sudah berada di paling kiri.
            if (this.raiden.equals(this.getFirst())) {
                break;
            }
        }
        return this.raiden.getTinggi();
    }

    public int gerakKanan(int s) {
        for (int step = 0; step < s; step++) {
            this.raiden = this.raiden.getNext();

            // Jika sudah berada di paling kanan.
            if (this.raiden.equals(this.getLast())) {
                break;
            }
        }
        return this.raiden.getTinggi();
    }

    public int teleportasi(String namaKuil) {
        while (this.raiden.getNext() != null) {
            if (this.raiden.getNamaKuil().equals(namaKuil)) {
                break;
            }

            this.raiden = this.raiden.getNext();
        }

        return this.raiden.getTinggi();
    }
}

public class TP2 {
    private static InputReader in;
    public static PrintWriter out;
    public static Pulau pulau;

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Struktur data tambahan
        Map<String, Pulau> seluruhPulau = new HashMap<String, Pulau>();
        Map<String, Pulau> seluruhKuil = new HashMap<String, Pulau>();

        // Instantiate pulau-pulau
        int banyakPulau = in.nextInt();

        // Membangun pulau-pulau
        for (int zz = 0; zz < banyakPulau; zz++) {
            int banyakDataran;
            String namaPulau = in.next();

            Pulau newPulau = new Pulau();

            banyakDataran = in.nextInt();

            for (int dataran = 0; dataran < banyakDataran; dataran++) {
                int tinggiDataran = in.nextInt();
                newPulau.bangun(namaPulau, tinggiDataran);
            }

            // Masukkan semua ke dalam hashmap.
            seluruhPulau.put(namaPulau, newPulau);
            seluruhKuil.put(namaPulau, newPulau);
        }

        // Urusan pointer Raiden
        String pulauRaiden = in.next();
        int dataranRaiden = in.nextInt();

        Pulau tempatRaiden = seluruhPulau.get(pulauRaiden);

        // Set pointer raiden
        Dataran raiden = tempatRaiden.getRaiden();
        raiden = tempatRaiden.getFirst();

        if (dataranRaiden >= 2) {
            for (int count = 0; count < dataranRaiden - 1; count++) {
                raiden = raiden.getNext();
            }
        }

        // Command dari Archons
        int banyakCommand = in.nextInt();
        for (int command = 0; command < banyakCommand; command++) {
            String cmd = in.next();

            if (cmd.equals("UNIFIKASI")) {
                String pulauU = in.next();
                String pulauV = in.next();

                // Lakukan proses unifikasi
                seluruhPulau.get(pulauU).unifikasi(seluruhPulau.get(pulauV));

                // Timpa hashmap seluruh pulau dengan pulau yang besar
                seluruhPulau.put(pulauU, seluruhPulau.get(pulauU));

                // Hapus pulau yang kecil
                seluruhPulau.remove(pulauV);

                // Timpa kuil yang kecil jadi pulau yang besar
                seluruhKuil.put(pulauV, seluruhPulau.get(pulauU));

                // Timpa kuil pulau yang besar terhadap gabungan pulau tersebut (unifikasi)
                seluruhKuil.put(pulauU, seluruhPulau.get(pulauU));

                // Cetak berapa banyak dataran di pulau baru.
                out.println(seluruhPulau.get(pulauU).size());
            } else if (cmd.equals("PISAH")) {
                String kuilU = in.next();
                Pulau[] pulauTerpisah = seluruhKuil.get(kuilU).pisah(kuilU);

                // Timpa untuk kuil yang ada di sebelah kiri U.
                seluruhKuil.put(seluruhKuil.get(kuilU).getFirst().getNamaKuil(), pulauTerpisah[0]);
                seluruhPulau.put(seluruhKuil.get(kuilU).getFirst().getNamaKuil(), pulauTerpisah[0]);

                // Timpa untuk kuil yang ada di sebelah kanan U.
                seluruhKuil.put(kuilU, pulauTerpisah[1]);
                seluruhPulau.put(kuilU, pulauTerpisah[1]);

                // Cetak hasilnya
                out.println(pulauTerpisah[0].size() + " " + pulauTerpisah[1].size());
            } else if (cmd.equals("GERAK")) {
                String arah = in.next();
                if (arah.equals("KIRI")) {
                    out.println(tempatRaiden.gerakKiri(in.nextInt()));
                } else {
                    out.println(tempatRaiden.gerakKanan(in.nextInt()));
                }
            } else if (cmd.equals("TELEPORTASI")) {
                String kuilTeleportasi = in.next();

                // Cari kuil tempat Raiden harus diteleportasi
                Pulau kuilRaiden = seluruhKuil.get(kuilTeleportasi);

                out.println(kuilRaiden.teleportasi(kuilTeleportasi));

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