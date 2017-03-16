package michaelpalmer.lab6.fetch;

/**
 * Fetch CPU
 */
public class FetchCPU {

    public FetchRegister r0, r1, r2, r3, pc, sp;
    public FetchPSW psw;
    public FetchALU alu;
    public FetchMem data, prog;

    public FetchCPU() {
        alu = new FetchALU();
        data = new FetchMem(); // memory size fixed to 1000 cells

        // create 4 8-bit registers
        r0 = new FetchRegister("r0");
        r1 = new FetchRegister("r1");
        r2 = new FetchRegister("r2");
        r3 = new FetchRegister("r3");

        pc = new FetchRegister("pc");
        sp = new FetchRegister("sp");

        // create 4-bit PSW
        psw = new FetchPSW();
    }

    public void print() {
        r0.print();
        r1.print();
        r2.print();
        r3.print();
        pc.print();
        sp.print();
        psw.print();
    }

    /**
     * Construct CPU and perform basic tests
     */
    public void testCPU() {
        print();
        data.testMem();
    }

}
