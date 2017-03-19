package michaelpalmer.lab6.alu.gates;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAndGate {

    private AndGate gate;

    @Before
    public void setUp() throws Exception {
        gate = new AndGate();
    }

    @Test
    public void testTrueTrue() throws Exception {
        gate.set(true, true);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    @Test
    public void testTrueFalse() throws Exception {
        gate.set(true, false);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    @Test
    public void testFalseTrue() throws Exception {
        gate.set(false, true);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    @Test
    public void testFalseFalse() throws Exception {
        gate.set(false, false);
        gate.execute();
        assertFalse(gate.getOutput());
    }

}