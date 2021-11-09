
/**
 * Referensi: https://www.geeksforgeeks.org/avl-with-duplicate-keys/
 * NOTES: ADUH KAK MAAF BANGET ini aku cuma delete this add this add that delete that pake feeling jadi jujurly bingung :()
 */

import java.io.*;
import java.util.*;

public class Lab5 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    private static AVLTree avlTree = new AVLTree();
    private static Map<String, Integer> hm = new HashMap<String, Integer>();

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

        return "-1 -1";
    }

    // TODO
    static void handleStock(String nama, int harga, int tipe) {
        avlTree.insertHelper(nama, harga, tipe);
        hm.put(nama, harga);

        avlTree.preOrder(avlTree.root);
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

// An AVL tree node
class Node {
    int harga;
    String nama;
    int tipe;
    Node left;
    Node right;
    int height;
    int count;
    ArrayList<Node> duplikat = new ArrayList<Node>();

    public Node(String nama, int harga, int tipe) {
        this.nama = nama;
        this.harga = harga;
        this.tipe = tipe;
    }
}

class AVLTree {
    Node root;

    public AVLTree() {
        this.root = null;
    }

    // A utility function to get height of the tree
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // A utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    /*
     * Helper function that allocates a new Node with the given key and null left
     * and right pointers.
     */
    Node newNode(String nama, int harga, int tipe) {
        Node node = new Node(nama, harga, tipe);
        node.left = null;
        node.right = null;
        node.height = 1; // new node is initially added at leaf
        node.count = 1;
        return (node);
    }

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get Balance factor of Node N
    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    void insertHelper(String nama, int harga, int tipe) {
        root = insert(root, nama, harga, tipe);
    }

    Node insert(Node node, String nama, int harga, int tipe) {
        /* 1. Perform the normal BST rotation */
        if (node == null)
            return (newNode(nama, harga, tipe));

        // If key already exists in BST, increment count and return
        if (harga == node.harga) {
            (node.count)++;
            return node;
        }

        /* Otherwise, recur down the tree */
        if (harga < node.harga)
            node.left = insert(node.left, nama, harga, tipe);
        else
            node.right = insert(node.right, nama, harga, tipe);

        /* 2. Update height of this ancestor node */
        node.height = max(height(node.left), height(node.right)) + 1;

        /*
         * 3. Get the balance factor of this ancestor node to check whether this node
         * became unbalanced
         */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && harga < node.left.harga)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && harga > node.right.harga)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && harga > node.left.harga) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && harga < node.right.harga) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }

    /*
     * Given a non-empty binary search tree, return the node with minimum key value
     * found in that tree. Note that the entire tree does not need to be searched.
     */
    Node minValueNode(Node node) {
        Node current = node;

        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;

        return current;
    }

    // Node deleteNode(Node root, int key) {
    // // STEP 1: PERFORM STANDARD BST DELETE

    // if (root == null)
    // return root;

    // // If the key to be deleted is smaller than the root's key,
    // // then it lies in left subtree
    // if (key < root.key)
    // root.left = deleteNode(root.left, key);

    // // If the key to be deleted is greater than the root's key,
    // // then it lies in right subtree
    // else if (key > root.key)
    // root.right = deleteNode(root.right, key);

    // // if key is same as root's key, then This is the node
    // // to be deleted
    // else {
    // // If key is present more than once, simply decrement
    // // count and return
    // if (root.count > 1) {
    // (root.count)--;
    // return null;
    // }
    // // ElSE, delete the node

    // // node with only one child or no child
    // if ((root.left == null) || (root.right == null)) {
    // Node temp = root.left != null ? root.left : root.right;

    // // No child case
    // if (temp == null) {
    // temp = root;
    // root = null;
    // } else // One child case
    // root = temp; // Copy the contents of the non-empty child
    // } else {
    // // node with two children: Get the inorder successor (smallest
    // // in the right subtree)
    // Node temp = minValueNode(root.right);

    // // Copy the inorder successor's data to this node and update the count
    // root.key = temp.key;
    // root.count = temp.count;
    // temp.count = 1;

    // // Delete the inorder successor
    // root.right = deleteNode(root.right, temp.key);
    // }
    // }

    // // If the tree had only one node then return
    // if (root == null)
    // return root;

    // // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
    // root.height = max(height(root.left), height(root.right)) + 1;

    // // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
    // // this node became unbalanced)
    // int balance = getBalance(root);

    // // If this node becomes unbalanced, then there are 4 cases

    // // Left Left Case
    // if (balance > 1 && getBalance(root.left) >= 0)
    // return rightRotate(root);

    // // Left Right Case
    // if (balance > 1 && getBalance(root.left) < 0) {
    // root.left = leftRotate(root.left);
    // return rightRotate(root);
    // }

    // // Right Right Case
    // if (balance < -1 && getBalance(root.right) <= 0)
    // return leftRotate(root);

    // // Right Left Case
    // if (balance < -1 && getBalance(root.right) > 0) {
    // root.right = rightRotate(root.right);
    // return leftRotate(root);
    // }

    // return root;
    // }

    // A utility function to print preorder traversal of the tree.
    // The function also prints height of every node
    // public StringBuilder preOrder(Node root) {
    // StringBuilder sb = new StringBuilder();
    // if (root != null) {
    // String harga = "(" + root.harga + ")";
    // sb.append(root.nama);
    // sb.append(harga);
    // sb.append(" ");
    // // if (root.duplicates.size() > 0) { // Jika ada duplikat
    // // for (int i = 0; i < root.duplicates.size(); i++) {
    // // harga = "(" + root.duplicates.get(i).harga + ")";
    // // sb.append(root.duplicates.get(i).nama);
    // // sb.append(harga);
    // // sb.append(" ");
    // // }
    // // }
    // preOrder(root.left);
    // preOrder(root.right);
    // }
    // return sb;
    // }

    void preOrder(Node root) {
        if (root != null) {
            System.out.println("------------------------------");
            System.out.println("INI HARGANYA: " + root.harga);
            System.out.println("INI NAMANYA: " + root.nama);
            preOrder(root.left);
            preOrder(root.right);
        }
    }
}
