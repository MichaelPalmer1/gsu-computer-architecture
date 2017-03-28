package michaelpalmer.lab7.alu;

import michaelpalmer.lab7.alu.adders.FullAdder;
import michaelpalmer.lab7.alu.gates.AndGate;
import michaelpalmer.lab7.alu.gates.NotGate;
import michaelpalmer.lab7.alu.gates.OrGate;
import michaelpalmer.lab7.alu.gates.XorGate;

public class NBitALU {
    private boolean op1, op2, op3, overflow;
    private int numBits;
    private boolean[] a, b, x, sum, carry;

    public static final int OP_AND = 0, OP_OR = 1, OP_XOR = 2, OP_ADD = 3,
            OP_SUB = 4, OP_CLR = 5, OP_SET = 6, OP_NEG = 7;

    public static final boolean[]
            AND = {false, false, false},
            OR = {false, false, true},
            XOR = {false, true, false},
            ADD = {false, true, true},
            SUB = {true, false, false},
            CLR = {true, false, true},
            SET = {true, true, false},
            NEG = {true, true, true};

    public NBitALU(int numBits) {
        if (numBits <= 0) {
            System.err.println("The number of bits must be a positive number.");
            System.exit(1);
        }
        this.numBits = numBits;
        sum = new boolean[numBits];
        carry = new boolean[numBits];
        x = new boolean[numBits];
    }

    public void setA(boolean... a) {
        this.a = a;
    }

    public void setB(boolean... b) {
        this.b = b;
    }

    public void execute(boolean... op) {
        if (op.length != 3) {
            System.err.println("Exactly 3 op bits are required");
            System.exit(1);
        }
        execute(op[0], op[1], op[2]);
    }

    public void execute(boolean op1, boolean op2, boolean op3) {
        if (this.a.length != numBits || this.b.length != numBits) {
            System.err.printf("A and B must have exactly %d bits.\n", numBits);
            System.exit(1);
        }

        boolean lastCarry;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.overflow = false;
        this.x = new boolean[numBits];

        switch (getOpCode()) {
            case OP_AND:
                for (int i = 0; i < numBits; i++) {
                    boolean a = this.a[numBits - i - 1];
                    boolean b = this.b[numBits - i - 1];
                    AndGate and = new AndGate();
                    and.set(a, b);
                    and.execute();
                    sum[numBits - i - 1] = and.getOutput();
                    carry[numBits - i - 1] = false;
                }
                return;

            case OP_OR:
                for (int i = 0; i < numBits; i++) {
                    boolean a = this.a[numBits - i - 1];
                    boolean b = this.b[numBits - i - 1];
                    OrGate or = new OrGate();
                    or.set(a, b);
                    or.execute();
                    sum[numBits - i - 1] = or.getOutput();
                    carry[numBits - i - 1] = false;
                }
                return;

            case OP_XOR:
                for (int i = 0; i < numBits; i++) {
                    boolean a = this.a[numBits - i - 1];
                    boolean b = this.b[numBits - i - 1];
                    XorGate xor = new XorGate();
                    xor.set(a, b);
                    xor.execute();
                    sum[numBits - i - 1] = xor.getOutput();
                    carry[numBits - i - 1] = false;
                }
                return;

            case OP_ADD:
                lastCarry = false;
                this.carry = new boolean[numBits];
                break;

            case OP_SUB:
                lastCarry = true;
                this.carry = new boolean[numBits];
                break;

            case OP_CLR:
                for(int i = 0; i < numBits; i++) {
                    this.a[i] = false;
                    this.b[i] = false;
                    this.sum[i] = false;

                    // Clear carry bits
                    this.carry[i] = false;
                }
                return;

            case OP_SET:
                for(int i = 0; i < numBits; i++) {
                    this.a[i] = true;
                    this.b[i] = true;
                    this.sum[i] = true;

                    // Clear carry bits
                    this.carry[i] = false;
                }
                return;

            case OP_NEG:
                for(int i = 0; i < numBits; i++) {
                    NotGate notGate = new NotGate();
                    notGate.set(this.a[i]);
                    notGate.execute();
                    this.a[i] = notGate.getOutput();

                    notGate.set(this.b[i]);
                    notGate.execute();
                    this.b[i] = notGate.getOutput();

                    notGate.set(this.sum[i]);
                    notGate.execute();
                    this.sum[i] = notGate.getOutput();

                    // Clear carry bits
                    this.carry[i] = false;
                }
                return;

            default:
                // Invalid op code
                System.err.println("Invalid op code");
                System.exit(1);
                return;
        }

        for (int i = 0; i < numBits; i++) {
            boolean a = this.a[numBits - i - 1];
            boolean b = this.b[numBits - i - 1];
            XorGate xor = new XorGate();
            xor.set(b, getOpCode() == OP_SUB);
            xor.execute();
            x[numBits - i - 1] = xor.getOutput();

            FullAdder adder = new FullAdder();
            adder.set(a, xor.getOutput(), lastCarry);
            adder.execute();
            sum[numBits - i - 1] = adder.sum;
            carry[numBits - i - 1] = adder.carryOut;
            lastCarry = adder.carryOut;
        }

        carry[numBits - 1] = getOpCode() == OP_SUB;
        overflow = lastCarry;
    }

