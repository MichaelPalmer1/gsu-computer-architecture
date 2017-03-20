package michaelpalmer.lab6.alu.adders;

import michaelpalmer.lab6.alu.gates.OrGate;

public class FullAdder {
    private HalfAdder h1 = new HalfAdder(), h2 = new HalfAdder();
    private OrGate o1 = new OrGate();
    public boolean a, b, carryIn, sum, carryOut;
    private String name;

    public FullAdder() {
        this("FullAdder");
    }

    public FullAdder(String name) {
        this.name = name;
    }

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

}
