import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP3Ver2 {
    private static InputReader in;
    public static PrintWriter out;
    public static Karyawan[] dataKaryawan = new Karyawan[0];
    public static int banyakRentan = 0;
    public static boolean bossStatus = false;
    public static int[] peringkatMax = new int[0]; // Untuk menyimpan peringkat max dari network karyawan ke-i.

    /**
     * Handler query BOSS.
     * 
     * @param graf         Graf yang digunakan
     * @param karyawanKe   karyawan yang digunakan sebagai root untuk DFS
     * @param peringkatMax Array untuk menyimpan BOSS dari masing-masing karyawan
     */
    public static void bossHandler(Graph graf, Karyawan karyawanKe, int[] peringkatMax) {
        peringkatMaxFill(graf, karyawanKe, peringkatMax);
        // Lakukan pengecekan network lain (Ganti pake while loop biar starting
        // point-nya ga dari awal)

        int counterArray = 0;
        while (counterArray < peringkatMax.length) {
            if (peringkatMax[counterArray] == -1) {
                peringkatMaxFill(graf, dataKaryawan[counterArray], peringkatMax);
            }
            counterArray++;
        }
    }

    /**
     * Method untuk mengisi array BOSS dari masing-masing karyawan.
     * 
     * @param graf         Graf yang digunakan
     * @param karyawanKe   karyawan yang digunakan sebagai root untuk DFS
     * @param peringkatMax Array untuk menyimpan BOSS dari masing-masing karyawan
     */
    public static void peringkatMaxFill(Graph graf, Karyawan karyawanKe, int[] peringkatMax) {

        // Lakukan pengecekan suatu network dan dapatkan ArrayList yang berisi peringkat
        // terurut.
        List<Karyawan> tempNetwork = graf.dfsUsingStack(karyawanKe);

        // Jika tidak memiliki teman, maka keluar dari function dan set peringkatMax
        // menjadi 0.
        if (tempNetwork.size() == 1) { // Tidak memiliki temen (dirinya sendiri)
            peringkatMax[karyawanKe.getIdentitas() - 1] = 0;
        } else if (tempNetwork.size() >= 2) {
            // Menyimpan maksimum yang paling tinggi.
            int realMax = tempNetwork.get(tempNetwork.size() - 1).getPangkat();

            // Menyimpan maksimum yang sebelumnya (untuk anggota yang memiliki pangkat max)
            int fakeMax = tempNetwork.get(tempNetwork.size() - 2).getPangkat();

            // Isi array yang menyimpan pangkat BOSS dari karyawan ke-i.
            for (int i = 0; i < tempNetwork.size(); i++) {
                int indexKaryawan = tempNetwork.get(i).getIdentitas() - 1;

                if (dataKaryawan[indexKaryawan].getPangkat() != realMax) {
                    peringkatMax[indexKaryawan] = realMax; // Masukkan pangkat tertinggi pada network.
                } else {
                    peringkatMax[indexKaryawan] = fakeMax; // Ini jika dirinya sendiri adalah pangkat tertinggi.
                }
            }
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        /**
         * input
         */

        // Banyak karyawan.
        int n = in.nextInt();

        // Banyak pertemanan.
        int m = in.nextInt();

        // Banyak kejadian.
        int q = in.nextInt();

        Graph graf = new Graph(n);
        dataKaryawan = new Karyawan[n];
        peringkatMax = new int[n];
        // Initialize pangkat-pangkat karyawan
        for (int i = 0; i < n; i++) {
            int pangkatKaryawan = in.nextInt();

            dataKaryawan[i] = new Karyawan(i + 1, pangkatKaryawan);
            peringkatMax[i] = -1;
        }

        // Hubungan antarkaryawwan
        for (int i = 0; i < m; i++) {
            int m1 = in.nextInt();
            int m2 = in.nextInt();

            graf.addEdge(dataKaryawan[m1 - 1], dataKaryawan[m2 - 1]);
        }

        // Kejadian
        for (int i = 0; i < q; i++) {
            int kode = in.nextInt();

            if (kode == 1) { // Tambah
                int u = in.nextInt();
                int v = in.nextInt();

                graf.addEdge(dataKaryawan[u - 1], dataKaryawan[v - 1]);
            } else if (kode == 2) { // Resign
                int u = in.nextInt();

                graf.resign(dataKaryawan[u - 1]);
            } else if (kode == 3) { // Carry
                int u = in.nextInt();

                out.println(graf.findMax(u));
            } else if (kode == 4) { // Boss
                int u = in.nextInt();

                if (bossStatus == false) {
                    bossHandler(graf, dataKaryawan[u - 1], peringkatMax);

                    bossStatus = true;

                    out.println(peringkatMax[u - 1]);
                } else {
                    out.println(peringkatMax[u - 1]);
                }
            } else if (kode == 5) { // Sebar

            } else if (kode == 6) { // Simulasi

            } else if (kode == 7) { // Networking
                out.println(0);
            }
        }
        out.flush();
    }

    static class Graph {
        private int V; // No. of vertices
        private List<Karyawan>[] adj; // Adjacency Lists
        private List<Karyawan> tempNetwork;

        // Constructor
        Graph(int v) {
            V = v;
            adj = new ArrayList[v];
            for (int i = 0; i < v; i++)
                adj[i] = new ArrayList();
        }

        /**
         * Membuat sorted adjacency list.
         * 
         * @param v Karyawan yang akan berteman dengan karyawan w.
         * @param w Karyawan yang akan berteman dengan karyawan v.
         */
        void addEdge(Karyawan v, Karyawan w) {
            int identitasV = v.getIdentitas();
            int identitasW = w.getIdentitas();

            addSortedWithBinSer(adj[identitasV - 1], w);
            addSortedWithBinSer(adj[identitasW - 1], v);
        }

        /**
         * Mengembalikan teman karyawan dengan pangkat tertinggi.
         * 
         * @param karyawanKe karyawan yang ingin dicari teman dengan pangkat
         *                   tertingginya.
         * @return objek karyawan yang memiliki pangkat tertinggi.
         */
        int findMax(int karyawanKe) {
            if (adj[karyawanKe - 1].size() == 0) {
                return 0;
            }
            // Ambil index terakhir karena udah sorted.
            return adj[karyawanKe - 1].get(adj[karyawanKe - 1].size() - 1).getPangkat();
        }

        /**
         * Mengembalikan teman karyawan dengan pangkat terendah.
         * 
         * @param karyawanKe karyawan yang ingin dicari teman dengan pangkat
         *                   tertingginya.
         * @return objek karyawan yang memiliki pangkat tertinggi.
         */
        int findMin(int karyawanKe) {
            if (adj[karyawanKe - 1].size() == 0) {
                return 0;
            }
            return adj[karyawanKe - 1].get(0).getPangkat();
        }

        /**
         * Memasukkan elemen ke dalam arraylist agar tetap menjaga urutannya.
         * 
         * @param adjList ArrayList yang ingin dimasukkan elemen.
         * @param x       Elemen yang akan dimasukkan.
         */
        void addSortedWithBinSer(List<Karyawan> adjList, Karyawan x) {
            int left = 0, right = adjList.size() - 1;
            int mid = 0;

            while (left <= right) {
                mid = left + (right - left) / 2;

                // Check if x is present at mid
                if (adjList.get(mid).getPangkat() == x.getPangkat()) {
                    adjList.add(mid, x);
                    break;
                }

                // If x greater, ignore left half
                if (adjList.get(mid).getPangkat() < x.getPangkat())
                    left = mid + 1;

                // If x is smaller, ignore right half
                else
                    right = mid - 1;
            }

            // Memasukkan element ke tempat yang cocok.
            if (left == adjList.size()) {
                adjList.add(x);
            } else if (left >= mid) { // Memasukkan elemen di kanan mid.
                adjList.add(left, x);
            } else if (right == -1) { // Memasukkan elemen di paling kiri.
                adjList.add(0, x);
            } else if (right <= mid) { // Memasukkan elemen di kiri mid.
                adjList.add(right, x);
            }
        }

        /**
         * Method untuk query RESIGN.
         * 
         * @param karyawanKe Karyawan yang ingin di-resign.
         */
        void resign(Karyawan karyawanKe) {
            int indexKaryawanDihapus = karyawanKe.getIdentitas() - 1;
            List<Karyawan> adjRemoved = adj[indexKaryawanDihapus];

            for (int i = 0; i < adjRemoved.size(); i++) { // Menghapus kemunculan node yang di-resign pada adjlist
                                                          // tetangganya.
                List<Karyawan> adjTetangga = adj[adjRemoved.get(i).getIdentitas() - 1];
                int indexFound = binarySearch(adjTetangga, karyawanKe);
                adjTetangga.remove(indexFound);
            }

        }

        int binarySearch(List<Karyawan> arr, Karyawan x) {
            int left = 0, right = arr.size() - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                // Check if x is present at mid
                if (arr.get(mid).getPangkat() == x.getPangkat())
                    return mid;

                // If x greater, ignore left half
                if (arr.get(mid).getPangkat() < x.getPangkat())
                    left = mid + 1;

                // If x is smaller, ignore right half
                else
                    right = mid - 1;
            }

            // if we reach here, then element was
            // not present
            return -1;
        }

        /**
         * https://java2blog.com/depth-first-search-in-java/
         * Traversal dengan menggunakan DFS sekaligus menyimpan 1 network dalam bentuk
         * sorted ArrayList.
         * 
         * @param karyawan Root dari DFS.
         * @return ArrayList yang berisi 1 network sorted ArrayList.
         */
        List<Karyawan> dfsUsingStack(Karyawan karyawan) {
            // Arraylist sementara untuk menampung peringkat-peringkat dari suatu network
            // (sorted)
            if (adj[karyawan.getIdentitas() - 1].size() == 0) { // Mengembalikan arraylist karyawan yang berisi pangkat
                                                                // 0 untuk dipanggil di atas.
                return new ArrayList<Karyawan>(Arrays.asList(new Karyawan(0, 0)));
            }

            tempNetwork = new ArrayList<Karyawan>();

            Stack<Karyawan> stack = new Stack<Karyawan>();
            if (!karyawan.getIsVisited()) {
                stack.add(karyawan);
            }

            while (!stack.isEmpty()) {
                Karyawan element = stack.pop();
                int indexElement = element.getIdentitas() - 1;

                if (!element.getIsVisited()) {
                    // Set visited as true
                    element.toggleIsVisited();

                    // Masukkan ke dalam arraylist network.
                    addSortedWithBinSer(tempNetwork, element);
                }

                List<Karyawan> neighbours = adj[indexElement];
                for (int i = 0; i < neighbours.size(); i++) {
                    Karyawan n = neighbours.get(i);
                    if (n != null && !n.getIsVisited()) {
                        stack.add(n);
                    }
                }
            }
            return tempNetwork;
        }

        void printAdjList(int karyawanKe) {
            for (int i = 0; i < adj[karyawanKe - 1].size(); i++) {
                out.println(adj[karyawanKe - 1].get(i).getPangkat());
            }
        }
    }

    static class Karyawan {
        private int identitas;
        private int pangkat;

        /**
         * Flags
         */
        private boolean isRentan;
        private boolean isVisited;
        private boolean isResigned;

        public Karyawan(int identitas, int pangkat) {
            this.identitas = identitas;
            this.pangkat = pangkat;
            this.isRentan = false;
            this.isVisited = false;
            this.isResigned = false;
        }

        public int getIdentitas() {
            return this.identitas;
        }

        public int getPangkat() {
            return this.pangkat;
        }

        public boolean getIsVisited() {
            return this.isVisited;
        }

        public boolean getIsResigned() {
            return this.isResigned;
        }

        public void toggleIsResigned() {
            this.isResigned = true;
        }

        public void toggleIsVisited() {
            this.isVisited = true;
        }

        public void setPangkat(int newPangkat) {
            this.pangkat = newPangkat;
        }
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
