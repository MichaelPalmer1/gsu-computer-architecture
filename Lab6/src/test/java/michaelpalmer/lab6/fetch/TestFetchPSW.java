package michaelpalmer.lab6.fetch;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestFetchPSW {

    private FetchPSW psw;

    @Before
    public void setUp() {
        psw = new FetchPSW();
    }

    @Test
    public void testSetFlags() {
        psw.setFlags(true, false, true, false);
        assertTrue(psw.getN());
        assertFalse(psw.getZ());
        assertTrue(psw.getV());
        assertFalse(psw.getC());
    }

    @Test
    public void testSetN() {
        assertFalse(psw.getN());
        psw.setN();
        assertTrue(psw.getN());
    }

    @Test
    public void testClearN() {
        psw.setN();
        assertTrue(psw.getN());
        psw.clearN();
        assertFalse(psw.getN());
    }

    @Test
    public void testGetN() {
        assertFalse(psw.getN());
    }

    @Test
    public void testSetZ() {
        assertFalse(psw.getZ());
        psw.setZ();
        assertTrue(psw.getZ());
    }

    @Test
    public void testClearZ() {
        psw.setZ();
        assertTrue(psw.getZ());
        psw.clearZ();
        assertFalse(psw.getZ());
    }

    @Test
    public void testGetZ() {
        assertFalse(psw.getZ());
    }

    @Test
    public void testSetV() {
        assertFalse(psw.getV());
        psw.setV();
        assertTrue(psw.getV());
    }

    @Test
    public void testClearV() {
        psw.setV();
        assertTrue(psw.getV());
        psw.clearV();
        assertFalse(psw.getV());
    }

    @Test
    public void testGetV() {
        assertFalse(psw.getV());
    }

    @Test
    public void testSetC() {
        assertFalse(psw.getC());
        psw.setC();
        assertTrue(psw.getC());
    }

    @Test
    public void testClearC() {
        psw.setC();
        assertTrue(psw.getC());
        psw.clearC();
        assertFalse(psw.getC());
    }

    @Test
    public void testGetC() {
        assertFalse(psw.getC());
    }

    @Test
    public void testToString() {
        assertEquals("PSW: N=false, Z=false, V=false, C=false", psw.toString());
    }

    @Test
    public void testPrint() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        psw.print();
        System.setOut(System.out);
        assertEquals("PSW: N=false, Z=false, V=false, C=false\n", outputStream.toString());
    }
}
