package michaelpalmer.lab7.alu.adders;

import michaelpalmer.lab7.alu.gates.AndGate;
import michaelpalmer.lab7.alu.gates.NotGate;
import michaelpalmer.lab7.alu.gates.OrGate;

public class HalfAdder {
    private AndGate a1 = new AndGate(), a2 = new AndGate();
    private OrGate o1 = new OrGate();
    private NotGate n1 = new NotGate();
    public boolean a, b, sum, carry;

    public void execute() {
        o1.set(a, b);
        o1.execute();

        a1.set(a, b);
        a1.execute();

        n1.set(a1.getOutput());
        n1.execute();

        a2.set(o1.getOutput(), n1.getOutput());
        a2.execute();

        carry = a1.getOutput();
        sum = a2.getOutput();
    }

    public void set(boolean a, boolean b) {
        this.a = a;
        this.b = b;
    }

    public void print() {
        System.out.println(this.getClass().getSimpleName() + ":");
        System.out.printf("Inputs: (a) %b, (b) %b\n", a, b);
        System.out.printf("Output: (sum) %b, (carry) %b\n", sum, carry);
    }

}
