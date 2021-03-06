package michaelpalmer.lab3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import michaelpalmer.lab3.alu.XorGate;

public class TestXorGate {

    private XorGate gate;

    @Before
    protected void setUp() throws Exception {
        gate = new XorGate();
    }

    @Test
    public void testTrueTrue() throws Exception {
        gate.set(true, true);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    @Test
    public void testTrueFalse() throws Exception {
        gate.set(true, false);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    @Test
    public void testFalseTrue() throws Exception {
        gate.set(false, true);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    @Test
    public void testFalseFalse() throws Exception {
        gate.set(false, false);
        gate.execute();
        assertFalse(gate.getOutput());
    }

}
