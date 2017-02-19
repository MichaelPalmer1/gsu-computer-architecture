package michaelpalmer.lab4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import michaelpalmer.lab4.alu.NotGate;

public class TestNotGate {

    private NotGate gate;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    protected void setUp() throws Exception {
        gate = new NotGate();
        System.setOut(new PrintStream(outputStream));
    }

    @After
    protected void tearDown() throws Exception {
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
        String expected = "NotGate:\nInput: true\nOutput: false\n";
        assertEquals(expected, outputStream.toString());
    }

}
