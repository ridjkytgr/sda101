
/**
 * Nama: Ridjky Tegar Perkasa (2006525330)
 * Referensi: Sabyna Maharani
 */

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Dataran {
    String namaKuil;
    long tinggi;
    Dataran next;

    // Kuil kalau element first
    boolean isKuil;

    // Untuk doubly linked list
    Dataran previous;

    // Constructors
    public Dataran(String namaKuil, long tinggi, Dataran previous, Dataran next) {
        this.namaKuil = namaKuil;
        this.tinggi = tinggi;
        this.previous = previous;
        this.next = next;
    }

    public Dataran(String namaKuil, long tinggi) {
        this(namaKuil, tinggi, null, null);
    }

    public Dataran(long tinggi) {
        this(null, tinggi, null, null);
    }

    public Dataran(String namaKuil) {
        this(namaKuil, 0, null, null);
    }

    public Dataran() {
        this(null, 0, null, null);
    }

    // Getter
    public String getNamaKuil() {
        return this.namaKuil;
    }

    public long getTinggi() {
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

    public void setTinggi(long tinggiBaru) {
        this.tinggi = tinggiBaru;
    }
}

class Pulau {
    Dataran first;
    Dataran last;
    Dataran raiden;
    int size;

    // Nested linked list
    Pulau next;
    Pulau previous;

    // Constructors
    public Pulau() {
        this.first = null;
        this.last = null;
        this.raiden = null;
        size = 0;
    }

    public Pulau(Dataran first) { // Bakal dipanggil kalo pengecekan hashmap pertama ternyata this.first == null.
        // first.setIsKuil(true);
        this.first = first;
        this.last = first;
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

    public Pulau getNextPulau() {
        return this.next;
    }

    public Pulau getPreviousPulau() {
        return this.previous;
    }

    // Setter
    public void setSize(int size) {
        this.size = size;
    }

    public void setNextPulau(Pulau pulau) {
        this.next = pulau;
    }

    public void setPreviousPulau(Pulau pulau) {
        this.previous = pulau;
    }

    public void setLast(Dataran newLast) {
        this.last = newLast;
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
            pulauBangun.setIsKuil(true);
            this.first = pulauBangun;
            this.last = pulauBangun;
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
        }
    }

    public StringBuilder pisah(Pulau pulauBesar) {
        StringBuilder sb = new StringBuilder();

        // Current pointer (kuil kanan)
        Dataran traverseKuil2 = this.first;
        // Current pointer (kuil kiri)
        Dataran traverseKuil1 = this.first.getPrevious();

        // Untuk memotong linked list
        traverseKuil2.setPrevious(null);
        traverseKuil1.setNext(null);

        // Untuk dicetak
        sb.append(pulauBesar.size());
        sb.append(" ");
        sb.append(this.size());

        return sb;
    }

    public void inisiasiRaiden(int s) {
        this.raiden = this.first;
        if (s >= 2) {
            for (int i = 0; i < s - 1; i++) {
                this.raiden = this.raiden.getNext();
            }
        }
    }

    public long gerakKiri(int s) {
        for (int step = 0; step < s; step++) {
            // Jika sudah berada di paling kiri.
            if (this.raiden.getPrevious() == null) {
                break;
            }
            this.raiden = this.raiden.getPrevious();
        }
        return this.raiden.getTinggi();
    }

    public long gerakKanan(int s) {
        for (int step = 0; step < s; step++) {
            // Jika sudah berada di paling kanan.
            if (this.raiden.getNext() == null) {
                break;
            }

            this.raiden = this.raiden.getNext();
        }
        return this.raiden.getTinggi();
    }

    public long teleportasi() {
        this.raiden = this.getFirst();

        return this.raiden.getTinggi();
    }

    public long tebasKiri(int s) {
        Dataran raidenSebelumnya = this.raiden;

        long tinggiSebelumnya = this.raiden.getTinggi();

        // Jika ke kiri tidak ada yg sama lagi.
        Dataran temp = this.raiden;

        while (s > 0 && this.raiden.getPrevious() != null) {
            this.raiden = this.raiden.getPrevious();

            if (this.raiden.getTinggi() == tinggiSebelumnya) {
                temp = this.raiden;
                s--;
            }
        }

        // Pindahkan pointer raiden.
        this.raiden = temp;

        if (raidenSebelumnya.equals(temp)) { // Jika tidak berpindah tempat
            return 0;
        }

        return temp.getNext().getTinggi();

    }

    public long tebasKanan(int s) {
        Dataran raidenSebelumnya = this.raiden;

        long tinggiSebelumnya = this.raiden.getTinggi();

        // Jika ke kanan tidak ada yg sama lagi.
        Dataran temp = this.raiden;

        while (s > 0 && this.raiden.getNext() != null) {
            this.raiden = this.raiden.getNext();

            if (this.raiden.getTinggi() == tinggiSebelumnya) {
                temp = this.raiden; // Jika ke kanan tidak ada yg sama lagi.
                s--;
            }
        }

        // Pindahkan pointer raiden.
        this.raiden = temp;

        if (raidenSebelumnya.equals(temp)) { // Jika tidak berpindah tempat
            return 0;
        }

        return temp.getPrevious().getTinggi();
    }

    public long crumble() {
        long tinggiDihancurkan = this.raiden.getTinggi();

        // Hilangkan daratan lalu pindahkan raiden ke kiri.
        if (this.raiden.getIsKuil()) { // Kalo kuil langsung return 0.
            return 0;
        }

        if (this.raiden.getNext() == null) { // Jika raiden ada di Dataran paling kanan
            Dataran beforeBaru = this.raiden.getPrevious();

            // Motong linked list
            beforeBaru.setNext(null);
            this.raiden.setPrevious(null);

            this.raiden = beforeBaru;
            this.last = this.raiden;
        } else { // Kalo raiden ada di tengah.
            Dataran nextBaru = this.raiden.getNext();

            // Agar tidak kehilangan referensi (kiri dataran baru)
            this.raiden.getPrevious().setNext(nextBaru);
            nextBaru.setPrevious(this.raiden.getPrevious());

            // Memindahkan raiden ke belakang
            this.raiden = this.raiden.getPrevious();
        }

        this.size--;

        return tinggiDihancurkan;
    }

    public long stabilize() {
        if (this.raiden.getIsKuil()) {
            return 0;
        }

        // Menyimpan tinggi dataran yang lebih rendah ke variabel x
        long x = this.raiden.getTinggi() > this.raiden.getPrevious().getTinggi() ? this.raiden.getPrevious().getTinggi()
                : this.raiden.getTinggi();

        // Bikin dataran baru yang memiliki tinggi lebih rendah.
        Dataran dataranStabilize = new Dataran(x);

        if (this.raiden.getNext() == null) { // Raiden shogun di paling kanan
            // Mengarahkan next dan previous.
            this.raiden.setNext(dataranStabilize);
            dataranStabilize.setPrevious(this.raiden);

            // Memindahkan last.
            this.last = dataranStabilize;
        } else { // Raiden shogun di tengah (ga mungkin pertama karena kuil)
            // Mengarahkan next dan previous agar tidak hilang (sisi kanan Dataran baru)
            dataranStabilize.setNext(this.raiden.getNext());
            this.raiden.getNext().setPrevious(dataranStabilize);

            // Mengarahkan next dan previous agar tidak hilang (sisi kiri Dataran baru)
            this.raiden.setNext(dataranStabilize);
            dataranStabilize.setPrevious(this.raiden);
        }

        this.size++;

        return dataranStabilize.getTinggi();
    }

    public int rise(int h, int x) {
        Dataran current = this.first;

        int count = 0;
        while (current.getNext() != null) {
            if (current.getTinggi() > h) {
                count++;
                current.setTinggi(current.getTinggi() + x);
            }
            current = current.getNext();
        }

        if (current.getNext() == null && current.getTinggi() > h) {
            count++;
            current.setTinggi(current.getTinggi() + x);
        }

        return count;
    }

    public int quake(int h, int x) {
        Dataran current = this.first;

        int count = 0;
        while (current.getNext() != null) {
            if (current.getTinggi() < h) {
                count++;
                current.setTinggi(current.getTinggi() - x);
            }
            current = current.getNext();
        }

        if (current.getNext() == null && current.getTinggi() < h) {
            count++;
            current.setTinggi(current.getTinggi() - x);
        }

        return count;
    }

    public int sweeping(int l) {
        Dataran current = this.first;

        int count = 0;
        while (current.getNext() != null) {
            if (current.getTinggi() < l) {
                count++;
            }
            current = current.getNext();
        }

        if (current.getNext() == null && current.getTinggi() < l) {
            count++;
        }

        return count;
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
            seluruhKuil.put(namaPulau, newPulau);
        }

        // Urusan pointer Raiden
        String pulauRaiden = in.next();
        int dataranRaiden = in.nextInt();

        // Menyimpan posisi Raiden jika nanti melakukan teleportasi.
        String currentRaiden = pulauRaiden;

        // Mencari pulau tempat Raiden.
        Pulau tempatRaiden = seluruhKuil.get(currentRaiden);

        // Menginisiasi pointer Raiden yang mulanya di first.
        tempatRaiden.inisiasiRaiden(dataranRaiden);

        // Command dari Archons
        int banyakCommand = in.nextInt();
        for (int command = 0; command < banyakCommand; command++) {
            String cmd = in.next();

            if (cmd.equals("UNIFIKASI")) {
                String pulauU = in.next();
                String pulauV = in.next();

                // Lakukan proses unifikasi
                seluruhKuil.get(pulauU).unifikasi(seluruhKuil.get(pulauV));

                // Membuat nested LinkedList
                Pulau current = seluruhKuil.get(pulauU);

                // Jika sebelumnya sudah pernah melakukan unifikasi.
                if (current.getNextPulau() != null) {
                    while (current.getNextPulau() != null) {
                        // Tambahkan size dari kuil yang dependen
                        current.setSize(current.size() + seluruhKuil.get(pulauV).size());

                        // Mengarahkan last.
                        current.setLast(seluruhKuil.get(pulauV).getLast());
                        current = current.getNextPulau();
                    }
                    // Untuk elemen paling terakhir (karena ini ga masuk pas while loop)
                    current.setSize(current.size() + seluruhKuil.get(pulauV).size());

                    // Mengarahkan last.
                    current.setLast(seluruhKuil.get(pulauV).getLast());
                } else { // Jika sebelumnya belum pernah unifikasi.
                    current.setSize(current.size() + seluruhKuil.get(pulauV).size());
                    current.setLast(seluruhKuil.get(pulauV).getLast());
                }

                current.setNextPulau(seluruhKuil.get(pulauV));
                seluruhKuil.get(pulauV).setPreviousPulau(current);

                // Cetak berapa banyak dataran di pulau baru.
                out.println(seluruhKuil.get(pulauU).size());
            } else if (cmd.equals("PISAH")) {
                String kuilU = in.next();

                // Untuk mencari reference pulau paling kiri (paling besar)
                Pulau current1 = seluruhKuil.get(kuilU);

                // Untuk memindahkan pointer last.
                Dataran lastHelper = seluruhKuil.get(kuilU).getFirst().getPrevious();
                if (current1.getPreviousPulau() != null) {
                    while (current1.getPreviousPulau() != null) {
                        current1 = current1.getPreviousPulau();
                        current1.setSize(current1.size() - seluruhKuil.get(kuilU).size());
                        current1.setLast(lastHelper);
                    }
                }

                // Memotong nested Linked List
                Pulau previousKuil = seluruhKuil.get(kuilU).getPreviousPulau();
                // System.out.println(previousKuil.getFirst().getNamaKuil()); // Debug
                seluruhKuil.get(kuilU).setPreviousPulau(null);
                seluruhKuil.get(previousKuil.getFirst().getNamaKuil()).setNextPulau(null);

                StringBuilder sb = seluruhKuil.get(kuilU).pisah(current1);

                // Cetak hasilnya
                out.println(sb);
            } else if (cmd.equals("GERAK")) {
                String arah = in.next();
                if (arah.equals("KIRI")) {
                    out.println(seluruhKuil.get(currentRaiden).gerakKiri(in.nextInt()));
                } else {
                    out.println(seluruhKuil.get(currentRaiden).gerakKanan(in.nextInt()));
                }
            } else if (cmd.equals("TELEPORTASI")) {
                String kuilTeleportasi = in.next();

                // Reset pointer raiden di pulau sebelumnya.
                seluruhKuil.get(currentRaiden).raiden = null;

                // Pindahkan letak raiden.
                currentRaiden = kuilTeleportasi;

                // Cari kuil tempat Raiden harus diteleportasi
                Pulau kuilRaiden = seluruhKuil.get(kuilTeleportasi);

                out.println(kuilRaiden.teleportasi());
            } else if (cmd.equals("TEBAS")) {
                String arah = in.next();
                if (arah.equals("KIRI")) {
                    out.println(seluruhKuil.get(currentRaiden).tebasKiri(in.nextInt()));
                } else {
                    out.println(seluruhKuil.get(currentRaiden).tebasKanan(in.nextInt()));

                }
            } else if (cmd.equals("CRUMBLE")) {
                out.println(seluruhKuil.get(currentRaiden).crumble());

                // Update pulau yang ada di kiri
                Pulau setSize = seluruhKuil.get(currentRaiden);
                while (setSize.getPreviousPulau() != null) {
                    setSize = setSize.getPreviousPulau();
                    setSize.setSize(setSize.size() - 1);
                }

            } else if (cmd.equals("STABILIZE")) {
                out.println(seluruhKuil.get(currentRaiden).stabilize());

                // Update pulau yang ada di kiri
                Pulau setSize = seluruhKuil.get(currentRaiden);
                while (setSize.getPreviousPulau() != null) {
                    setSize = setSize.getPreviousPulau();
                    setSize.setSize(setSize.size() + 1);
                }
            } else if (cmd.equals("RISE")) {
                String u = in.next();

                int h = in.nextInt();

                int x = in.nextInt();

                out.println(seluruhKuil.get(u).rise(h, x));
            } else if (cmd.equals("QUAKE")) {
                String u = in.next();

                int h = in.nextInt();

                int x = in.nextInt();

                out.println(seluruhKuil.get(u).quake(h, x));
            } else if (cmd.equals("SWEEPING")) {
                String u = in.next();

                int l = in.nextInt();

                out.println(seluruhKuil.get(u).sweeping(l));
            }
        }

        // don't forget to close/flush the output
        out.flush();

    }

    /**
     * Printing LinkedList (for debugging purposes)
     * 
     * @param ll LinkedLIst that wanna be printed
     */
    public static void printLL(Pulau ll) {
        StringBuilder sb = new StringBuilder();

        Dataran current = ll.getFirst();
        while (current != null) {
            sb.append(current.getTinggi());
            sb.append(" ");
            // sb.append("==========\n");
            // if (current.getPrevious() != null) {
            // sb.append("PREVIOUSNYA: " + current.getPrevious().getTinggi() + " " + "\n");
            // } else {
            // sb.append("GA ADA PREV\n");
            // }

            // sb.append("==========\n");
            // if (current.getNext() != null) {
            // sb.append("NEXTNYA: " + current.getNext().getTinggi() + " " + "\n");
            // } else {
            // sb.append("GA ADA NEXT\n");
            // }
            current = current.getNext();
        }

        out.println(sb);
    }

    /**
     * Printing HashMap (for debugging purposes)
     * 
     * @param hm Hashmap that wanna be printed
     */
    public static void printHM(Map<String, Pulau> hm) {
        hm.forEach((key, value) -> {
            out.println(key);
            printLL(value);
            out.println("==============================");
        });
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