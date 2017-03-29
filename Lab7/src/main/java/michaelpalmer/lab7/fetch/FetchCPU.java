package michaelpalmer.lab7.fetch;

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
            OP_ADD = new boolean[] {false, false, true, true},
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

    public void runLab7() {
        int[] randomNums = calculateRandomNumbers();
        int MEMORY_ADDRESS = 500;

        // Add everything up
        for (int i = 0; i < randomNums.length; i++) {
            // Set instruction in memory
            System.out.printf("Number %d = %d\n", i, randomNums[i]);
            setInstruction(MEMORY_ADDRESS, OP_ADD, FetchMem.intToBoolean(randomNums[i]), r0.getBits());

            data.print(MEMORY_ADDRESS);

            // Execute instruction
            execute(MEMORY_ADDRESS);
        }

        // Divide to get average
        setInstruction(MEMORY_ADDRESS, OP_DIV, FetchMem.intToBoolean(randomNums.length), r0.getBits());
        data.print(MEMORY_ADDRESS);
        execute(MEMORY_ADDRESS);

        // Get result
        System.out.println("Result: " + alu.booleanToInt(r0.getBits()));
        int sum = 0;
        for (int i : randomNums) {
            sum += i;
        }
        sum /= randomNums.length;
        System.out.println("Expected: " + sum);
    }

    public void setInstruction(int memoryLocation, boolean[] code, boolean[] ssss, boolean[] dddd) {
        data.setCell(memoryLocation, code, ssss, dddd);
    }

    public void execute(int memoryLocation) {
        // Pull from memory
        boolean[] dddd = data.getD(memoryLocation);

        // Execute ALU
        alu.execute(data.getCode(memoryLocation), data.getS(memoryLocation), dddd, psw);

        // Update register
        for (int i = 0; i < dddd.length; i++) {
            if (dddd[i]) {
                r0.setBit(i);
            } else {
                r0.clearBit(i);
            }
        }
    }

    public int[] calculateRandomNumbers() {
        return calculateRandomNumbers(4, 12);
    }

    public int[] calculateRandomNumbers(int min, int max) {
        int[] randomNums = new int[min + (int) (Math.random() * ((max - min) + 1))];

        // Calculate random numbers between 0 and 11
        min = 0;
        max = 11;

        for (int i = 0; i < randomNums.length; i++) {
            randomNums[i] = min + (int) (Math.random() * ((max - min) + 1));
        }

        return randomNums;
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
