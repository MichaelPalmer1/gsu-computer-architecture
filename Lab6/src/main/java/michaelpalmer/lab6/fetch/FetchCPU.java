package michaelpalmer.lab6.fetch;

/**
 * Fetch CPU
 */
public class FetchCPU {

    private FetchRegister r0, r1, r2, r3, pc, sp;
    private FetchPSW psw;
    private FetchALU alu;
    private FetchMem data, prog;

    public FetchCPU() {
        alu = new FetchALU();
        data = new FetchMem(); // memory size fixed to 1000 cells
        prog = new FetchMem();

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

    public FetchALU getAlu() {
        return alu;
    }

    public FetchPSW getPsw() {
        return psw;
    }

    public FetchMem getData() {
        return data;
    }

    public FetchMem getProg() {
        return prog;
    }

    public FetchRegister getR0() {
        return r0;
    }

    public FetchRegister getR1() {
        return r1;
    }

    public FetchRegister getR2() {
        return r2;
    }

    public FetchRegister getR3() {
        return r3;
    }

    public FetchRegister getPc() {
        return pc;
    }

    public FetchRegister getSp() {
        return sp;
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

}
