package architecture.lab2;

import junit.framework.TestCase;

public class TestAndGate extends TestCase {

    private AndGate gate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gate = new AndGate();
    }

    public void testTrueTrue() {
        gate.set(true, true);
        gate.execute();
        assertTrue(gate.getOutput());
    }

    public void testTrueFalse() {
        gate.set(true, false);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    public void testFalseTrue() {
        gate.set(false, true);
        gate.execute();
        assertFalse(gate.getOutput());
    }

    public void testFalseFalse() {
        gate.set(false, false);
        gate.execute();
        assertFalse(gate.getOutput());
    }

}
