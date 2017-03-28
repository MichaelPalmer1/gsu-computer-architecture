package michaelpalmer.lab7.alu.adders;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestFullAdder {

    private FullAdder fullAdder;

    @Before
    public void setUp() {
        fullAdder = new FullAdder();
    }

    @Test
    public void testSet() {
        fullAdder.set(true, false, true);
        assertTrue(fullAdder.a);
        assertFalse(fullAdder.b);
        assertTrue(fullAdder.carryIn);
    }

    @Test
    public void testPrint() {
        fullAdder.a = true;
        fullAdder.b = false;
        fullAdder.carryIn = true;
        fullAdder.carryOut = true;
        fullAdder.sum = false;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        fullAdder.print();
        System.setOut(System.out);
        String expected =
                "FullAdder:\n" +
                "Inputs: (a) true, (b) false, (cIn) true\n" +
                "Output: (sum) false, (cOut) true\n\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testExecuteFalseFalseFalse() {
        fullAdder.set(false, false, false);
        fullAdder.execute();
        assertFalse(fullAdder.sum);
        assertFalse(fullAdder.carryOut);
    }

    @Test
    public void testExecuteFalseFalseTrue() {
        fullAdder.set(false, false, true);
        fullAdder.execute();
        assertTrue(fullAdder.sum);
        assertFalse(fullAdder.carryOut);
    }

    @Test
    public void testExecuteFalseTrueFalse() {
        fullAdder.set(false, true, false);
        fullAdder.execute();
        assertTrue(fullAdder.sum);
        assertFalse(fullAdder.carryOut);
    }

    @Test
    public void testExecuteFalseTrueTrue() {
        fullAdder.set(false, true, true);
        fullAdder.execute();
        assertFalse(fullAdder.sum);
        assertTrue(fullAdder.carryOut);
    }

    @Test
    public void testExecuteTrueFalseFalse() {
        fullAdder.set(true, false, false);
        fullAdder.execute();
        assertTrue(fullAdder.sum);
        assertFalse(fullAdder.carryOut);
    }

    @Test
    public void testExecuteTrueFalseTrue() {
        fullAdder.set(true, false, true);
        fullAdder.execute();
        assertFalse(fullAdder.sum);
        assertTrue(fullAdder.carryOut);
    }

    @Test
    public void testExecuteTrueTrueFalse() {
        fullAdder.set(true, true, false);
        fullAdder.execute();
        assertFalse(fullAdder.sum);
        assertTrue(fullAdder.carryOut);
    }

    @Test
    public void testExecuteTrueTrueTrue() {
        fullAdder.set(true, true, true);
        fullAdder.execute();
        assertTrue(fullAdder.sum);
        assertTrue(fullAdder.carryOut);
    }

}