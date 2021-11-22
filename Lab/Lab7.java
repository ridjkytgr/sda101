
/**
 * Ide: Kenshin Himura (Menggunakan Floyd-Warshall algo)
 */

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
    private static final int INF = Integer.MAX_VALUE;
    private static int[][] graph;

    public static void createGraph(int N) {
        graph = new int[N][N];

        // Mengisi dengan Integer.MAX_VALUE.
        for (int i = 0; i < N; i++) {
            Arrays.fill(graph[i], INF);
        }
    }

    public static void addEdge(int U, int V, int T) {
        // Masukkan secara 2 arah.
        graph[U - 1][V - 1] = T;
        graph[V - 1][U - 1] = T;
    }

    public static int canMudik(int X, int Y, int K, int N) {
        // Bisa mudik
        if (graph[X - 1][Y - 1] <= K) {
            return 1;
        }

        // Gabisa mudik
        return 0;
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

        // Fill the graph
        graph = floydWarshall(graph, N);
        while (Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K, N));
        }
        out.flush();
    }

    // Template from https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16
    public static int[][] floydWarshall(int graph[][], int N) {
        int dist[][] = new int[N][N];
        int i, j, k;

        /*
         * Initialize the solution matrix same as input graph matrix. Or we can say the
         * initial values of shortest distances are based on shortest paths considering
         * no intermediate vertex.
         */
        for (i = 0; i < N; i++)
            for (j = i; j < N; j++) {
                dist[i][j] = graph[i][j];
                dist[j][i] = graph[i][j];
            }

        /*
         * Add all vertices one by one to the set of intermediate vertices. ---> Before
         * start of an iteration, we have shortest distances between all pairs of
         * vertices such that the shortest distances consider only the vertices in set
         * {0, 1, 2, .. k-1} as intermediate vertices. ----> After the end of an
         * iteration, vertex no. k is added to the set of intermediate vertices and the
         * set becomes {0, 1, 2, .. k}
         */
        for (k = 0; k < N; k++) {
            // Pick all vertices as source one by one
            for (i = 0; i < N; i++) {
                // Pick all vertices as destination for the
                // above picked source
                for (j = i; j < N; j++) {
                    // If vertex k is on the shortest path from
                    // i to j, then update the value of dist[i][j]
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];

                            // Karena directed graph.
                            dist[j][i] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }
        return dist;
    }

    public static void printSolution(int dist[][], int N) {
        System.out.println("The following matrix shows the shortest " + "distances between every pair of vertices");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + "   ");
            }
            System.out.println();
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