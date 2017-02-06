package architecture.lab2;

public class NBitALU {
    private boolean op, overflow;
    private int numBits;
    private boolean[] a, b, x, sum, carry;

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

    public void execute(boolean op) {
        if (this.a.length != numBits || this.b.length != numBits) {
            System.err.printf("A and B must have exactly %d bits.\n", numBits);
            System.exit(1);
        }

        this.op = op;
        boolean lastCarry = op;

        for (int i = 0; i < numBits; i++) {
            boolean a = this.a[numBits - i - 1];
            boolean b = this.b[numBits - i - 1];
            XorGate xor = new XorGate();
            xor.set(b, op);
            xor.execute();
            x[numBits - i - 1] = xor.getOutput();

            FullAdder adder = new FullAdder();
            adder.set(a, xor.getOutput(), lastCarry);
            adder.execute();
            sum[numBits - i - 1] = adder.sum;
            carry[numBits - i - 1] = adder.carryOut;
            lastCarry = adder.carryOut;
        }
        carry[numBits - 1] = op;
        overflow = lastCarry;
    }

    public int getA() {
        double a = 0;
        for (int i = 0; i < numBits; i++) {
            a += (this.a[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        return (int) a;
    }

    public int getB() {
        double b = 0;
        for (int i = 0; i < numBits; i++) {
            b += (this.b[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        b *= (op ? -1 : 1);
        return (int) b;
    }

    public int getSum() {
        double sum = op ? -Math.pow(2, numBits) : 0;
        sum += (overflow ? Math.pow(2, numBits) : 0);
        for (int i = 0; i < numBits; i++) {
            sum += (this.sum[i] ? Math.pow(2, numBits - i - 1) : 0);
        }
        return (int) sum;
    }

    public void getEquation() {
        System.out.printf("Equation: %d + %d = %d\n", getA(), getB(), getSum());
    }

    public void print() {
        String a = "", b = "", x = "", sum = "", carry = "", divider = "";
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
        System.out.printf("Input B:  %s + (%d) [%s before XOR]\n", x, getB(), b);
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
                execute(!addition);
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
