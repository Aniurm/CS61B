package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    //all inputs are composite numbers
    @Test
    public void testSquarePrimesAllComposite() {
       IntList lst = IntList.of(20, 4, 6, 8, 10);
       boolean changed = IntListExercises.squarePrimes(lst);
       assertEquals("20 -> 4 -> 6 -> 8 -> 10", lst.toString());
       assertTrue(!changed);
    }

    //all inputs are prime number
    @Test
    public void testSquarePrimesAllPrime() {
        IntList lst = IntList.of(3, 5, 7, 11);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 25 -> 49 -> 121", lst.toString());
        assertTrue(changed);
    }

}
