package michaelpalmer.lab6.alu.gates;

public class OrGate extends Gate {

    public void execute() {
        this.output = this.inputA || this.inputB;
    }
}
