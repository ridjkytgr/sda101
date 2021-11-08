import java.io.*;
import java.util.*;

public class Lab5 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);

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