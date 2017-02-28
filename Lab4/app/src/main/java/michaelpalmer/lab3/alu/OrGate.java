package michaelpalmer.lab4.alu;

public class OrGate extends Gate {

    public void execute() {
        this.output = this.inputA || this.inputB;
    }
}
