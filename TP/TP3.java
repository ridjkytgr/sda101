import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP3 {
    private static InputReader in;
    public static PrintWriter out;
    public static Karyawan[] dataKaryawan = new Karyawan[100000];

    /**
     * Method untuk menambahkan hubungan antara karyawan ke-u dan karyawan ke-v.
     * 
     * @param u karyawan ke-u.
     * @param v karyawan ke-v.
     */
    public static void tambah(Graph graf, int u, int v) {
        graf.getAdj(u).insert(dataKaryawan[v - 1]);
        graf.getAdj(v).insert(dataKaryawan[u - 1]);
    }

    public static void resign(Graph graf, int u) {
        // Mengubah karyawan menjadi resign.
        dataKaryawan[u - 1].setIsResigned(true);

        // Menghapus semua vertex u di tetangganya.
        MaxHeap adjU = graf.getAdj(u);

        adjU.removeAll();
    }

    /**
     * Mencari teman dari u dengan pangkat tertinggi.
     * 
     * @param u karyawan yang akan dicari teman tertingginya.
     * @return pangkat tertinggi dari teman u.
     */
    public static int carry(Graph graf, int u) {
        int extract = Integer.MIN_VALUE;
        if (graf.getAdj(u).getMax() == null || graf.getAdj(u).getMax().getPangkat() <= 0) { // Menandakan kalau u
                                                                                            // belum punya teman.
            return 0;
        }

        if (graf.getAdj(u).getMax().getIsResigned() == true) { // Jika yang max adalah yang resign, maka hapus
            extract = graf.getAdj(u).extractMax();
        }

        if (extract == 0) {
            return 0;
        }

        return graf.getAdj(u).getMax().getPangkat();
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

        // Initialize pangkat-pangkat karyawan
        for (int i = 0; i < n; i++) {
            int pangkatKaryawan = in.nextInt();

            Karyawan karyawanBaru = new Karyawan(i + 1, pangkatKaryawan);

            dataKaryawan[i] = karyawanBaru;
        }

        // Hubungan antarkaryawwan
        for (int i = 0; i < m; i++) {
            int m1 = in.nextInt();
            int m2 = in.nextInt();

            MaxHeap adjList1 = graf.getAdj(m1);
            MaxHeap adjList2 = graf.getAdj(m2);

            // Karena undirected graph.
            adjList1.insert(dataKaryawan[m2 - 1]);
            adjList2.insert(dataKaryawan[m1 - 1]);

        }

        // Kejadian
        for (int i = 0; i < q; i++) {
            int kode = in.nextInt();

            if (kode == 1) { // Tambah
                int u = in.nextInt();
                int v = in.nextInt();

                tambah(graf, u, v);
            } else if (kode == 2) { // Resign
                int u = in.nextInt();

                resign(graf, u);
            } else if (kode == 3) { // Carry
                int u = in.nextInt();

                out.println(carry(graf, u));
            } else if (kode == 4) {
                int u = in.nextInt();

                out.println(carry(graf, u));
            } else if (kode == 5) {

            } else if (kode == 6) {

            } else if (kode == 7) {

            }

        }
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

    /**
     * Reference: https://www.geeksforgeeks.org/min-heap-in-java/
     */

    static class MaxHeap {
        // Member variables of this class
        private Karyawan[] Heap;
        private int size;
        private int maxsize;

        // Initializaing front as static with unity
        private final int FRONT = 1;

        // Constructor of this class
        public MaxHeap(int maxsize) {

            // This keyword refers to current object itself
            this.maxsize = maxsize;
            this.size = 0;

            Heap = new Karyawan[this.maxsize + 1];
            Heap[0] = new Karyawan(0, Integer.MAX_VALUE);
        }

        // Method 1
        // Returning the position of
        // the parent for the node currently
        // at pos
        private int parent(int pos) {
            return pos / 2;
        }

        // Method 2
        // Returning the position of the
        // left child for the node currently at pos
        private int leftChild(int pos) {
            return (2 * pos);
        }

        // Method 3
        // Returning the position of
        // the right child for the node currently
        // at pos
        private int rightChild(int pos) {
            return (2 * pos) + 1;
        }

        // Method 4
        // Returning true if the passed
        // node is a leaf node
        private boolean isLeaf(int pos) {

            if (pos > (size / 2) && pos <= size) {
                return true;
            }

            return false;
        }

        // Method 5
        // To swap two nodes of the heap
        private void swap(int fpos, int spos) {

            Karyawan tmp;
            tmp = Heap[fpos];

            Heap[fpos] = Heap[spos];
            Heap[spos] = tmp;
        }

        // Method 6
        // To heapify the node at pos
        private void maxHeapify(int pos) {
            // If the node is a non-leaf node and greater
            // than any of its child
            if (!isLeaf(pos)) {
                if (Heap[leftChild(pos)] != null && Heap[rightChild(pos)] != null) {
                    if (Heap[pos].getPangkat() < Heap[leftChild(pos)].getPangkat()
                            || Heap[pos].getPangkat() < Heap[rightChild(pos)].getPangkat()) {

                        // Swap with the left child and heapify
                        // the left child
                        if (Heap[leftChild(pos)].getPangkat() > Heap[rightChild(pos)].getPangkat()) {
                            swap(pos, leftChild(pos));
                            maxHeapify(leftChild(pos));
                        }

                        // Swap with the right child and heapify
                        // the right child
                        else {
                            swap(pos, rightChild(pos));
                            maxHeapify(rightChild(pos));
                        }
                    }
                }
                if (Heap[leftChild(pos)] != null) { // Jika hanya ada child yang kiri
                    if (Heap[leftChild(pos)].getPangkat() < Heap[pos].getPangkat()) { // Ini percolate down biasa
                        swap(pos, leftChild(pos));
                        maxHeapify(leftChild(pos));
                    }
                }
            }
        }

        // Method 7
        // To insert a node into the heap
        public void insert(Karyawan element) {

            if (size >= maxsize) {
                return;
            }

            Heap[++size] = element;
            int current = size;

            while (Heap[current].getPangkat() > Heap[parent(current)].getPangkat()) {
                swap(current, parent(current));
                current = parent(current);
            }
        }

        // Method 8
        // To print the contents of the heap
        public StringBuilder print() {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= size / 2; i++) {

                // Printing the parent and both childrens
                sb.append(" PARENT : " + Heap[i].getPangkat() + "("
                        + i + ")"
                        + " LEFT CHILD : "
                        + Heap[2 * i].getPangkat() + "(" + 2 * i
                        + ")" + " RIGHT CHILD :"
                        + Heap[2 * i + 1].getPangkat() + "("
                        + 2 * i + 1 + ")");

                // By here new line is required
                sb.append("\n");
            }
            return sb;
        }

        // Method 10
        // Getter for the minimal element
        public Karyawan getMax() {
            return Heap[FRONT];
        }

        public int extractMax() {
            int temp = size;
            Karyawan popped = Heap[FRONT];
            if (popped.equals(Heap[size])) { // Jika hanya ada 1 anggota
                Heap[FRONT] = null;
                size--;
                return 0;
            } else {
                Heap[FRONT] = Heap[size--];
                Heap[temp] = null;
                maxHeapify(FRONT);
            }
            return -1;
        }

        public void removeAll() {
            Heap = new Karyawan[1];
            Heap[0] = new Karyawan(0, Integer.MAX_VALUE);
        }
    }

    static class Karyawan {
        private int identitas;
        private int pangkat;
        private int maxFriendRank;
        private boolean isResigned;

        public Karyawan(int identitas, int pangkat) {
            this.identitas = identitas;
            this.pangkat = pangkat;
            this.isResigned = false;
            this.maxFriendRank = 0;
        }

        public int getIdentitas() {
            return this.identitas;
        }

        public int getPangkat() {
            return this.pangkat;
        }

        public int getMaxFriendRank() {
            return this.maxFriendRank;
        }

        public boolean getIsResigned() {
            return this.isResigned;
        }

        public void setIsResigned(boolean newValue) {
            this.isResigned = newValue;
        }

        public void setPangkat(int newPangkat) {
            this.pangkat = newPangkat;
        }

        public void setMaxFriendRank(int newMaxFriendRank) {
            this.maxFriendRank = newMaxFriendRank;
        }
    }

    static class Graph {
        private int numOfVertex;
        private MaxHeap adj;
        private MaxHeap[] arrOfAdj;

        public Graph(int numOfVertex) {
            this.numOfVertex = numOfVertex;

            arrOfAdj = new MaxHeap[numOfVertex];

            // Membuat arrayOfMaxHeaps untuk menyatakan adj list. Index 0 Karyawan ke-1
            for (int i = 0; i < numOfVertex; i++) {
                adj = new MaxHeap(100000);
                arrOfAdj[i] = adj;
            }
        }

        public MaxHeap getAdj(int karyawanKe) {
            return arrOfAdj[karyawanKe - 1];
        }
    }
}
