package michaelpalmer.lab3.alu;

public class OrGate extends Gate {

    public void execute() {
        this.output = this.inputA || this.inputB;
    }
}
