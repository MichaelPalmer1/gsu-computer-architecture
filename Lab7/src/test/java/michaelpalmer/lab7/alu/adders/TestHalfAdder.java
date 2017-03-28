package michaelpalmer.lab7.alu.adders;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestHalfAdder {

    private HalfAdder halfAdder;

    @Before
    public void setUp() {
        halfAdder = new HalfAdder();
    }

    @Test
    public void testSet() {
        halfAdder.set(true, false);
        assertTrue(halfAdder.a);
        assertFalse(halfAdder.b);
    }

    @Test
    public void testPrint() {
        halfAdder.a = true;
        halfAdder.b = false;
        halfAdder.sum = true;
        halfAdder.carry = true;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        halfAdder.print();
        System.setOut(System.out);
        String expected =
                "HalfAdder:\n" +
                        "Inputs: (a) true, (b) false\n" +
                        "Output: (sum) true, (carry) true\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testExecuteFalseFalse() {
        halfAdder.set(false, false);
        halfAdder.execute();
        assertFalse(halfAdder.sum);
        assertFalse(halfAdder.carry);
    }

    @Test
    public void testExecuteFalseTrue() {
        halfAdder.set(false, true);
        halfAdder.execute();
        assertTrue(halfAdder.sum);
        assertFalse(halfAdder.carry);
    }

    @Test
    public void testExecuteTrueFalse() {
        halfAdder.set(true, false);
        halfAdder.execute();
        assertTrue(halfAdder.sum);
        assertFalse(halfAdder.carry);
    }

    @Test
    public void testExecuteTrueTrue() {
        halfAdder.set(true, true);
        halfAdder.execute();
        assertFalse(halfAdder.sum);
        assertTrue(halfAdder.carry);
    }

}