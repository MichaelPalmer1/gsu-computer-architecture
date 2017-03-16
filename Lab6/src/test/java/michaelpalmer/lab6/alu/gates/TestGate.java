package michaelpalmer.lab6.alu.gates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestGate {

    private AndGate gate, gate2;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    protected void setUp() throws Exception {
        gate = new AndGate();
        gate2 = new AndGate();
        System.setOut(new PrintStream(outputStream));
    }

    @After
    protected void tearDown() throws Exception {
        System.setOut(System.out);
    }

    @Test
    public void testGetA() throws Exception {
        gate.set(true, false);
        assertTrue(gate.getA());
    }

    @Test
    public void testGetB() throws Exception {
        gate.set(true, false);
        assertFalse(gate.getB());
    }

    @Test
    public void testEquals() throws Exception {
        gate.set(true, false);
        gate.execute();
        gate2.set(true, false);
        gate2.execute();
        assertTrue(gate.equals(gate2));
    }

    @Test
    public void testNotEquals() throws Exception {
        gate.set(true, false);
        gate.execute();
        gate2.set(false, false);
        gate2.execute();
        assertFalse(gate.equals(gate2));
    }

    @Test
    public void testMakeEqual() throws Exception {
        gate.set(true, true);
        gate.execute();
        gate2.set(false, false);
        gate2.execute();
        assertFalse(gate.equals(gate2));
        gate.makeEqual(gate2);
        assertTrue(gate.equals(gate2));
    }

    @Test
    public void testPrint() throws Exception {
        gate.set(true, false);
        gate.execute();
        gate.print();
        String expected = "AndGate:\nInputs: (a) true, (b) false\nOutput: false\n";
        assertEquals(expected, outputStream.toString());
    }

}
