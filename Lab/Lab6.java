import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    private static Map<Integer, Dataran> hm = new HashMap<Integer, Dataran>();
    private static int urutanTerakhir = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        MinHeap heap = new MinHeap(200000);
        for (int i = 0; i < N; i++) {
            int height = in.nextInt();

            // Instantiate dataran baru.
            Dataran dataranBaru = new Dataran(urutanTerakhir, height);

            // Masukkan ke hashmap dan binaryheap.
            hm.put(urutanTerakhir, dataranBaru);
            heap.insert(dataranBaru);

            // Increment urutan paling terakhir
            urutanTerakhir++;
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) {
                int y = in.nextInt();
                Dataran dataranBaru = new Dataran(urutanTerakhir, y);

                // Masukkan ke hashmap dan ke heap.
                hm.put(urutanTerakhir, dataranBaru);
                heap.insert(dataranBaru);
                urutanTerakhir++;
            } else if (query.equals("U")) {
                int x = in.nextInt();
                int y = in.nextInt();

                // Mengubah tinggi dataran.
                Dataran dataranDiubah = hm.get(x);
                dataranDiubah.setTinggi(y);
                heap.u(dataranDiubah);
            } else {
                StringBuilder sb = new StringBuilder();
                // Mencari dataran terendah
                Dataran minimum = heap.getMin();
                int urutanMin = minimum.getUrutan();

                // Mencari tetangga dari dataran tersebut
                int urutanTetangga1 = urutanMin > 0 ? urutanMin - 1 : -1;
                int urutanTetangga2 = urutanMin < urutanTerakhir ? urutanMin + 1 : -1;
                Dataran tetangga1 = urutanTetangga1 == -1 ? null : hm.get(urutanTetangga1);
                Dataran tetangga2 = urutanTetangga2 == -1 ? null : hm.get(urutanTetangga2);

                long tinggiBaru = 0;
                if (tetangga1 != null && tetangga2 != null) {
                    tinggiBaru = Math.max(minimum.getTinggi(), Math.max(tetangga1.getTinggi(), tetangga2.getTinggi()));
                    minimum.setTinggi(tinggiBaru);
                    heap.u(minimum);
                    // out.println(heap.print());
                    if (tetangga1.getTinggi() < tetangga2.getTinggi()) {
                        tetangga1.setTinggi(tinggiBaru);
                        heap.u(tetangga1);
                        // out.println(heap.print());

                        tetangga2.setTinggi(tinggiBaru);
                        heap.u(tetangga2);
                        // out.println(heap.print());
                    } else {
                        tetangga2.setTinggi(tinggiBaru);
                        heap.u(tetangga2);

                        tetangga1.setTinggi(tinggiBaru);
                        heap.u(tetangga1);
                    }

                } else if (tetangga1 != null && tetangga2 == null) {
                    tinggiBaru = Math.max(minimum.getTinggi(), tetangga1.getTinggi());
                    minimum.setTinggi(tinggiBaru);
                    heap.u(minimum);

                    tetangga1.setTinggi(tinggiBaru);
                    heap.u(tetangga1);
                } else if (tetangga1 == null && tetangga2 != null) {
                    tinggiBaru = Math.max(minimum.getTinggi(), tetangga2.getTinggi());
                    minimum.setTinggi(tinggiBaru);
                    heap.u(minimum);

                    tetangga2.setTinggi(tinggiBaru);
                    heap.u(tetangga2);
                } else if (tetangga1 == null && tetangga2 == null) {
                    tinggiBaru = minimum.getTinggi();
                }

                sb.append(tinggiBaru);
                sb.append(" ");
                sb.append(urutanMin);

                out.println(sb);
            }
            // out.println(heap.print());
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

/**
 * Reference: https://www.geeksforgeeks.org/min-heap-in-java/
 */

class MinHeap {
    // Member variables of this class
    private Dataran[] Heap;
    private int size;
    private int maxsize;

    // Initializaing front as static with unity
    private final int FRONT = 1;

