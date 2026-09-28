#include <stdio.h>

#define MAX_PROCESSES 20
#define MAX_MESSAGES 50
#define DISPLAY_LIMIT 60
#define CONTINUE_VALUES 16

int main()
{
    int n, messages;
    int rate[MAX_PROCESSES];
    int adjusted[MAX_PROCESSES];

    int sender, receiver;
    int sendTime, receiveTime;
    int newTime;

    int i, j, t;

    /* 1. Number of processes */
    printf("Enter number of processes: ");
    scanf("%d", &n);

    /* 2. Clock rates */
    printf("\nEnter clock rate for each process:\n");

    for (i = 0; i < n; i++)
    {
        printf("Clock rate of Process %d: ", i + 1);
        scanf("%d", &rate[i]);

        /* -1 means no adjustment has happened */
        adjusted[i] = -1;
    }

    /* 3. Display original clock values */
    printf("\n========== ORIGINAL CLOCK VALUES ==========\n");

    printf("%-10s %-10s %s\n",
           "Process", "Rate", "Clock Values");

    printf("---------------------------------------------\n");

    for (i = 0; i < n; i++)
    {
        printf("P%-9d %-10d ", i + 1, rate[i]);

        for (t = 0; t <= DISPLAY_LIMIT; t += rate[i])
        {
            printf("%d ", t);
        }

        printf("\n");
    }

    /* 4. Number of messages */
    printf("\nEnter number of messages: ");
    scanf("%d", &messages);

    /* 5. Process every message */
    for (i = 1; i <= messages; i++)
    {
        printf("\n========================================\n");
        printf("              MESSAGE %d\n", i);
        printf("========================================\n");

        printf("Enter sender process: ");
        scanf("%d", &sender);

        printf("Enter receiver process: ");
        scanf("%d", &receiver);

        printf("Enter sending time of P%d: ", sender);
        scanf("%d", &sendTime);

        printf("Enter receiving time of P%d: ", receiver);
        scanf("%d", &receiveTime);

        printf("\nSender time   = %d\n", sendTime);
        printf("Receiver time = %d\n", receiveTime);

        /* 6. Lamport logical clock adjustment */
        if (sendTime > receiveTime)
        {
            newTime = sendTime + 1;

            adjusted[receiver - 1] = newTime;

            printf("\nSender time is greater.");
            printf("\nReceiver clock adjusted.");
            printf("\nNew receiver time = %d\n", newTime);
        }
        else
        {
            newTime = receiveTime;

            printf("\nReceiver time is already greater.");
            printf("\nNo adjustment required.");
            printf("\nReceiver time remains = %d\n", newTime);
        }

        /* 7. Display complete clock table */
        printf("\n============================================================\n");
        printf("             CLOCK TABLE AFTER MESSAGE %d\n", i);
        printf("============================================================\n");

        printf("%-10s %-10s %s\n",
               "Process", "Rate", "Clock Values");

        printf("------------------------------------------------------------\n");

        for (j = 0; j < n; j++)
        {
            printf("P%-9d %-10d ", j + 1, rate[j]);

            /*
             * No adjustment yet:
             * Show normal clock values from 0 to 60.
             */
            if (adjusted[j] == -1)
            {
                for (t = 0; t <= DISPLAY_LIMIT; t += rate[j])
                {
                    printf("%d ", t);
                }
            }

            /*
             * Adjustment has happened:
             * Show normal values before adjustment,
             * then adjusted value,
             * then continue from adjusted value.
             */
            else
            {
                /* Original values before adjustment */
                for (t = 0; t < adjusted[j]; t += rate[j])
                {
                    if (t <= DISPLAY_LIMIT)
                    {
                        printf("%d ", t);
                    }
                }

                /* Adjusted value */
                printf("%d ", adjusted[j]);

                /* Continue using the same clock rate */
                for (t = adjusted[j] + rate[j];
                     t <= adjusted[j] + CONTINUE_VALUES;
                     t += rate[j])
                {
                    printf("%d ", t);
                }
            }

            printf("\n");
        }

        printf("------------------------------------------------------------\n");
    }

    /* 8. Final table */
    printf("\n\n================ FINAL CLOCK TABLE ================\n");

    printf("%-10s %-10s %s\n",
           "Process", "Rate", "Clock Values");

    printf("------------------------------------------------------------\n");

    for (i = 0; i < n; i++)
    {
        printf("P%-9d %-10d ", i + 1, rate[i]);

        if (adjusted[i] == -1)
        {
            for (t = 0; t <= DISPLAY_LIMIT; t += rate[i])
            {
                printf("%d ", t);
            }
        }
        else
        {
            for (t = 0; t < adjusted[i]; t += rate[i])
            {
                if (t <= DISPLAY_LIMIT)
                {
                    printf("%d ", t);
                }
            }

            printf("%d ", adjusted[i]);

            for (t = adjusted[i] + rate[i];
                 t <= adjusted[i] + CONTINUE_VALUES;
                 t += rate[i])
            {
                printf("%d ", t);
            }
        }

        printf("\n");
    }

    printf("------------------------------------------------------------\n");

    return 0;
}
