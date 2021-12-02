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
    public static Karyawan[] dataKaryawan = new Karyawan[100000];
    public static int banyakRentan = 0;

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

        // Initialize pangkat-pangkat karyawan
        for (int i = 0; i < n; i++) {
            int pangkatKaryawan = in.nextInt();

            dataKaryawan[i] = new Karyawan(i + 1, pangkatKaryawan);
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

                out.println(graf.findMax(u));
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
        private ArrayList<Karyawan>[] adj; // Adjacency Lists

        // Constructor
        Graph(int v) {
            V = v;
            adj = new ArrayList[v];
            for (int i = 0; i < v; i++)
                adj[i] = new ArrayList();
        }

        // Function to add an edge into the graph
        void addEdge(Karyawan v, Karyawan w) {
            int identitasV = v.getIdentitas();
            int identitasW = w.getIdentitas();

            addSortedWithBinSer(adj[identitasV - 1], w);
            addSortedWithBinSer(adj[identitasW - 1], v);
        }

        int findMax(int karyawanKe) {
            if (adj[karyawanKe - 1].size() == 0) {
                return 0;
            }
            // Ambil index terakhir karena udah sorted.
            return adj[karyawanKe - 1].get(adj[karyawanKe - 1].size() - 1).getPangkat();
        }

        int findMin(int karyawanKe) {
            if (adj[karyawanKe - 1].size() == 0) {
                return 0;
            }
            return adj[karyawanKe - 1].get(0).getPangkat();
        }

        void addSortedWithBinSer(ArrayList<Karyawan> adjList, Karyawan x) {
            int left = 0, right = adjList.size() - 1;
            int mid = (adjList.size() / 2) + 1;

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
            if (left >= mid) { // Memasukkan elemen di kanan mid.
                adjList.add(left, x);
            } else if (right == -1) { // Memasukkan elemen di paling kiri.
                adjList.add(0, x);
            } else if (right <= mid) { // Memasukkan elemen di kiri mid.
                adjList.add(right, x);
            }
        }

        void resign(Karyawan karyawanKe) {
            int indexKaryawanDihapus = karyawanKe.getIdentitas() - 1;
            ArrayList<Karyawan> adjRemoved = adj[indexKaryawanDihapus];

            for (int i = 0; i < adjRemoved.size(); i++) { // Menghapus kemunculan node yang di-resign pada adjlist
                                                          // tetangganya.
                ArrayList<Karyawan> adjTetangga = adj[adjRemoved.get(i).getIdentitas() - 1];
                int indexFound = binarySearch(adjTetangga, karyawanKe);
                adjTetangga.remove(indexFound);
            }

        }

        // Returns index of x if it is present in arr[],
        // else return -1
        int binarySearch(ArrayList<Karyawan> arr, Karyawan x) {
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

        void printAdjList(int karyawanKe) {
            for (int i = 0; i < adj[karyawanKe - 1].size(); i++) {
                out.println(adj[karyawanKe - 1].get(i).getPangkat());
            }
        }
    }

    static class Karyawan {
        private int identitas;
        private int pangkat;
        private boolean isRentan;

        public Karyawan(int identitas, int pangkat) {
            this.identitas = identitas;
            this.pangkat = pangkat;
        }

        public int getIdentitas() {
            return this.identitas;
        }

        public int getPangkat() {
            return this.pangkat;
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
