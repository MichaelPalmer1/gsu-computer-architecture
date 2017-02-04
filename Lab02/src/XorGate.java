public class XorGate extends Gate {

    public void execute() {
        this.output = this.inputA ^ this.inputB;
    }
}