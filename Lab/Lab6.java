import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    private static Map<Integer, Dataran> hm = new HashMap<Integer, Dataran>();
    private static int urutanTerakhir;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            int height = in.nextInt();
            hm.put(urutanTerakhir, new Dataran(urutanTerakhir, height));
            urutanTerakhir++;
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) {
                int y = in.nextInt();
            } else if (query.equals("U")) {
                // TODO: Handle query U
            } else {
                // TODO: Handle query R
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
}

class MinHeap {
    // Member variables of this class
    private Dataran[] Heap;
    private int size;
    private int maxsize;

    // Initializaing front as static with unity
    private static final int FRONT = 1;

    // Constructor of this class
    public MinHeap(int maxsize) {

        // This keyword refers to current object itself
        this.maxsize = maxsize;
        this.size = 0;

        Heap = new Dataran[this.maxsize + 1];
        Heap[0] = null;
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

        Dataran tmp;
        tmp = Heap[fpos];

        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    // Method 6
    // To heapify the node at pos
    private void minHeapify(int pos) {

        // If the node is a non-leaf node and greater
        // than any of its child
        if (!isLeaf(pos)) {
            if (Heap[pos].getTinggi() > Heap[leftChild(pos)].getTinggi()
                    || Heap[pos].getTinggi() > Heap[rightChild(pos)].getTinggi()) {

                // Swap with the left child and heapify
                // the left child
                if (Heap[leftChild(pos)].getTinggi() < Heap[rightChild(pos)].getTinggi()) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                }

                // Swap with the right child and heapify
                // the right child
                else {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            }
        }
    }

    // Method 7
    // To insert a node into the heap
    public void insert(Dataran element) {

        if (size >= maxsize) {
            return;
        }

        Heap[++size] = element;
        int current = size;

        while (Heap[current].getTinggi() < Heap[parent(current)].getTinggi()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Method 8
    // To print the contents of the heap
    public void print() {
        for (int i = 1; i <= size / 2; i++) {

            // Printing the parent and both childrens
            System.out.print(" PARENT : " + Heap[i].getTinggi() + " LEFT CHILD : " + Heap[2 * i].getTinggi()
                    + " RIGHT CHILD :" + Heap[2 * i + 1].getTinggi());

            // By here new line is required
            System.out.println();
        }
    }

    // Method 9
    // To remove and return the minimum
    // element from the heap
    public Dataran remove() {

        Dataran popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);

        return popped;
    }
}

class Dataran {
    private int urutan;
    private long tinggi;

    public Dataran(int urutan, long tinggi) {
        this.urutan = urutan;
        this.tinggi = tinggi;
    }

    // Getter
    public int getUrutan() {
        return this.urutan;
    }

    public long getTinggi() {
        return this.tinggi;
    }

    // Setter
    public void setUrutan(int urutanBaru) {
        this.urutan = urutanBaru;
    }

    public void setTinggi(long tinggiBaru) {
        this.tinggi = tinggiBaru;
    }
}