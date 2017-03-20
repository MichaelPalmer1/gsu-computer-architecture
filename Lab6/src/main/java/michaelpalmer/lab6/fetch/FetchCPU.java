package michaelpalmer.lab6.fetch;

/**
 * Fetch CPU
 */
public class FetchCPU {

    private FetchRegister r0, r1, r2, r3, pc, sp;
    private FetchPSW psw;
    private FetchALU alu;
    private FetchMem data, prog;

    public static final boolean[]
            OP_AND = {false, false, false, false},
            OP_OR = {false, false, false, true},
            OP_XOR = {false, false, true, false},
            OP_ADD = {false, false, true, true},
            OP_SUB = {false, true, false, false},
            OP_MUL = {false, true, false, true},
            OP_DIV = {false, true, true, false},
            OP_MOV = {false, true, true, true},
            OP_CLR = {true, false, false, false},
            OP_SET = {true, false, false, true},
            OP_INC = {true, false, true, false},
            OP_DEC = {true, false, true, true},
            OP_NEG = {true, true, false, false},
            OP_BNE = {true, true, false, true},
            OP_BEQ = {true, true, true, false},
            OP_HLT = {true, true, true, true};

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

}
