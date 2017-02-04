import junit.framework.TestCase;

public class TestFourBitALU extends TestCase {

    private FourBitALU alu;

    private boolean[][] operations = {
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.alu = new FourBitALU();
    }

    public void testAddition() {
        for (boolean[] operationA : operations) {
            this.alu.setA(operationA[0], operationA[1], operationA[2], operationA[3]);
            for (boolean[] operationB : operations) {
                this.alu.setB(operationB[0], operationB[1], operationB[2], operationB[3]);
                this.alu.execute(false);
                this.alu.printEquation();
                assertEquals(this.alu.getSum(), this.alu.getA() + this.alu.getB());
            }
        }
    }

    public void testSubtraction() {
        for (boolean[] operationA : operations) {
            this.alu.setA(operationA[0], operationA[1], operationA[2], operationA[3]);
            for (boolean[] operationB : operations) {
                this.alu.setB(operationB[0], operationB[1], operationB[2], operationB[3]);
                this.alu.execute(true);
                this.alu.printEquation();
                assertEquals(this.alu.getSum(), this.alu.getA() + this.alu.getB());
            }
        }
    }

}
