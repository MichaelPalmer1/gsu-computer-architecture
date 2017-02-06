package architecture.lab2;

public class AndGate extends Gate {

    public void execute() {
        this.output = this.inputA && this.inputB;
    }
}
