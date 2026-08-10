import java.util.Scanner;

public class Main {
    static final int MAX = 105;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n, failed, initiator, i, j;
        int[] process = new int[MAX];

        System.out.println("--- Bully Algorithm Simulation ---\n");
        System.out.print("Enter number of processes: ");
        n = sc.nextInt();

        // Validate number of processes
        if (n <= 0 || n > MAX) {
            System.out.println("Invalid number of processes!");
            sc.close();
            return;
        }

        // Assign process IDs
        for (i = 0; i < n; i++) {
            process[i] = i + 1;
        }

        System.out.print("Process IDs: ");
        for (i = 0; i < n; i++) {
            System.out.print("P" + process[i]);
            if (i < n - 1)
                System.out.print(", ");
        }
        System.out.println();

        // Highest process is the coordinator
        failed = n - 1;
        System.out.println("\nInitial coordinator: P" + process[failed] + " (highest ID)");
        System.out.println("P" + process[failed] + " has crashed!\n");
        System.out.println("----------------------------------------");

        // Election initiator
        System.out.print("Enter process ID to initiate election: ");
        initiator = sc.nextInt();

        boolean found = false;

        // Find process index
        for (i = 0; i < n; i++) {
            if (process[i] == initiator) {
                initiator = i;
                found = true;
                break;
            }
        }

        // Validate process ID
        if (!found) {
            System.out.println("Invalid Process ID!");
            sc.close();
            return;
        }

        // Failed coordinator cannot initiate election
        if (initiator == failed) {
            System.out.println("The selected process is the failed coordinator!");
            sc.close();
            return;
        }

        System.out.println("P" + process[initiator] + " detected coordinator failure");
        System.out.println("P" + process[initiator] + " is starting election\n");

        long startTime = System.nanoTime();

        // Queue for election stages
        int[] stage = new int[MAX];
        boolean[] visited = new boolean[MAX];

        int front = 0, rear = 0;
        stage[rear++] = initiator;
        visited[initiator] = true;

        int elected = -1;
        int[] electedStage = new int[MAX];
        int stageCount = 0;

        while (front < rear) {
            int curr = stage[front++];
            electedStage[stageCount++] = curr;

            int gotOK = 0;

            for (j = curr + 1; j < n; j++) {
                if (j != failed && !visited[j]) {
                    stage[rear++] = j;
                    visited[j] = true;
                    gotOK = 1;
                }
            }

            if (gotOK == 0) {
                elected = curr;
                break;
            }
        }

        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000.0;

        // Display election stages
        for (int s = 0; s < stageCount; s++) {
            int curr = electedStage[s];

            System.out.println("Stage " + (s + 1) + ": P" + process[curr] + " is starting election");

            for (j = curr + 1; j < n; j++) {
                System.out.println("  P" + process[curr] + " sends ELECTION to P" + process[j]);
            }

            for (j = curr + 1; j < n; j++) {
                if (j == failed) {
                    System.out.println("  P" + process[j] + " is dead");
                } else {
                    System.out.println("  P" + process[j] + " sends OK to P" + process[curr]);
                }
            }

            if (s == stageCount - 1 && elected != -1) {
                System.out.println("  P" + process[curr] + " receives no response, declares itself as coordinator");
            } else {
                System.out.println("  P" + process[curr] + " steps down (got OK from higher process)\n");
            }
        }

        // Coordinator announcement
        if (elected != -1) {
            System.out.println("\n----------------------------------------");
            System.out.println("New coordinator: P" + process[elected]);
            System.out.println("----------------------------------------");

            System.out.println("\nFinal coordinator announcement:");
            for (i = 0; i < n; i++) {
                if (i == failed || i == elected)
                    continue;

                System.out.println("  P" + process[elected] + " sends COORDINATOR to P" + process[i]);
            }

            System.out.println("\nP" + process[elected] + " is the new coordinator!");
        } else {
            System.out.println("\nNo process could be elected!");
        }

        System.out.printf("\nTime taken for election: %.4f ms%n", timeTaken);

        sc.close();
    }
}