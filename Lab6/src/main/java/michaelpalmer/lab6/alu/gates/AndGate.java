package michaelpalmer.lab6.alu.gates;

public class AndGate extends Gate {

    public void execute() {
        this.output = this.inputA && this.inputB;
    }
}
