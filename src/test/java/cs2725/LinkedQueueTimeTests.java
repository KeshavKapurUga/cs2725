package cs2725;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import cs2725.api.Queue;
import cs2725.impl.LinkedQueue;
import cs2725.impl.SlowLinkedQueue;

public class LinkedQueueTimeTests {

    // @Test
    public void testOperationsOnLargeQueue() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {

            Queue<Integer> queue = new SlowLinkedQueue<>();
            int n = 2000000;

            for (int i = 0; i < n; ++i) {
                queue.enqueue(i);
                queue.size();
                queue.isEmpty();
            }

            for (int i = 0; i < n / 2; ++i) {
                queue.dequeue();
                queue.size();
                queue.isEmpty();
            }

            assertEquals(queue.size(), n / 2);

        });
    }

}
