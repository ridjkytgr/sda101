import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class Tp1 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int batch;
        int agents;
        int days;
        int events;

        batch = in.nextInt();

        for (int tmp = 0; tmp < batch; tmp++) {
            // Prompt for agents data
            agents = in.nextInt();
            for (int agent = 0; agent < agents; agent++) {
                String prompt = in.next();
                String[] arrOfPrompt = prompt.split(" ");

                String agentCode = arrOfPrompt[0];
                char agentSpecialization = arrOfPrompt[1].charAt(0);
            }

            // Prompt for ammount of days
            days = in.nextInt();
            for (int day = 0; day < days; day++) {
                // Prompt for ammount of siesta appointing someone events
                events = in.nextInt();
                for (int event = 0; event < events; event++) {
                    String prompt = in.next();
                    String[] arrOfPrompt = prompt.split(" ");

                    String agentCode = arrOfPrompt[0];
                    int eventCode = Integer.parseInt(arrOfPrompt[1]);
                }
            }

            // Prompt for last evaluation
            String prompt = in.next();
            String[] arrOfPrompt = prompt.split(" ");
            String evalCommand = arrOfPrompt[0];

            // Jika panutan & deploy
            if (!arrOfPrompt[1].isEmpty()) {
                int num = Integer.parseInt(arrOfPrompt[1]);

                if (evalCommand.equals("PANUTAN")) {
                    out.println(panutan(num));
                } else {
                    out.println(deploy(num));
                }
            } else {
                if (evalCommand.equals("KOMPETITIF")) {
                    out.println(kompetitif());
                } else if (evalCommand.equals("EVALUASI")) {
                    out.println(evaluasi());
                } else {
                    out.println(duo());
                }
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

class Agent {
    private char specialization;
    private String code;
    private int ascend;
    private int descend;
    private int currentRank;
    private boolean isNeverIncrease = true;

    public Agent(String code, char specialization) {
        this.code = code;
        this.specialization = specialization;
        this.ascend = 0;
        this.descend = 0;
    }

    public String getCode() {
        return this.code;
    }

    public char getSpecialization() {
        return this.specialization;
    }

    public int getAscend() {
        return this.ascend;
    }

    public int getDescend() {
        return this.descend;
    }

    public int getCurrentRank() {
        return this.currentRank;
    }

    public boolean getIsNeverIncrease() {
        return this.isNeverIncrease;
    }

    /**
     * Menghitung dan me-return banyak rank 1 dan turun rank terakhir suatu agen.
     * 
     * @return integer banyak rank 1 dan turun rank terakhir suatu agen.
     */
    public int countAscDes() {
        return this.descend + this.ascend;
    }

    /**
     * Mengubah status dari isNeverIncrease menjadi false jika suatu agen pernah
     * naik rank.
     */
    public void changeStatus() {
        this.isNeverIncrease = false;
    }
}