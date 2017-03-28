package michaelpalmer.lab7.alu.gates;

public class NotGate extends Gate {

    public void execute() {
        output = !getInput(0);
    }
}