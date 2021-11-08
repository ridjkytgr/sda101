
/**
 * Referensi: https://www.baeldung.com/java-avl-trees
 */

import java.io.*;
import java.util.*;

public class Lab5 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    private static AVLTree avlTree = new AVLTree();

    public static void main(String[] args) {

        // Menginisialisasi kotak sebanyak N
        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            String nama = in.next();
            int harga = in.nextInt();
            int tipe = in.nextInt();
            handleStock(nama, harga, tipe);
        }

        // Query
        // (method dan argumennya boleh diatur sendiri, sesuai kebutuhan)
        int NQ = in.nextInt();
        for (int i = 0; i < NQ; i++) {
            String Q = in.next();
            if (Q.equals("BELI")) {
                int L = in.nextInt();
                int R = in.nextInt();
                out.println(handleBeli(L, R));

            } else if (Q.equals("STOCK")) {
                String nama = in.next();
                int harga = in.nextInt();
                int tipe = in.nextInt();
                handleStock(nama, harga, tipe);

            } else { // SOLD_OUT
                String nama = in.next();
                handleSoldOut(nama);

            }
        }

        out.flush();
    }

    // TODO
    static String handleBeli(int L, int R) {

        return "";
    }

    // TODO
    static void handleStock(String nama, int harga, int tipe) {
        Node insert = avlTree.insert(new Node(nama, harga, tipe), nama, harga, tipe);
        avlTree.insertHelper(insert);

        out.println(avlTree.preOrder(avlTree.root));
    }

    // TODO
    static void handleSoldOut(String nama) {

    }

    // taken from https://codeforces.com/submissions/Petr
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

class Node {
    String nama;
    int harga;
    int tipe;
    int height;
    Node left;
    Node right;
    boolean flagVisited;

    // Untuk yang harga duplikat.
    ArrayList<Node> duplicates;

    public Node(String nama, int harga, int tipe) {
        this.nama = nama;
        this.harga = harga;
        this.tipe = tipe;
        this.duplicates = new ArrayList<Node>();
        this.flagVisited = false;
    }

}

class AVLTree {
    public Node root;

    public AVLTree() {
        this.root = null;
    }

    public void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    public int height(Node n) {
        return n == null ? -1 : n.height;
    }

    public int getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    /**
     * Method untuk melakukan RR-rotation
     * 
     * @param y root dari AVLTree yang akan dilakukan rotation
     * @return Root yang baru setelah dilakukan rotasi
     */
    public Node rotateLeft(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Method untuk melakukan LL-rotation
     * 
     * @param y root dari AVLTree yang akan dilakukan rotation
     * @return Root yang baru setelah dilakukan rotasi
     */
    public Node rotateRight(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Proses rebalance setiap menambahkan ataupun mengurangi Node
     * (Insertion/Deletion).
     * 
     * @param z root yang akan dilakukan rebalancing
     * @return root baru setelah dilakuakn rebalancing
     */
    public Node rebalance(Node z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right))
                z = rotateRight(z);
            else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    public void insertHelper(Node node) {
        this.root = node;
    }

    public Node insert(Node node, String nama, int harga, int tipe) {
        if (node == null) {
            return new Node(nama, harga, tipe);
        } else if (node.harga > harga) {
            node.left = insert(node.left, nama, harga, tipe);
        } else if (node.harga < harga) {
            node.right = insert(node.right, nama, harga, tipe);
        } else if (node.harga == harga && node.flagVisited == true) { // Jika ada node yang kembar
            node.duplicates.add(new Node(nama, harga, tipe));
        } else if (node.harga == harga && node.flagVisited == false) {
            node.flagVisited = true;
        }
        return rebalance(node);
    }

    public StringBuilder preOrder(Node root) {
        StringBuilder sb = new StringBuilder();
        if (root != null) {
            String harga = "(" + root.harga + ")";
            sb.append(root.nama);
            sb.append(harga);
            sb.append(" ");
            if (root.duplicates.size() > 0) { // Jika ada duplikat
                for (int i = 0; i < root.duplicates.size(); i++) {
                    harga = "(" + root.duplicates.get(i).harga + ")";
                    sb.append(root.duplicates.get(i).nama);
                    sb.append(harga);
                    sb.append(" ");
                }
            }
            preOrder(root.left);
            preOrder(root.right);
        }
        return sb;
    }

    // public Node delete(Node node, int harga) {
    // if (node == null) {
    // return node;
    // } else if (node.harga > harga) {
    // node.left = delete(node.left, harga);
    // } else if (node.harga < harga) {
    // node.right = delete(node.right, harga);
    // } else {
    // if (node.left == null || node.right == null) {
    // node = (node.left == null) ? node.right : node.left;
    // } else {
    // Node mostLeftChild = mostLeftChild(node.right);
    // node.harga = mostLeftChild.harga;
    // node.right = delete(node.right, node.harga);
    // }
    // }
    // if (node != null) {
    // node = rebalance(node);
    // }
    // return node;
    // }
}