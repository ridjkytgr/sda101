import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

class Group {
    private String groupName;
    private long groupAmount;

    public Group(String name, long amount) {
        groupName = name;
        groupAmount = amount;
    }

    public String getNama() {
        return groupName;
    }

    public long getJumlah() {
        return groupAmount;
    }

    public void setJumlah(long newValue) {
        groupAmount = newValue;
    }
}

class Lab2 {

    private static InputReader in;
    private static PrintWriter out;
    // Untuk nyimpen grup apa yang terakhir dilayani
    static Queue<Group> linkedList = new LinkedList<Group>();

    // Untuk nyimpen grup dengan total penguin masing-masing
    static Map<String, Long> hashmap = new HashMap<String, Long>();

    static long numOfPenguins = 0;

    // TODO
    static private long handleDatang(String Gi, long Xi) {
        linkedList.add(new Group(Gi, Xi));

        // Untuk keeping track grup apa jumlah berapa (initialize value)
        if (!hashmap.containsKey(Gi)) {
            hashmap.put(Gi, (long) 0);
        }
        numOfPenguins += Xi;
        return numOfPenguins;
    }

    // TODO
    static private String handleLayani(long Yi) {
        numOfPenguins -= Yi;

        while (Yi > 0) {
            Group temp = linkedList.peek();
            long tempAmount;
            if (Yi > temp.getJumlah()) {
                tempAmount = Yi - temp.getJumlah();
                hashmap.put(temp.getNama(), hashmap.get(temp.getNama()) + temp.getJumlah());
                temp.setJumlah(temp.getJumlah() - Yi);
                Yi = tempAmount;
            } else {
                temp.setJumlah(temp.getJumlah() - Yi);
                hashmap.put(temp.getNama(), hashmap.get(temp.getNama()) + Yi);
                Yi = 0;
            }

            if (temp.getJumlah() <= 0) {
                Group result = linkedList.remove();

                if (Yi == 0) {
                    return result.getNama();
                }
            }

            if (Yi == 0) {
                return temp.getNama();
            }

        }
        return "";
    }

    // TODO
    static private long handleTotal(String Gi) {
        if (!hashmap.containsKey(Gi)) {
            return 0;
        }
        return hashmap.get(Gi);
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