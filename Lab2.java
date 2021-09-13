import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

class Lab2 {

    private static InputReader in;
    private static PrintWriter out;

    // Untuk keeping track banyak penguin yang sedang mengantri
    static Stack<Integer> stack = new Stack();

    // Untuk nyimpen grup apa yang terakhir dilayani
    static LinkedList<String> linkedList = new LinkedList();
    static int llCount = 0;

    // TODO
    static private int handleDatang(String Gi, int Xi) {
        // Ngisi stack untuk keeping track banyak penguin
        if (stack.isEmpty()) {
            stack.push(Xi);
        } else {
            int before = stack.pop();
            stack.push(before + Xi);
        }

        // Ngisi linked list untuk keeping track grup terakhir
        for (int i = 0; i < Xi; i++) {
            linkedList.add(Gi);
            llCount++;
        }

        return stack.peek();
    }

    // TODO
    static private String handleLayani(int Yi) {
        // Untuk mengurangi total penguin pada stack
        int before = stack.pop();
        stack.push(before - Yi);

        String result = linkedList.get(Yi - 1);

        // Untuk mengeluarkan yang mengantri pertama
        for (int i = 0; i < Yi; i++) {
            linkedList.removeFirst();
            llCount--;
        }

        return result;
    }

    // TODO
    static private int handleTotal(String Gi) {
        return stack.peek();
    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N;

        N = in.nextInt();

        for (int tmp = 0; tmp < N; tmp++) {
            String event = in.next();

            if (event.equals("DATANG")) {
                String Gi = in.next();
                int Xi = in.nextInt();

                out.println(handleDatang(Gi, Xi));
            } else if (event.equals("LAYANI")) {
                int Yi = in.nextInt();

                out.println(handleLayani(Yi));
            } else {
                String Gi = in.next();

                out.println(handleTotal(Gi));
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