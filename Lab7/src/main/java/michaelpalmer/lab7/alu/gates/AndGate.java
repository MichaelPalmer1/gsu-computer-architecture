package michaelpalmer.lab7.alu.gates;

public class AndGate extends Gate {

    public void execute() {
        output = getInput(0) && getInput(1);
    }
}
