package id.unware.poken;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void rounding_floating_point_number() throws Exception {
        double fee = 58500.01;
        int expectation = 58500;

        int res = (int) fee;

        assertEquals(expectation, res);
    }
}