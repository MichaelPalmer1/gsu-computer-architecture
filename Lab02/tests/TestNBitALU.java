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
