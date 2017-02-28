package michaelpalmer.lab4.alu;

public class XorGate extends Gate {

    public void execute() {
        this.output = this.inputA ^ this.inputB;
    }
}