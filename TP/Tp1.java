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
    private static ArrayList<Agent> arrList = new ArrayList<Agent>();
    private static Queue<String> queue = new LinkedList<String>();

    // The max ammount of appointments from siesta
    private static int maxValue;

    // The last rank of the agent that has the maxValue
    private static int maxRank;

    // The agent that will win the KOMPETITIF evaluation
    private static Agent maxAgent;

    public static String panutan(int numOfToppest) {
        int bakso = 0;
        int siomay = 0;

        for (int i = 0; i < numOfToppest; i++) {
            if (arrList.get(i).getSpecialization() == 'B') {
                bakso++;
            } else {
                siomay++;
            }
        }
        return bakso + " " + siomay;
    }

    public static String kompetitif() {
        return maxAgent.getCode() + " " + maxAgent.countAscDes();
    }

    public static String evaluasi() {
        return "";
    }

    public static String duo() {
        return "HASIL DUO";
    }

    public static long deploy(int numOfGroups) {
        return 69;
    }

    /**
     * Method to appoint agent, either to place it on rank 1 or the bottom rank
     * depending on the eventCode.
     * 
     * @param agentCode Which agent that will be ascended or descended
     * @param eventCode Which action that will be taken (0 = ascend, 1 = descend)
     */
    public static void appoint(String agentCode, int eventCode) {
        for (int i = 0; i < arrList.size(); i++) {
            if (arrList.get(i).getCode().equals(agentCode)) {
                // Remove and ascend/descend depending on the event code
                Agent temp = arrList.get(i);
                arrList.remove(i);

                // Ascend
                if (eventCode == 0) {
                    temp.increaseAscend();
                    arrList.add(0, temp);
                    break;
                } else { // Descend
                    temp.increaseDescend();
                    arrList.add(temp);
                    break;
                }
            }

        }
    }

    /**
     * Method to recap agent who has the max siesta points value
     * 
     * @param agent Agent who needs to be checked
     */
    public static void recapMax(Agent agent) {
        if (agent.countAscDes() > maxValue) {
            maxValue = agent.countAscDes();
            maxRank = agent.getCurrentRank();
            maxAgent = agent;
        }
    }

    public static void printArray() {
        // Utilize PrintWriter
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        for (int i = 0; i < arrList.size(); i++) {
            out.print(arrList.get(i).getCode() + " ");
        }
        out.println("");

        // Untuk membuang semua yang tersimpan
        out.flush();
    }

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
            // Reset all of the values and array
            maxValue = 0;
            maxRank = 0;
            arrList.clear();
            queue.clear();

            // Prompt for agents data
            agents = in.nextInt();
            for (int agent = 0; agent < agents; agent++) {
                String agentCode = in.next();
                char agentSpecialization = in.next().charAt(0);

                Agent initiatedAgent = new Agent(agentCode, agentSpecialization);

                // Set rank of the newly added agent
                initiatedAgent.setCurrentRank(agent + 1);

                // Save agent object
                arrList.add(new Agent(agentCode, agentSpecialization));
            }

            // Prompt for ammount of days
            days = in.nextInt();
            for (int day = 0; day < days; day++) {
                // Prompt for ammount of siesta appointing someone events
                events = in.nextInt();
                for (int event = 0; event < events; event++) {
                    String agentCode = in.next();
                    int eventCode = in.nextInt();

                    // Siesta points an agent (it can be ascending, or descending)
                    appoint(agentCode, eventCode);
                }
                printArray();

                // Reset queue
                queue.clear();
                // Assign rank to each agent and also recap the maxValue of siesta points
                for (int agent = 0; agent < arrList.size(); agent++) {
                    arrList.get(agent).setCurrentRank(agent + 1);
                    recapMax(arrList.get(agent));

                    // Recap which agent that never get its rank increased and put it inside of
                    // queue
                    if (arrList.get(agent).getIsNeverIncrease()) {
                        queue.add(arrList.get(agent).getCode());
                    }
                }
            }

            // Prompt for last evaluation
            String evalCommand = in.next();

            if (evalCommand.equals("PANUTAN") || evalCommand.equals("DEPLOY")) {
                int num = in.nextInt();
                if (evalCommand.equals("PANUTAN")) {
                    out.println(panutan(num));
                } else {
                    out.println(deploy(num));
                }
            } else if (evalCommand.equals("KOMPETITIF")) {
                out.println(kompetitif());
            } else if (evalCommand.equals("EVALUASI")) {
                while (!queue.isEmpty()) {
                    out.print(queue.remove() + " ");
                }
            } else if (evalCommand.equals("DUO")) {
                out.println(duo());
            }
            out.flush();
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

    public void setCurrentRank(int currentRank) {
        // Jika rank-nya naik
        if (this.currentRank > currentRank) {
            this.changeStatus();
        }
        this.currentRank = currentRank;
    }

    public void increaseAscend() {
        this.ascend++;
    }

    public void increaseDescend() {
        this.descend++;
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