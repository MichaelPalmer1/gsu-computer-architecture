package michaelpalmer.lab6.fetch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestFetchALU {

    private FetchALU alu;
    private FetchPSW psw;

    @Before
    public void setUp() {
        alu = new FetchALU();
        psw = new FetchPSW();
    }

    @Test
    public void testIncOp() {
        boolean[] initial = {false, false, false, false, false, false, false, false};
        boolean[] expected = {false, false, false, false, false, false, false, true};
        assertArrayEquals(expected, alu.incOp(initial, psw));
    }

    @Test
    public void testDecOp() {
        boolean[] initial = {false, false, false, false, false, false, false, true};
        boolean[] expected = {false, false, false, false, false, false, false, false};
        assertArrayEquals(expected, alu.decOp(initial, psw));
    }

    @Test
    public void testAddOp() {
        boolean[] a = {false, false, false, false, false, false, true, true}; // 3
        boolean[] b = {false, false, false, false, false, true, false, false}; // 4
        boolean[] expected = {false, false, false, false, false, true, true, true}; // 7
        assertArrayEquals(expected, alu.addOp(a, b, psw));
    }

    @Test
    public void testSubOpPositive() {
        boolean[] a = {false, false, false, false, false, true, true, true}; // 7
        boolean[] b = {false, false, false, false, false, true, false, false}; // 4
        boolean[] expected = {false, false, false, false, false, false, true, true}; // 3
        assertArrayEquals(expected, alu.subOp(a, b, psw));
    }

    @Test
    public void testSubOpNegative() {
        boolean[] a = {false, false, false, false, false, true, true, true}; // 7
        boolean[] b = {false, false, false, false, true, false, true, false}; // 10
        boolean[] expected = {true, true, true, true, true, true, false, true}; // -3
        assertArrayEquals(expected, alu.subOp(a, b, psw));
    }

    @Test
    public void testMulOp() {
        boolean[] a = {false, false, false, false, false, true, false, true}; // 5
        boolean[] b = {false, false, false, false, false, false, true, true}; // 3
        boolean[] expected = {false, false, false, false, true, true, true, true}; // 15
        assertArrayEquals(expected, alu.mulOp(a, b, psw));
    }

    @Test
    public void testDivOp() {
        boolean[] a = {false, false, false, false, true, false, true, false}; // 10
        boolean[] b = {false, false, false, false, false, false, true, false}; // 2
        boolean[] expected = {false, false, false, false, false, true, false, true}; // 5
        assertArrayEquals(expected, alu.divOp(a, b, psw));
    }

    @Test
    public void testSetOp() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {true, true, true, true, true, true, true, true};
        alu.setOp(initial, psw);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testClrOp() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {false, false, false, false, false, false, false, false};
        alu.clrOp(initial, psw);
        assertArrayEquals(expected, initial);
        assertFalse(psw.getN());
        assertFalse(psw.getC());
        assertFalse(psw.getV());
        assertFalse(psw.getZ());
    }

    @Test
    public void testNegOp() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {true, true, true, true, false, true, false, true};
        alu.negOp(initial, psw);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testMovOp() {
        boolean[] dddd = {false, false, false, false, true, false, true, false};
        boolean[] ssss = {false, true, true, false, true, false, false, false};
        alu.movOp(dddd, ssss, psw);
        assertArrayEquals(ssss, dddd);
    }

    @Test
    public void testAndOp() {
        boolean[] a = {false, false, true, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, true};
        boolean[] expected = {false, false, true, false, true, false, false, false};
        assertArrayEquals(expected, alu.andOp(a, b, psw));
    }

    @Test
    public void testOrOp() {
        boolean[] a = {false, false, false, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, false};
        boolean[] expected = {false, true, true, false, true, false, true, false};
        assertArrayEquals(expected, alu.orOp(a, b, psw));
    }

    @Test
    public void testXorOp() {
        boolean[] a = {false, false, false, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, false};
        boolean[] expected = {false, true, true, false, false, false, true, false};
        assertArrayEquals(expected, alu.xorOp(a, b, psw));
    }

    @Test
    public void testShiftLeftInline() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {false, false, false, true, false, true, false, false};
        alu.shiftLeftInline(initial);
        assertArrayEquals(expected, initial);
    }
}
