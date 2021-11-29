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
        }

        // Hubungan antarkaryawwan
        for (int i = 0; i < m; i++) {

        }

        // Kejadian
        for (int i = 0; i < q; i++) {
            int kode = in.nextInt();

            if (kode == 1) {

            } else if (kode == 2) {

            } else if (kode == 3) {

            } else if (kode == 4) {

            } else if (kode == 5) {

            } else if (kode == 6) {

            } else if (kode == 7) {

            }

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
