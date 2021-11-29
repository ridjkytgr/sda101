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
    public static int[] pangkat = new int[100000];
    public static MaxHeap[] adjList = new MaxHeap[100000];

    /**
     * Method untuk menambahkan hubungan antara karyawan ke-u dan karyawan ke-v.
     * 
     * @param u karyawan ke-u.
     * @param v karyawan ke-v.
     */
    public static void tambah(int u, int v) {
        adjList[u - 1].insert(v - 1);
        adjList[v - 1].insert(u - 1);
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
    public static int carry(int u) {
        if (adjList[u - 1].getMax() < 1) { // Menandakan kalau u belum punya teman.
            return 0;
        }
        return pangkat[adjList[u - 1].getMax()];
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

        // Initialize pangkat-pangkat karyawan
        for (int i = 0; i < n; i++) {
            int pangkatKaryawan = in.nextInt();

            pangkat[i] = pangkatKaryawan;
            adjList[i] = new MaxHeap(100000);
        }

        // Hubungan antarkaryawwan
        for (int i = 0; i < m; i++) {
            int m1 = in.nextInt();
            int m2 = in.nextInt();

            // Karena undirected graph.
            adjList[m1 - 1].insert(m2 - 1);
            adjList[m2 - 1].insert(m1 - 1);
        }

        // Kejadian
        for (int i = 0; i < q; i++) {
            int kode = in.nextInt();

            if (kode == 1) { // Tambah
                int u = in.nextInt();
                int v = in.nextInt();

                tambah(u, v);
            } else if (kode == 2) { // Resign
                int u = in.nextInt();

                // resign(u);
            } else if (kode == 3) { // Carry
                int u = in.nextInt();

                out.println(carry(u));
            } else if (kode == 4) {

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
        private int[] Heap;
        private int size;
        private int maxsize;

        // Initializaing front as static with unity
        private final int FRONT = 1;

        // Constructor of this class
        public MaxHeap(int maxsize) {

            // This keyword refers to current object itself
            this.maxsize = maxsize;
            this.size = 0;

            Heap = new int[this.maxsize + 1];
            Heap[0] = Integer.MIN_VALUE;
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

            int tmp;
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
                if (pangkat[pos] < pangkat[leftChild(pos)]
                        || pangkat[pos] < pangkat[rightChild(pos)]) {

                    // Swap with the left child and heapify
                    // the left child
                    if (pangkat[leftChild(pos)] > pangkat[rightChild(pos)]) {
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
        }

        // Method 7
        // To insert a node into the heap
        public void insert(int element) {

            if (size >= maxsize) {
                return;
            }

            Heap[++size] = element;
            int current = size;

            while (pangkat[current] < pangkat[parent(current)]) {
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
                sb.append(" PARENT : " + pangkat[Heap[i]] + "(" + Heap[i] + ")" + " LEFT CHILD : "
                        + pangkat[Heap[2 * i]] + "(" + Heap[2 * i] + ")" + " RIGHT CHILD :"
                        + pangkat[Heap[2 * i + 1]] + "(" + Heap[2 * i + 1] + ")");

                // By here new line is required
                sb.append("\n");
            }
            return sb;
        }

        // Method 9
        // To remove and return the minimum
        // element from the heap
        public int remove() {

            int popped = Heap[FRONT];
            Heap[FRONT] = Heap[size--];
            maxHeapify(FRONT);

            return popped;
        }

        // Method 10
        // Getter for the minimal element
        public int getMax() {
            return Heap[FRONT];
        }
    }
}
