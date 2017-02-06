package architecture.lab2;

import architecture.lab2.FourBitALU;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestFourBitALU extends TestCase {

    private FourBitALU alu;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

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
        System.setOut(new PrintStream(outputStream));
        this.alu = new FourBitALU();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.setOut(System.out);
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

    public void testPrint() {
        this.alu.setA(true, true, false, false);
        this.alu.setB(false, false, true, true);
        this.alu.execute(false);
        this.alu.print();
        String expected = "Carry:    0000\n";
        expected += "          ----\n";
        expected += "Input A:  1100   ( 12)\n";
        expected += "Input B:  0011 + (  3) [0011 before XOR]\n";
        expected += "          ----\n";
        expected += "Result: 0 1111   ( 15)\n";
        assertEquals(expected, outputStream.toString());
    }

}
