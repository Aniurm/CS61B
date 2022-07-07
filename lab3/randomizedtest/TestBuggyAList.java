package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> bugA = new BuggyAList<>();
        AListNoResizing<Integer> rightA = new AListNoResizing<>();
        for (int i = 4; i <= 6; i++)
        {
            bugA.addLast(i);
            rightA.addLast(i);
        }
        assertEquals(bugA.size(), rightA.size());

        for (int i = 0; i < 3; i++)
        {
            assertEquals(bugA.removeLast(), rightA.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> R = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                R.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(L.size(), R.size());
            } else if (operationNumber == 2) {
                // removelast
                if (L.size() <= 0)
                    continue;
                L.removeLast();
                R.removeLast();
            } else if (operationNumber == 3)
            {
                // getlast
                if (L.size() <= 0)
                    continue;
                assertEquals(L.getLast(), R.getLast());
            }
        }
    }
}
