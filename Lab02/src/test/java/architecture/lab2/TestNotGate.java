package architecture.lab2;

import junit.framework.TestCase;

public class TestNotGate extends TestCase {

    private NotGate gate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gate = new NotGate();
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

}
