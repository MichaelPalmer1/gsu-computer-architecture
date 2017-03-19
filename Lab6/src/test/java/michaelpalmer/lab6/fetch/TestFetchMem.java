package michaelpalmer.lab6.fetch;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestFetchMem {

    private FetchMem mem;

    @Before
    public void setUp() {
        mem = new FetchMem(3);
    }

    @Test
    public void testDefaultConstructor() {
        FetchMem fetchMem = new FetchMem();
        assertEquals(1000, fetchMem.getSize());
        assertEquals(0, fetchMem.getOffset());
        assertEquals(FetchMem.TYPE_DATA, fetchMem.getType());
        assertEquals(0, fetchMem.getByte(0));
    }

    @Test
    public void testConstructSizeType() {
        FetchMem fetchMem = new FetchMem(5, FetchMem.TYPE_PROG);
        assertEquals(5, fetchMem.getSize());
        assertEquals(FetchMem.TYPE_PROG, fetchMem.getType());
    }

    @Test
    public void testGetType() {
        FetchMem data = new FetchMem(FetchMem.TYPE_DATA);
        FetchMem prog = new FetchMem(FetchMem.TYPE_PROG);
        assertEquals(FetchMem.TYPE_DATA, data.getType());
        assertEquals(FetchMem.TYPE_PROG, prog.getType());
    }

    @Test
    public void testGetSize() {
        assertEquals(3, mem.getSize());
    }

    @Test
    public void testGetOffset() {
        assertEquals(0, mem.getOffset());
    }

    @Test
    public void testSetByte() {
        assertEquals(0, mem.getByte(0));
        mem.setByte(0);
        assertEquals(127, mem.getByte(0));
    }

    @Test
    public void testSetByteInvalidIndex() {
        try {
            mem.setByte(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testSetByteMultiple() {
        assertEquals(0, mem.getByte(0));
        assertEquals(0, mem.getByte(2));
        mem.setByte(0);
        mem.setByte(2);
        assertEquals(127, mem.getByte(0));
        assertEquals(127, mem.getByte(2));
    }

    @Test
    public void testClearByte() {
        mem.setByte(0);
        assertEquals(127, mem.getByte(0));
        mem.clearByte(0);
        assertEquals(0, mem.getByte(0));
    }

    @Test
    public void testClearByteInvalidIndex() {
        try {
            mem.clearByte(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testClearByteMultiple() {
        mem.setByte(0);
        mem.setByte(2);
        assertEquals(127, mem.getByte(0));
        assertEquals(127, mem.getByte(2));
        mem.clearByte(0);
        mem.clearByte(2);
        assertEquals(0, mem.getByte(0));
        assertEquals(0, mem.getByte(2));
    }

    @Test
    public void testGetByte() {
        assertEquals(0, mem.getByte(0));
    }

    @Test
    public void testGetByteInvalidIndex() {
        try {
            mem.getByte(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }

    @Test
    public void testPrint() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        mem.print(0);
        System.setOut(System.out);
        assertEquals("MEM: 0: 0\n", outputStream.toString());
    }

    @Test
    public void testPrintInvalidIndex() {
        try {
            mem.print(10);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        fail("IndexOutOfBounds exception was not raised.");
    }
}
