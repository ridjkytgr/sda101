import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;

    public static void createGraph(int N) {

    }

    public static void addEdge(int U, int V, int T) {

    }

    public static int canMudik(int X, int Y, int K) {
        return -1;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int M = in.nextInt();
        int Q = in.nextInt();
        createGraph(N);

        for (int i = 0; i < M; i++) {
            int U = in.nextInt();
            int V = in.nextInt();
            int T = in.nextInt();
            addEdge(U, V, T);
        }
        while (Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K));
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