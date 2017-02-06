package architecture.lab2;

abstract class Gate {
    boolean inputA, inputB, output;

    public void set(boolean a, boolean b) {
        this.inputA = a;
        this.inputB = b;
    }

    public boolean getA() {
        return this.inputA;
    }

    public boolean getB() {
        return this.inputB;
    }

    public boolean getOutput() {
        return this.output;
    }

    public boolean equals(Gate gate) {
        return this.inputA == gate.inputA && this.inputB == gate.inputB && this.output == gate.output;
    }

    public void makeEqual(Gate gate) {
        this.inputA = gate.inputA;
        this.inputB = gate.inputB;
        this.output = gate.output;
    }

    public abstract void execute();

    public void print() {
        System.out.println(this.getClass().getCanonicalName() + ":");
        System.out.printf("Inputs: (a) %b, (b) %b\n", this.inputA, this.inputB);
        System.out.printf("Output: %b\n", this.output);
    }
}
