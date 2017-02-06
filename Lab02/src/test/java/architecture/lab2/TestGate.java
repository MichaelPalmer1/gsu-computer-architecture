package architecture.lab2;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestGate extends TestCase {

    private AndGate gate, gate2;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gate = new AndGate();
        gate2 = new AndGate();
        System.setOut(new PrintStream(outputStream));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.setOut(System.out);
    }

    public void testGetA() {
        gate.set(true, false);
        assertTrue(gate.getA());
    }

    public void testGetB() {
        gate.set(true, false);
        assertFalse(gate.getB());
    }

    public void testEquals() {
        gate.set(true, false);
        gate.execute();
        gate2.set(true, false);
        gate2.execute();
        assertTrue(gate.equals(gate2));
    }

    public void testNotEquals() {
        gate.set(true, false);
        gate.execute();
        gate2.set(false, false);
        gate2.execute();
        assertFalse(gate.equals(gate2));
    }

    public void testMakeEqual() {
        gate.set(true, true);
        gate.execute();
        gate2.set(false, false);
        gate2.execute();
        assertFalse(gate.equals(gate2));
        gate.makeEqual(gate2);
        assertTrue(gate.equals(gate2));
    }

    public void testPrint() {
        gate.set(true, false);
        gate.execute();
        gate.print();
        String expected = "AndGate:\nInputs: (a) true, (b) false\nOutput: false\n";
        assertEquals(expected, outputStream.toString());
    }

}
