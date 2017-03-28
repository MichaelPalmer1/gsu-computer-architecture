package michaelpalmer.lab7.alu.gates;

public class XorGate extends Gate {

    public void execute() {
        output = getInput(0) ^ getInput(1);
    }
}