    // Constructor of this class
    public MinHeap(int maxsize) {

        // This keyword refers to current object itself
        this.maxsize = maxsize;
        this.size = 0;

        Heap = new Dataran[this.maxsize + 1];
        Heap[0] = new Dataran(0, Integer.MIN_VALUE);
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

        Heap[fpos].setUrutanHeap(spos);
        Heap[spos].setUrutanHeap(fpos);

        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    // Method 6
    // To heapify the node at pos
    private void minHeapify(int pos) {
        // If the node is a non-leaf node and greater
        // than any of its child
        if (!isLeaf(pos)) {
            if (Heap[leftChild(pos)] != null && Heap[rightChild(pos)] != null) {
                if (Heap[leftChild(pos)].getTinggi() == Heap[rightChild(pos)].getTinggi()
                        && Heap[pos].getTinggi() > Heap[leftChild(pos)].getTinggi()) {
                    Dataran dataranSwap = Heap[rightChild(pos)].getUrutan() < Heap[leftChild(pos)].getUrutan()
                            ? Heap[rightChild(pos)]
                            : Heap[leftChild(pos)];
                    int nextHeap = dataranSwap.getUrutanHeap();

                    swap(pos, dataranSwap.getUrutanHeap());
                    minHeapify(nextHeap);
                } else if (Heap[pos].getTinggi() > Heap[leftChild(pos)].getTinggi()
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
                } else if (Heap[pos].getTinggi() == Heap[leftChild(pos)].getTinggi()
                        && Heap[pos].getTinggi() == Heap[rightChild(pos)].getTinggi()) {
                    Dataran dataranSwap = Heap[rightChild(pos)].getUrutan() < Heap[leftChild(pos)].getUrutan()
                            ? Heap[rightChild(pos)]
                            : Heap[leftChild(pos)];
                    if (dataranSwap.getUrutan() < Heap[pos].getUrutan()) {
                        int nextHeap = dataranSwap.getUrutanHeap();
                        swap(pos, dataranSwap.getUrutanHeap());
                        minHeapify(nextHeap);
                    }
                } else if (Heap[pos].getTinggi() == Heap[leftChild(pos)].getTinggi()
                        && Heap[pos].getUrutan() > Heap[leftChild(pos)].getUrutan()) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                } else if (Heap[pos].getTinggi() == Heap[rightChild(pos)].getTinggi()
                        && Heap[pos].getUrutan() > Heap[rightChild(pos)].getUrutan()) {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            } else { // Jika hanya ada child yang kiri
                if (Heap[leftChild(pos)].getTinggi() < Heap[pos].getTinggi()) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                } else if (Heap[leftChild(pos)].getTinggi() == Heap[pos].getTinggi()) { // Jika sama
                    if (Heap[pos].getUrutan() > Heap[leftChild(pos)].getUrutan()) {
                        swap(pos, leftChild(pos));
                        minHeapify(leftChild(pos));
                    }
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
        Heap[size].setUrutanHeap(size);
        int current = size;

        while (Heap[current].getTinggi() < Heap[parent(current)].getTinggi()) {
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
            if (Heap[2 * i + 1] != null) {
                sb.append(" PARENT : " + Heap[i].getTinggi() + "(" + Heap[i].getUrutan() + ")" + " LEFT CHILD : "
                        + Heap[2 * i].getTinggi() + "(" + Heap[2 * i].getUrutan() + ")" + " RIGHT CHILD :"
                        + Heap[2 * i + 1].getTinggi() + "(" + Heap[2 * i + 1].getUrutan() + ")");
            } else {
                sb.append(" PARENT : " + Heap[i].getTinggi() + "(" + Heap[i].getUrutan() + ")" + " LEFT CHILD : "
                        + Heap[2 * i].getTinggi() + "(" + Heap[2 * i].getUrutan() + ")");

            }
            // By here new line is required
            sb.append("\n");
        }
        return sb;
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

    // Method 10
    // Getter for the minimal element
    public Dataran getMin() {
        return Heap[FRONT];
    }

    public void u(Dataran dataran) {
        int current = dataran.getUrutanHeap();
        if (isLeaf(current)) { // Di leaf.
            while (Heap[current].getTinggi() <= Heap[parent(current)].getTinggi()) {
                if (Heap[current].getTinggi() < Heap[parent(current)].getTinggi()) {
                    swap(current, parent(current));
                    current = parent(current);
                } else if (Heap[current].getTinggi() == Heap[parent(current)].getTinggi()
                        && Heap[current].getUrutan() < Heap[parent(current)].getUrutan()) { // Kalo sama, tuker
                                                                                            // berdasarkan urutan
                    swap(current, parent(current));
                    current = parent(current);
                } else {
                    break;
                }
            }
        } else if (current == FRONT) { // Di root
            minHeapify(current);
        } else {
            while (Heap[current].getTinggi() <= Heap[parent(current)].getTinggi()) { // Cek ke atas
                if (Heap[current].getTinggi() < Heap[parent(current)].getTinggi()) { // lebih kecil di atas.
                    swap(current, parent(current));
                    current = parent(current);
                } else if (Heap[current].getTinggi() == Heap[parent(current)].getTinggi()
                        && Heap[current].getUrutan() < Heap[parent(current)].getUrutan()) {
                    swap(current, parent(current));
                    current = parent(current);
                } else {
                    break;
                }
            }

            if (Heap[leftChild(current)] != null) {
                minHeapify(current); // Cek bawah
            }

        }
    }
}

class Dataran {
    private int urutan;
    private int urutanHeap;
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

    public int getUrutanHeap() {
        return this.urutanHeap;
    }

    // Setter
    public void setUrutan(int urutanBaru) {
        this.urutan = urutanBaru;
    }

    public void setTinggi(long tinggiBaru) {
        this.tinggi = tinggiBaru;
    }

    public void setUrutanHeap(int urutanBaru) {
        this.urutanHeap = urutanBaru;
    }
}