package architecture.lab2;

import architecture.lab2.NBitALU;
import junit.framework.TestCase;

public class TestNBitALU extends TestCase {

    private NBitALU alu;
    private boolean[][] operations;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.alu = new NBitALU(6);
        this.operations = this.alu.generateOperations();
    }

    public void testGenerateOperations() {
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

    public void testAddition() {
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

    public void testSubtraction() {
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
