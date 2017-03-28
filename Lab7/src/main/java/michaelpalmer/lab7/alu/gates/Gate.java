package michaelpalmer.lab7.alu.gates;

public abstract class Gate {
    protected boolean output;
    private boolean[] input;

    public void set(boolean... input) {
        this.input = input;
    }

    public boolean getInput(int index) {
        return input[index];
    }

    public boolean[] getInputs() {
        return input;
    }

    public boolean getOutput() {
        return output;
    }

    public boolean equals(Gate gate) {
        boolean[] gateInputs = gate.getInputs();

        if (getInputs().length != gateInputs.length) {
            return false;
        }

        if (getOutput() != gate.getOutput()) {
            return false;
        }

        for (int i = 0; i < gateInputs.length; i++) {
            if (gateInputs[i] != getInput(i)) {
                return false;
            }
        }

        return true;
    }

    public void makeEqual(Gate gate) {
        input = gate.getInputs();
        output = gate.output;
    }

    public abstract void execute();

    public void print() {
        char c = 'a';
        System.out.println(getClass().getSimpleName() + ":");
        for (int i = 0; i < getInputs().length; i++) {
            System.out.printf("%s(%c) %b", i == 0 ? "Input: " : ", ", c++, getInput(i));
        }
        System.out.println();
        System.out.printf("Output: %b\n", output);
    }
}
