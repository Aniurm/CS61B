package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class randomizedTest {
    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> A = new ArrayDeque<>();

        int N = 500000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                A.addLast(randVal);
                assertTrue(A.equals(L));
            } else if (operationNumber == 1) {
                // size
                assertEquals(L.size(), A.size());
            } else if (operationNumber == 2) {
                // removelast
                if (L.size() <= 0)
                    continue;
                L.removeLast();
                A.removeLast();
                assertTrue(L.equals(A));
            } else if (operationNumber == 3)
            {
                // get
                if (L.size() <= 0)
                    continue;
                int randVal = StdRandom.uniform(0, L.size());
                assertEquals(L.get(randVal), A.get(randVal));
            } else if (operationNumber == 4) {
                //addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                A.addFirst(randVal);
                assertTrue(L.equals(A));
            } else if (operationNumber == 5) {
                assertEquals(L.isEmpty(), A.isEmpty());
            }
        }
    }
}
