package michaelpalmer.lab7.fetch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestFetchCPU {
    private FetchCPU cpu;


    @Before
    public void setUp() {
        cpu = new FetchCPU();
    }

    @Test
    public void testALU() {
        boolean[] expected = {false, false, false, false, false, false, false, false};

        assertArrayEquals(expected, cpu.getR0().getBits());
        cpu.getAlu().setOp(cpu.getR0().getBits(), cpu.getPsw());

        expected = new boolean[] {true, true, true, true, true, true, true, true};
        assertArrayEquals(expected, cpu.getR0().getBits());
    }

    @Test
    public void testGetR0() {
        assertEquals(FetchRegister.class, cpu.getR0().getClass());
    }

    @Test
    public void testGetR1() {
        assertEquals(FetchRegister.class, cpu.getR1().getClass());
    }

    @Test
    public void testGetR2() {
        assertEquals(FetchRegister.class, cpu.getR2().getClass());
    }

    @Test
    public void testGetR3() {
        assertEquals(FetchRegister.class, cpu.getR3().getClass());
    }

    @Test
    public void testGetPc() {
        assertEquals(FetchRegister.class, cpu.getPc().getClass());
    }

    @Test
    public void testGetSp() {
        assertEquals(FetchRegister.class, cpu.getSp().getClass());
    }

    @Test
    public void testGetData() {
        assertEquals(FetchMem.class, cpu.getData().getClass());
    }

    @Test
    public void testGetProg() {
        assertEquals(FetchMem.class, cpu.getProg().getClass());
    }
}
