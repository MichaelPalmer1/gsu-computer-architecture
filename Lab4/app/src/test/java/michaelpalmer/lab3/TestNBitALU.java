package michaelpalmer.lab4;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import michaelpalmer.lab4.alu.NBitALU;

public class TestNBitALU {

    private NBitALU alu;
    private boolean[][] operations;

    @Before
    protected void setUp() throws Exception {
        this.alu = new NBitALU(6);
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
    public void testAddition() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(false);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getSum(), alu.getA() + alu.getB());
            }
        }
    }

    @Test
    public void testSubtraction() throws Exception {
        for (boolean[] operationA : operations) {
            alu.setA(operationA);
            for (boolean[] operationB : operations) {
                alu.setB(operationB);
                alu.execute(true);
                alu.print();
                System.out.println("-----------------------------------------");
                assertEquals(alu.getSum(), alu.getA() + alu.getB());
            }
        }
    }

}
