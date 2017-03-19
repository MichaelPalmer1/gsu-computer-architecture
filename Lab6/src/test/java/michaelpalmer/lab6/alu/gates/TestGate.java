package michaelpalmer.lab6.alu.gates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestGate {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private Gate gate1, gate2, gate3;

    @Before
    public void setUp() throws Exception {
        gate1 = new Gate() {
            @Override
            public void execute() {
                output = true;
            }
        };
        gate2 = new Gate() {
            @Override
            public void execute() {
                output = true;
            }
        };
        gate3 = new Gate() {
            @Override
            public void execute() {
                output = false;
            }
        };
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(System.out);
    }

    @Test
    public void testGetA() throws Exception {
        gate1.set(true, false);
        assertTrue(gate1.getInput(0));
    }

    @Test
    public void testGetB() throws Exception {
        gate1.set(true, false);
        assertFalse(gate1.getInput(1));
    }

    @Test
    public void testEquals() throws Exception {
        gate1.set(true, false);
        gate1.execute();
        gate2.set(true, false);
        gate2.execute();
        assertTrue(gate1.equals(gate2));
    }

    @Test
    public void testNotEquals() throws Exception {
        gate2.set(true, false);
        gate2.execute();
        gate3.set(false, false);
        gate3.execute();
        assertFalse(gate2.equals(gate3));
    }

    @Test
    public void testMakeEqual() throws Exception {
        gate2.set(true, true);
        gate2.execute();
        gate3.set(false, false);
        gate3.execute();
        assertFalse(gate2.equals(gate3));
        gate2.makeEqual(gate3);
        assertTrue(gate2.equals(gate3));
    }

    @Test
    public void testPrint() throws Exception {
        gate1.set(true, false);
        gate1.execute();
        gate1.print();
        String expected = ":\nInput: (a) true, (b) false\nOutput: true\n";
        assertEquals(expected, outputStream.toString());
    }

}
