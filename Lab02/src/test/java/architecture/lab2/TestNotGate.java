package architecture.lab2;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestNotGate extends TestCase {

    private NotGate gate;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gate = new NotGate();
        System.setOut(new PrintStream(outputStream));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.setOut(System.out);
    }

    public void testTrue() {
        gate.set(true);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    public void testFalse() {
        gate.set(false);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    public void testPrint() {
        gate.set(true);
        gate.execute();
        gate.print();
        String expected = "NotGate:\nInput: true\nOutput: false\n";
        assertEquals(expected, outputStream.toString());
    }

}
