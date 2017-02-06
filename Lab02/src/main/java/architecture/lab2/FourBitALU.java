package architecture.lab2;

public class FourBitALU {
    private FullAdder f0 = new FullAdder(), f1 = new FullAdder(), f2 = new FullAdder(), f3 = new FullAdder();
    private XorGate x0 = new XorGate(), x1 = new XorGate(), x2 = new XorGate(), x3 = new XorGate();
    private boolean op, overflow;
    private boolean a0, a1, a2, a3;
    private boolean b0, b1, b2, b3;
    private boolean sum0, sum1, sum2, sum3;
    private boolean carry0, carry1, carry2, carry3;

    public void setA(boolean a0, boolean a1, boolean a2, boolean a3) {
        this.a0 = a0;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    public void setB(boolean b0, boolean b1, boolean b2, boolean b3) {
        this.b0 = b0;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public void execute(boolean op) {
        this.op = op;

        // Working right to left
        x3.set(b3, op);
        x3.execute();
        f3.set(a3, x3.getOutput(), op);
        f3.execute();
        sum3 = f3.sum;
        carry3 = f3.carryOut;

        x2.set(b2, op);
        x2.execute();
        f2.set(a2, x2.getOutput(), carry3);
        f2.execute();
        sum2 = f2.sum;
        carry2 = f2.carryOut;

        x1.set(b1, op);
        x1.execute();
        f1.set(a1, x1.getOutput(), carry2);
        f1.execute();
        sum1 = f1.sum;
        carry1 = f1.carryOut;

        x0.set(b0, op);
        x0.execute();
        f0.set(a0, x0.getOutput(), carry1);
        f0.execute();
        sum0 = f0.sum;
        carry0 = f0.carryOut;

        overflow = carry0;
    }

    public int getA() {
        double a = 0;
        a += (a0 ? Math.pow(2, 3) : 0);
        a += (a1 ? Math.pow(2, 2) : 0);
        a += (a2 ? Math.pow(2, 1) : 0);
        a += (a3 ? Math.pow(2, 0) : 0);
        return (int) a;
    }

    public int getB() {
        double b = 0;
        b += (b0 ? Math.pow(2, 3) : 0);
        b += (b1 ? Math.pow(2, 2) : 0);
        b += (b2 ? Math.pow(2, 1) : 0);
        b += (b3 ? Math.pow(2, 0) : 0);
        b *= (op ? -1 : 1);
        return (int) b;
    }

    public int getSum() {
        double sum = op ? -Math.pow(2, 4) : 0;
        sum += (overflow ? Math.pow(2, 4) : 0);
        sum += (sum0 ? Math.pow(2, 3) : 0);
        sum += (sum1 ? Math.pow(2, 2) : 0);
        sum += (sum2 ? Math.pow(2, 1) : 0);
        sum += (sum3 ? Math.pow(2, 0) : 0);
        return (int) sum;
    }

    public void printEquation() {
        System.out.printf("Equation: %d + %d = %d\n", getA(), getB(), getSum());
    }

    public void print() {
        System.out.printf("Carry:    %d%d%d%d\n",
                carry1 ? 1 : 0, carry2 ? 1 : 0, carry3 ? 1 : 0, 0);
        System.out.println("          ----");
        System.out.printf("Input A:  %d%d%d%d   (%3d)\n",
                a0 ? 1 : 0, a1 ? 1 : 0, a2 ? 1 : 0, a3 ? 1 : 0, getA());
        System.out.printf("Input B:  %d%d%d%d + (%3d) [%d%d%d%d before XOR]\n",
                x0.getOutput() ? 1 : 0, x1.getOutput() ? 1 : 0, x2.getOutput() ? 1 : 0, x3.getOutput() ? 1 : 0,
                getB(), b0 ? 1 : 0, b1 ? 1 : 0, b2 ? 1 : 0, b3 ? 1 : 0);
        System.out.println("          ----");
        System.out.printf("Result: %d %d%d%d%d   (%3d)\n",
                overflow ? 1 : 0, sum0 ? 1 : 0, sum1 ? 1 : 0, sum2 ? 1 : 0, sum3 ? 1 : 0, getSum());
    }

    public void test(boolean addition) {
        String header = addition ? "ADDITION" : "SUBTRACTION";
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

        System.out.println();
        System.out.println("##################################");
        System.out.printf("#           %-20s #\n", header);
        System.out.println("##################################");
        System.out.println();
        for (boolean[] operationA : operations) {
            setA(operationA[0], operationA[1], operationA[2], operationA[3]);
            for (boolean[] operationB : operations) {
                setB(operationB[0], operationB[1], operationB[2], operationB[3]);
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
