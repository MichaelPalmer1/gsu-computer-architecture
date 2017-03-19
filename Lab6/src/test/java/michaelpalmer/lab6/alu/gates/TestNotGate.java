package michaelpalmer.lab6.alu.gates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestNotGate {

    private NotGate gate;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        gate = new NotGate();
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(System.out);
    }

    @Test
    public void testTrue() throws Exception {
        gate.set(true);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    @Test
    public void testFalse() throws Exception {
        gate.set(false);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    @Test
    public void testPrint() throws Exception {
        gate.set(true);
        gate.execute();
        gate.print();
        String expected = "NotGate:\nInput: (a) true\nOutput: false\n";
        assertEquals(expected, outputStream.toString());
    }

}
