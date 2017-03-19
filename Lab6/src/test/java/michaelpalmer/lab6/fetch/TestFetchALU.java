package michaelpalmer.lab6.fetch;

import com.sun.javaws.exceptions.InvalidArgumentException;
import michaelpalmer.lab6.alu.gates.AndGate;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;

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
    public void testGateOperation() {
        boolean[] a = {false, false, false, true, false, true, false, true};
        boolean[] b = {true, true, false, true, true, true, false, false};
        boolean[] expected = {false, false, false, true, false, true, false, false};
        alu.gateOperation(new AndGate(), a, b);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testGateOperationBadLength() {
        try {
            alu.gateOperation(new AndGate(), new boolean[] {false}, new boolean[] {true});
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBoundsException was not thrown");
    }

    @Test
    public void testGateOperationMissingOperandParameter() {
        try {
            alu.gateOperation(new AndGate());
        } catch (InvalidParameterException e) {
            return;
        }
        fail("InvalidParameterException was not thrown");
    }

    @Test
    public void testIncOp() {
        boolean[] initial = {false, false, false, false, false, false, false, false};
        boolean[] expected = {false, false, false, false, false, false, false, true};
        alu.incOp(initial, psw);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testDecOp() {
        boolean[] initial = {false, false, false, false, false, false, false, true};
        boolean[] expected = {false, false, false, false, false, false, false, false};
        alu.decOp(initial, psw);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testAddOp() {
        boolean[] a = {false, false, false, false, false, false, true, true}; // 3
        boolean[] b = {false, false, false, false, false, true, false, false}; // 4
        boolean[] expected = {false, false, false, false, false, true, true, true}; // 7
        alu.addOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testSubOpPositive() {
        boolean[] a = {false, false, false, false, false, true, true, true}; // 7
        boolean[] b = {false, false, false, false, false, true, false, false}; // 4
        boolean[] expected = {false, false, false, false, false, false, true, true}; // 3
        alu.subOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testSubOpNegative() {
        boolean[] a = {false, false, false, false, false, true, true, true}; // 7
        boolean[] b = {false, false, false, false, true, false, true, false}; // 10
        boolean[] expected = {true, true, true, true, true, true, false, true}; // -3
        alu.subOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testMulOp() {
        boolean[] a = {false, false, false, false, false, true, false, true}; // 5
        boolean[] b = {false, false, false, false, false, false, true, true}; // 3
        boolean[] expected = {false, false, false, false, true, true, true, true}; // 15
        alu.mulOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testDivOp() {
        boolean[] a = {false, false, false, false, true, false, true, false}; // 10
        boolean[] b = {false, false, false, false, false, false, true, false}; // 2
        boolean[] expected = {false, false, false, false, false, true, false, true}; // 5
        alu.divOp(a, b, psw);
        assertArrayEquals(expected, b);
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
        boolean[] ssss = {false, false, false, false, true, false, true, false};
        boolean[] dddd = {false, true, true, false, true, false, false, false};
        alu.movOp(ssss, dddd, psw);
        assertArrayEquals(ssss, dddd);
    }

    @Test
    public void testAndOp() {
        boolean[] a = {false, false, true, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, true};
        boolean[] expected = {false, false, true, false, true, false, false, false};
        alu.andOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testOrOp() {
        boolean[] a = {false, false, false, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, false};
        boolean[] expected = {false, true, true, false, true, false, true, false};
        alu.orOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testXorOp() {
        boolean[] a = {false, false, false, false, true, false, true, false};
        boolean[] b = {false, true, true, false, true, false, false, false};
        boolean[] expected = {false, true, true, false, false, false, true, false};
        alu.xorOp(a, b, psw);
        assertArrayEquals(expected, b);
    }

    @Test
    public void testShiftLeft() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {false, false, false, true, false, true, false, false};
        alu.shiftLeft(initial);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testShiftRight() {
        boolean[] initial = {false, false, false, false, true, false, true, false};
        boolean[] expected = {false, false, false, false, false, true, false, true};
        alu.shiftRight(initial);
        assertArrayEquals(expected, initial);
    }

    @Test
    public void testCompare() {
        boolean[] a = {false, false, false, false, false, true, false, true};
        boolean[] b = {false, false, false, false, false, false, true, true};
        boolean[] c = {false, false, false, false, false, false, true, true};
        assertEquals(-1, alu.compare(a, b));
        assertEquals(1, alu.compare(b, a));
        assertEquals(0, alu.compare(b, c));
    }
}
