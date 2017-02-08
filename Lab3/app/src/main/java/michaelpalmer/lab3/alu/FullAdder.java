package michaelpalmer.lab3.alu;

public class FullAdder {
    private HalfAdder h1 = new HalfAdder(), h2 = new HalfAdder();
    private OrGate o1 = new OrGate();
    public boolean a, b, carryIn, sum, carryOut;

    public void set(boolean a, boolean b, boolean carryIn) {
        this.a = a;
        this.b = b;
        this.carryIn = carryIn;
    }

    public void execute() {
        h1.set(b, carryIn);
        h1.execute();

        h2.set(a, h1.sum);
        h2.execute();

        o1.set(h2.carry, h1.carry);
        o1.execute();

        sum = h2.sum;
        carryOut = o1.getOutput();
    }

    public void print() {
        System.out.println(this.getClass().getSimpleName() + ":");
        System.out.printf("Inputs: (a) %b, (b) %b, (cIn) %b\n", a, b, carryIn);
        System.out.printf("Output: (sum) %b, (cOut) %b\n\n", sum, carryOut);
    }

    public void test() {
        set(false, false, false);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", !sum && !carryOut);

        set(false, false, true);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", sum && !carryOut);

        set(false, true, false);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", sum && !carryOut);

        set(false, true, true);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", !sum && carryOut);

        set(true, false, false);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", sum && !carryOut);

        set(true, false, true);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", !sum && carryOut);

        set(true, true, false);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", !sum && carryOut);

        set(true, true, true);
        execute();
        print();
        System.out.printf("Correct: %b\n\n", sum && carryOut);
    }
}