    public boolean getA(int index) {
        return a[index];
    }

    public int getA() {
        double a = 0;
        for (int i = 0; i < numBits; i++) {
            a += (this.a[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        return (int) a;
    }

    public boolean getB(int index) {
        return b[index];
    }

    public int getB() {
        double b = 0;
        for (int i = 0; i < numBits; i++) {
            b += (this.b[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        b *= (getOpCode() == OP_SUB ? -1 : 1);
        return (int) b;
    }

    public int getSum() {
        double sum = getOpCode() == OP_SUB ? -Math.pow(2, numBits) : 0;
        sum += (overflow ? Math.pow(2, numBits) : 0);
        for (int i = 0; i < numBits; i++) {
            sum += (this.sum[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        return (int) sum;
    }

    public boolean[] getSumBits() {
        return this.sum;
    }

    public int getOpCode() {
        return (int) ((op1 ? Math.pow(2, 2) : 0) + (op2 ? Math.pow(2, 1) : 0) + (op3 ? Math.pow(2, 0) : 0));
    }

    public boolean getSumBit(int bit) {
        return sum[bit];
    }

    public boolean getCarryBit(int bit) {
        return carry[bit];
    }

    public boolean getOverflow() {
        return overflow;
    }

    public void getEquation() {
        System.out.printf("Equation: %d + %d = %d\n", getA(), getB(), getSum());
    }

    public void print() {
        String a = "", b = "", x = "", sum = "", carry = "", divider = "", operator = " ";

        switch (getOpCode()) {
            case OP_AND:
                operator = "&";
                break;
            case OP_OR:
                operator = "|";
                break;
            case OP_XOR:
                operator = "^";
                break;
            case OP_ADD:
                operator = "+";
                break;
            case OP_SUB:
                operator = "-";
                break;
            case OP_NEG:
                operator = "!";
                break;
        }

        for (int i = 0; i < numBits; i++) {
            a += this.a[i] ? "1" : "0";
            b += this.b[i] ? "1" : "0";
            x += this.x[i] ? "1" : "0";
            carry += this.carry[i] ? "1" : "0";
            sum += this.sum[i] ? "1" : "0";
            divider += "-";
        }
        System.out.printf("Carry:    %s\n", carry);
        System.out.printf("          %s\n", divider);
        System.out.printf("Input A:  %s   (%d)\n", a, getA());

        if (getOpCode() == OP_SUB) {
            System.out.printf("Input B:  %s %s (%d) [%s before XOR]\n", x, operator, getB(), b);
        } else {
            System.out.printf("Input B:  %s %s (%d)\n", b, operator, getB());
        }

        System.out.printf("          %s\n", divider);
        System.out.printf("Result: %d %s   (%d)\n", overflow ? 1 : 0, sum, getSum());
    }

    /**
     * Generate all the possible inputs
     *
     * @return 2d array of booleans representing all possible operations
     */
    public boolean[][] generateOperations() {
        boolean[][] operations = new boolean[(int) Math.pow(2, numBits)][numBits];
        int times = 0, numTimes = 1;

        for (int i = numBits - 1; i >= 0; i--) {
            boolean value = false;

            for (int j = 0; j < operations.length; j++) {
                operations[j][i] = value;
                times += 1;

                if (numTimes == times) {
                    value = !value;
                    times = 0;
                }
            }
            numTimes *= 2;
        }
        return operations;
    }

    public void test(boolean addition) {
        String header = addition ? "ADDITION" : "SUBTRACTION";
        boolean[][] operations = generateOperations();

        System.out.println("\n##################################");
        System.out.printf("#           %-20s #\n", header);
        System.out.println("##################################\n");
        for (boolean[] operationA : operations) {
            setA(operationA);
            for (boolean[] operationB : operations) {
                setB(operationB);
                execute(!addition, addition, addition);
                print();
                System.out.println("-----------------------------------------");
            }
        }
    }

    public void testAddition() {
        test(true);
    }

    public void testSubtraction() {
        test(false);
    }
}
