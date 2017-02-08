package michaelpalmer.lab3.alu;

public class NotGate extends Gate {

    public void set(boolean a) {
        this.inputA = a;
    }

    public void execute() {
        this.output = !this.inputA;
    }

    @Override
    public void print() {
        System.out.println(this.getClass().getSimpleName() + ":");
        System.out.printf("Input: %b\n", this.inputA);
        System.out.printf("Output: %b\n", this.output);
    }
}