package michaelpalmer.lab6.alu;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestNBitALU {

    private NBitALU alu;
    private boolean[][] operations;

    @Before
    public void setUp() throws Exception {
        this.alu = new NBitALU(4);
        this.operations = this.alu.generateOperations();
    }

    @Test
    public void testGenerateOperations() throws Exception {
        this.alu = new NBitALU(4);
        this.operations = this.alu.generateOperations();
        boolean[][] operations = {
                {false, false, false, false},
                {false, false, false, true},
                {false, false, true, false},
                {false, false, true, true},
                {false, true, false, false},
                {false, true, false, true},
                {false, true, true, false},
                {false, true, true, true},
                {true, false, false, false},
                {true, false, false, true},
                {true, false, true, false},
                {true, false, true, true},
                {true, true, false, false},
                {true, true, false, true},
                {true, true, true, false},
                {true, true, true, true},
        };
        for (int i = 0; i < operations.length; i++) {
            for (int j = 0; j < operations[i].length; j++) {
                assertEquals(operations[i][j], this.operations[i][j]);
            }
        }
    }

    @Test
    public void testOpAND() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(false, false, false);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA() & alu.getB(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpOR() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(false, false, true);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA() | alu.getB(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpXOR() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(false, true, false);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA() ^ alu.getB(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpSET() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(true, true, false);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA(), alu.getB());
                assertEquals(alu.getA(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpCLR() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(true, false, true);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA(), alu.getB());
                assertEquals(alu.getA(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpNEG() throws Exception {
        alu.setA(false, true, false, true);
        alu.setB(false, false, true, true);
        assertEquals(5, alu.getA());
        assertEquals(3, alu.getB());

        alu.execute(true, true, true);
        assertEquals(10, alu.getA());
        assertEquals(12, alu.getB());
        assertEquals(15, alu.getSum());
    }

    @Test
    public void testOpADD() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(false, true, true);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA() + alu.getB(), alu.getSum());
            }
        }
    }

    @Test
    public void testOpSUB() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(true, false, false);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getA() - -alu.getB(), alu.getSum());
            }
        }
    }

}
