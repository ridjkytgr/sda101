
/**
 * Referensi: Rakha Rayhan Nusyura, Zuhal 'Alimul Hadi, Sabyna Maharani
 */

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
    public static int banyakPede = 0;
    public static boolean bossStatus = false;
    public static boolean simulasiStatus = false;
    public static int[] peringkatMax = new int[0]; // Untuk menyimpan peringkat max dari network karyawan ke-i.
    public static int[] dist = new int[0];
    public static boolean[] flagBFS = new boolean[0];

    // Index 0 = karyawan pangkat 1.
    public static ArrayList<ArrayList<Karyawan>> pangkatSama = new ArrayList<ArrayList<Karyawan>>(0);

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

    public static void simulasiHandler() {
        for (int i = 0; i < dataKaryawan.length; i++) {
            if (dataKaryawan[i].getTemanJago() == 0 && !dataKaryawan[i].getIsResigned()) {
                banyakPede++;
                dataKaryawan[i].setIsPede(true);
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
        dist = new int[n];
        flagBFS = new boolean[n];
        pangkatSama = new ArrayList<ArrayList<Karyawan>>();

        // Mengisi arraylist pangkatsama dengan arraylist kosong.
        for (int i = 0; i < n; i++) {
            pangkatSama.add(new ArrayList<Karyawan>());
        }

        // Initialize pangkat-pangkat karyawan
        for (int i = 0; i < n; i++) {
            int pangkatKaryawan = in.nextInt();

            Karyawan karyawanBaru = new Karyawan(i + 1, pangkatKaryawan);
            dataKaryawan[i] = karyawanBaru;
            peringkatMax[i] = -1;
            dist[i] = -1;
            flagBFS[i] = false;

            // Mengisi arraylist yang berpangkat sama.
            pangkatSama.get(pangkatKaryawan - 1).add(karyawanBaru);
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
                int u = in.nextInt();

                int v = in.nextInt();

                graf.BFSForShortestDistance(dataKaryawan[u - 1], dataKaryawan[v - 1]);

                if (dist[v - 1] != 0 && dist[v - 1] != -1) {
                    out.println(dist[v - 1] - 1); // Exclude node src dan dest.
                } else {
                    out.println(dist[v - 1]); // Jika src == dest atau tidak mungkin mengirimkan pesan.
                }
            } else if (kode == 6) { // Simulasi

                if (simulasiStatus == false) {

                    simulasiHandler();
                    simulasiStatus = true;
                    out.println(banyakPede);
                } else {
                    out.println(banyakPede);
                }

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

            // Menambah teman-teman dengan pangkat yang lebih besar
            Karyawan karyawanV = dataKaryawan[identitasV - 1];
            Karyawan karyawanW = dataKaryawan[identitasW - 1];
            if (karyawanV.getPangkat() <= karyawanW.getPangkat()) {
                karyawanV.incrementTemanJago();
            } else if (karyawanW.getPangkat() <= karyawanV.getPangkat()) {
                karyawanW.incrementTemanJago();
            }

            if (simulasiStatus) {
                if (karyawanV.getIsPede() && karyawanV.getTemanJago() != 0) {
                    banyakPede--;
                    karyawanV.setIsPede(false);
                }

                if (karyawanW.getIsPede() && karyawanW.getTemanJago() != 0) {
                    banyakPede--;
                    karyawanW.setIsPede(false);
                }
            }
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
            Karyawan karyawanDihapus = dataKaryawan[indexKaryawanDihapus];
            karyawanDihapus.toggleIsResigned();
            List<Karyawan> adjRemoved = adj[indexKaryawanDihapus];

            for (int i = 0; i < adjRemoved.size(); i++) { // Menghapus kemunculan node yang di-resign pada adjlist
                                                          // tetangganya.
                Karyawan tetangga = adjRemoved.get(i);
                // Handle untuk simulasi
                if (karyawanDihapus.getPangkat() >= tetangga.getPangkat()) {
                    tetangga.decrementTemanjago();

                    // Jika akhirnya tidak ada teman yang jago
                    if (simulasiStatus) {
                        if (tetangga.getTemanJago() == 0 && !tetangga.getIsPede()) {
                            tetangga.setIsPede(true);
                            banyakPede++;
                        }
                    }
                }

                List<Karyawan> adjTetangga = adj[adjRemoved.get(i).getIdentitas() - 1];
                int indexFound = binarySearch(adjTetangga, karyawanKe);
                adjTetangga.remove(indexFound);
            }

            // Menghapus karyawan yang pede
            if (karyawanDihapus.getIsPede()) {
                banyakPede--;
            }
        }

        int binarySearch(List<Karyawan> arr, Karyawan x) {
            int left = 0, right = arr.size() - 1;
            int mid = 0;

            while (left <= right) {
                mid = left + (right - left) / 2;

                // Check if x is present at mid
                if (arr.get(mid).getPangkat() == x.getPangkat())
                    break;

                // If x greater, ignore left half
                if (arr.get(mid).getPangkat() < x.getPangkat())
                    left = mid + 1;

                // If x is smaller, ignore right half
                else
                    right = mid - 1;
            }

            if (arr.get(mid).getIdentitas() == x.getIdentitas()) { // Jika identitasnya sudah benar
                return mid;
            } else {
                while (mid > 0 && arr.get(mid).getPangkat() == x.getPangkat()
                        && arr.get(mid).getIdentitas() != x.getIdentitas()) { // Cek
                    // ke
                    // kiri
                    mid--;
                }
                while (mid < arr.size() && arr.get(mid).getPangkat() == x.getPangkat()
                        && arr.get(mid).getIdentitas() != x.getIdentitas()) { // Cek
                    // ke
                    // kanan
                    mid++;
                }
            }

            // if we reach here, then element was
            // not present
            return mid;
        }

        /**
         * BFS yang telah disesuaikan untuk menghitung jarak terpendek antar 2 karyawan.
         * 
         * @param adj
         * @param src
         * @param dest
         * @param v
         * @param pred
         * @param dist
         * @return
         */
        void BFSForShortestDistance(Karyawan src, Karyawan dest) {
            Arrays.fill(dist, -1);
            Arrays.fill(flagBFS, false);

            int indexSrc = src.getIdentitas() - 1;
            int indexDest = dest.getIdentitas() - 1;

            // Jika sebar ke diri sendiri
            if (src.equals(dest)) {
                dist[indexSrc] = 0;
                dist[indexDest] = 0;
                return;
            }

            LinkedList<Karyawan> queue = new LinkedList<Karyawan>();

            // now source is first to be visited and
            // distance from source to itself should be 0
            flagBFS[indexSrc] = true;
            dist[indexSrc] = 0;
            queue.add(src);

            // bfs Algorithm
            while (!queue.isEmpty()) {
                Karyawan u = queue.remove();
                int indexU = u.getIdentitas() - 1;
                for (int i = 0; i < adj[indexU].size(); i++) { // Loop adjacency list
                    if (!flagBFS[adj[indexU].get(i).getIdentitas() - 1] && !adj[indexU].get(i).getIsResigned()) {
                        flagBFS[adj[indexU].get(i).getIdentitas() - 1] = true;
                        dist[adj[indexU].get(i).getIdentitas() - 1] = dist[indexU] + 1;
                        queue.add(adj[indexU].get(i));

                        // stopping condition (when we find
                        // our destination)
                        if (adj[indexU].get(i).equals(dest))
                            return;
                    }
                }

                for (int i = 0; i < pangkatSama.get(u.getPangkat() - 1).size(); i++) { // Loop pangkat sama
                    if (!flagBFS[pangkatSama.get(u.getPangkat() - 1).get(i).getIdentitas() - 1]
                            && !pangkatSama.get(u.getPangkat() - 1).get(i).getIsResigned()) {
                        flagBFS[pangkatSama.get(u.getPangkat() - 1).get(i).getIdentitas() - 1] = true;
                        dist[pangkatSama.get(u.getPangkat() - 1).get(i).getIdentitas() - 1] = dist[indexU] + 1;
                        queue.add(pangkatSama.get(u.getPangkat() - 1).get(i));

                        // stopping condition (when we find
                        // our destination)
                        if (pangkatSama.get(u.getPangkat() - 1).get(i).equals(dest))
                            return;
                    }
                }
            }
            return;
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
            if (!karyawan.getIsVisitedDFS()) {
                stack.add(karyawan);
            }

            while (!stack.isEmpty()) {
                Karyawan element = stack.pop();
                int indexElement = element.getIdentitas() - 1;

                if (!element.getIsVisitedDFS()) {
                    // Set visited as true
                    element.toggleIsVisitedDFS();

                    // Masukkan ke dalam arraylist network.
                    addSortedWithBinSer(tempNetwork, element);
                }

                List<Karyawan> neighbours = adj[indexElement];
                for (int i = 0; i < neighbours.size(); i++) {
                    Karyawan n = neighbours.get(i);
                    if (n != null && !n.getIsVisitedDFS()) {
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
        private int temanJago;

        /**
         * Flags
         */
        private boolean isPede;
        private boolean isVisitedDFS;
        private boolean isVisitedBFS;
        private boolean isResigned;

        public Karyawan(int identitas, int pangkat) {
            this.identitas = identitas;
            this.pangkat = pangkat;
            this.isPede = false;
            this.isVisitedDFS = false;
            this.isVisitedBFS = false;
            this.isResigned = false;
            this.temanJago = 0;
        }

        public int getIdentitas() {
            return this.identitas;
        }

        public int getPangkat() {
            return this.pangkat;
        }

        public boolean getIsVisitedDFS() {
            return this.isVisitedDFS;
        }

        public boolean getIsVisitedBFS() {
            return this.isVisitedBFS;
        }

        public boolean getIsResigned() {
            return this.isResigned;
        }

        public boolean getIsPede() {
            return this.isPede;
        }

        public int getTemanJago() {
            return this.temanJago;
        }

        public void incrementTemanJago() {
            this.temanJago++;
        }

        public void decrementTemanjago() {
            this.temanJago--;
        }

        public void setIsPede(boolean newValue) {
            this.isPede = newValue;
        }

        public void toggleIsResigned() {
            this.isResigned = true;
        }

        public void toggleIsVisitedDFS() {
            this.isVisitedDFS = true;
        }

        public void setIsVisitedBFS(boolean newValue) {
            this.isVisitedBFS = newValue;
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
