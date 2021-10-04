import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO - class untuk Lantai
class Lantai {

    public Lantai() {

    }

    public String getValue() {
        return "";
    }

}

// TODO - class untuk Gedung
class Gedung {

    public Gedung() {

    }

    public void bangun(String input) {
        // TODO - handle BANGUN
    }

    public void lift(String input) {
        // TODO - handle LIFT
    }

    public void hancurkan() {
        // TODO - handle HANCURKAN
    }

    public void timpa(String input) {
        // TODO - handle TIMPA
    }

    public String sketsa() {
        // TODO - handle SKETSA
        return "";
    }

}

public class Lab4 {
    private static InputReader in;
    public static PrintWriter out;
    public static Gedung Gedung;

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N operations
        int N = in.nextInt();
        String cmd;

        // TODO - handle inputs
        for (int zz = 0; zz < N; zz++) {

            cmd = in.next();

            if (cmd.equals("FONDASI")) {
                String A = in.next();

            } else if (cmd.equals("BANGUN")) {
                String A = in.next();
                String X = in.next();
                // TODO

            } else if (cmd.equals("LIFT")) {
                String A = in.next();
                String X = in.next();
                // TODO

            } else if (cmd.equals("SKETSA")) {
                String A = in.next();
                // TODO

            } else if (cmd.equals("TIMPA")) {
                String A = in.next();
                String B = in.next();
                // TODO

            } else if (cmd.equals("HANCURKAN")) {
                String A = in.next();
                // TODO
            }
        }

        // don't forget to close/flush the output
        out.close();
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