package michaelpalmer.lab6.fetch;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestFetchRegister {

    private FetchRegister register;

    @Before
    public void setUp() {
        register = new FetchRegister();
    }

    @Test
    public void testGetBit() {
        assertFalse(register.getBit(0));
    }

    @Test
    public void testGetBitInvalidIndex() {
        try {
            register.getBit(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testSetBits() {
        for (int i = 0; i < 8; i++) {
            assertFalse(register.getBit(i));
        }

        register.setBits();

        for (int i = 0; i < 8; i++) {
            assertTrue(register.getBit(i));
        }
    }

    @Test
    public void testClearBits() {
        register.setBit(0);
        register.setBit(3);
        register.setBit(7);

        assertTrue(register.getBit(0));
        assertFalse(register.getBit(1));
        assertFalse(register.getBit(2));
        assertTrue(register.getBit(3));
        assertFalse(register.getBit(4));
        assertFalse(register.getBit(5));
        assertFalse(register.getBit(6));
        assertTrue(register.getBit(7));

        register.clearBits();

        for (int i = 0; i < 8; i++) {
            assertFalse(register.getBit(i));
        }
    }

    @Test
    public void testSetBit() {
        assertFalse(register.getBit(0));
        register.setBit(0);
        assertTrue(register.getBit(0));
    }

    @Test
    public void testSetBitInvalidIndex() {
        try {
            register.setBit(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testClearBit() {
        register.setBit(0);
        assertTrue(register.getBit(0));
        register.clearBit(0);
        assertFalse(register.getBit(0));
    }

    @Test
    public void testClearBitInvalidIndex() {
        try {
            register.clearBit(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testGetBits() {
        register.setBit(0);
        register.setBit(3);
        boolean[] expected = {true, false, false, true, false, false, false, false};
        assertArrayEquals(expected, register.getBits());
    }

    @Test
    public void testToString() {
        assertEquals("Register: false false false false false false false false", register.toString());
    }

    @Test
    public void testPrint() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        register.print();
        System.setOut(System.out);
        assertEquals("Register: false false false false false false false false\n", outputStream.toString());
    }
}
