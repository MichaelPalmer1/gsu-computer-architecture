import junit.framework.TestCase;

public class TestNBitALU extends TestCase {

    private NBitALU alu;
    private boolean[][] operations;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        int bits = 5;
        this.alu = new NBitALU(bits);
        this.operations = generateOperations(bits);
    }

    private boolean[][] generateOperations(int numBits) {
        boolean[][] operations = new boolean[(int) Math.pow(2, numBits)][numBits];
        int num = 0, numTimes = 1;

        for (int i = numBits - 1; i >= 0; i--) {
            boolean value = false;
            for (int j = 0; j < operations.length; j++) {
                operations[j][i] = value;
                num++;
                if (numTimes == num) {
                    value = !value;
                    num = 0;
                }
            }
            numTimes *= 2;
        }
        return operations;
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
