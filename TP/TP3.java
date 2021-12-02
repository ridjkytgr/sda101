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
        graf.getAdj(u).insert(dataKaryawan[u - 1], dataKaryawan[v - 1]);
        graf.getAdj(v).insert(dataKaryawan[v - 1], dataKaryawan[u - 1]);
    }

    // public static void resign(int u) {
    // pangkat[u - 1] = -1;

    // // Menghapus semua vertex u di tetangganya.
    // while (adjList[u - 1].getMax() > 0) {
    // int tetangga = adjList[u - 1].remove();
    // adjList[tetangga]
    // }

    // // Menghapus adjacency list dari karyawan ke-u tersebut.
    // adjList[u - 1] = new MaxHeap(1);

    // }

    /**
     * Mencari teman dari u dengan pangkat tertinggi.
     * 
     * @param u karyawan yang akan dicari teman tertingginya.
     * @return pangkat tertinggi dari teman u.
     */
    public static int carry(Graph graf, int u) {
        if (graf.getAdj(u).getMax() == null) { // Menandakan kalau u belum punya teman.
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

            dataKaryawan[i] = new Karyawan(i + 1, pangkatKaryawan);
        }

        // Hubungan antarkaryawwan
        for (int i = 0; i < m; i++) {
            int m1 = in.nextInt();
            int m2 = in.nextInt();

            MaxHeap adjList1 = graf.getAdj(m1);
            MaxHeap adjList2 = graf.getAdj(m2);

            // Karena undirected graph.
            adjList1.insert(dataKaryawan[m1 - 1], dataKaryawan[m2 - 1]);
            adjList2.insert(dataKaryawan[m2 - 1], dataKaryawan[m1 - 1]);

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

                // resign(u);
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
        private void swap(Karyawan reference, int fpos, int spos) {

            Karyawan tmp;
            tmp = Heap[fpos];

            Heap[fpos].setLetakHeapWithWho(reference.getIdentitas(), spos);
            Heap[spos].setLetakHeapWithWho(reference.getIdentitas(), fpos);

            Heap[fpos] = Heap[spos];
            Heap[spos] = tmp;
        }

        // // Method 6
        // // To heapify the node at pos
        // private void maxHeapify(Karyawan reference, int pos) {
        // // If the node is a non-leaf node and greater
        // // than any of its child
        // if (!isLeaf(pos)) {
        // if (Heap[pos] < Heap[leftChild(pos)])
        // || pangkat[pos] < pangkat[rightChild(pos)]) {

        // // Swap with the left child and heapify
        // // the left child
        // if (pangkat[leftChild(pos)] > pangkat[rightChild(pos)]) {
        // swap(reference, pos, leftChild(pos));
        // maxHeapify(reference, leftChild(pos));
        // }

        // // Swap with the right child and heapify
        // // the right child
        // else {
        // swap(reference, pos, rightChild(pos));
        // maxHeapify(reference, rightChild(pos));
        // }
        // }
        // }
        // }

        // Method 7
        // To insert a node into the heap
        public void insert(Karyawan reference, Karyawan element) {

            if (size >= maxsize) {
                return;
            }

            Heap[++size] = element;
            Heap[size].setLetakHeapWithWho(reference.getIdentitas(), size);
            int current = size;

            while (Heap[current].getPangkat() > Heap[parent(current)].getPangkat()) {
                swap(reference, current, parent(current));
                current = parent(current);
            }
        }

        // Method 8
        // To print the contents of the heap
        public StringBuilder print(Karyawan reference) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= size / 2; i++) {

                // Printing the parent and both childrens
                sb.append(" PARENT : " + Heap[i].getPangkat() + "("
                        + Heap[i].getLetakHeapWithWho(reference.getIdentitas()) + ")"
                        + " LEFT CHILD : "
                        + Heap[2 * i].getPangkat() + "(" + Heap[2 * i].getLetakHeapWithWho(reference.getIdentitas())
                        + ")" + " RIGHT CHILD :"
                        + Heap[2 * i + 1].getPangkat() + "("
                        + Heap[2 * i + 1].getLetakHeapWithWho(reference.getIdentitas()) + ")");

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
    }

    static class Karyawan {
        private int identitas;
        private int pangkat;
        private int[] letakHeap;

        public Karyawan(int identitas, int pangkat) {
            this.identitas = identitas;
            this.pangkat = pangkat;

            // Untuk menyimpan letak karyawan pada adj orang lain.
            this.letakHeap = new int[100000];
        }

        public int getIdentitas() {
            return this.identitas;
        }

        public int getPangkat() {
            return this.pangkat;
        }

        public int getLetakHeapWithWho(int karyawanTetangga) {
            return letakHeap[karyawanTetangga - 1];
        }

        public void setLetakHeapWithWho(int karyawanTetangga, int letakBaru) {
            letakHeap[karyawanTetangga - 1] = letakBaru;
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
