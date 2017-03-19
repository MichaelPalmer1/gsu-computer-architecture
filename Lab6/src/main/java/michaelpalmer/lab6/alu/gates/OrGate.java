package michaelpalmer.lab6.alu.gates;

public class OrGate extends Gate {

    public void execute() {
        output = getInput(0) || getInput(1);
    }
}
