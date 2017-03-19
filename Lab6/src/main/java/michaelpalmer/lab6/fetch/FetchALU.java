package michaelpalmer.lab6.fetch;

import michaelpalmer.lab6.alu.NBitALU;
import michaelpalmer.lab6.alu.adders.FullAdder;
import michaelpalmer.lab6.alu.gates.*;

import java.security.InvalidParameterException;

/**
 * Fetch ALU
 */
public class FetchALU {

    // name
    private String name;

    // number of bits that FetchALU can work with
    private int fetchWordSize;

    // maximum decimal number that FetchALU can work with
    private int fetchMaxNumber = (int) Math.pow(2, fetchWordSize);

    // operation
    private boolean[] op;

    public FetchALU() {
        this(8);
    }

    public FetchALU(int fetchWordSize) {
        name = "FetchALU";
        this.fetchWordSize = fetchWordSize;
        op = FetchCPU.OP_HLT;
    }

    public void encodeOp(boolean[] op) {
        this.op = op;
    }

    /**
     * Validate length of operands
     *
     * @param operands Operands
     * @throws InvalidParameterException Operand parameter missing
     * @throws IndexOutOfBoundsException Invalid array length
     */
    void validateLength(boolean[]... operands) {
        if (operands.length == 0) {
            throw new InvalidParameterException("Operand parameter is required");
        }
        for (boolean[] operand : operands) {
            if (operand.length != fetchWordSize) {
                throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
            }
        }
    }


    /**
     * Perform a N-bit operation with a Gate
     *
     * @param gate Gate
     * @param operands Operands
     */
    void gateOperation(Gate gate, boolean[]... operands) {
        // Check that the operands are correctly sized
        validateLength(operands);

        for (int i = 0; i < fetchWordSize; i++) {
            int index = fetchWordSize - i - 1;
            boolean[] args = new boolean[operands.length];

            // Build arguments
            for (int j = 0; j < operands.length; j++) {
                args[j] = operands[j][index];
            }

            // Perform operation
            gate.set(args);
            gate.execute();

            // Update last operand with result
            operands[operands.length - 1][index] = gate.getOutput();
        }
    }

    /*
     * Double Operand Instructions
     */

    /**
     * 8-bit logical bit-wise AND SSSS DDDD
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void andOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        gateOperation(new AndGate(), ssss, dddd);
    }

    /**
     * 8-bit logical bit-wise OR SSSS DDDD
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void orOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        gateOperation(new OrGate(), ssss, dddd);
    }

    /**
     * 8-bit logical bit-wise XOR SSSS DDDD
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void xorOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        gateOperation(new XorGate(), ssss, dddd);
    }

    /**
     * Add SSSS DDDD (Set C in PSW if necessary)
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void addOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        validateLength(ssss, dddd);

        FullAdder adder = new FullAdder();

        for (int i = 0; i < fetchWordSize; i++) {
            int index = fetchWordSize - i - 1;

            adder.set(ssss[index], dddd[index], psw.getC());
            adder.execute();
            dddd[index] = adder.sum;

            if (adder.carryOut) {
                psw.setC();
            } else {
                psw.clearC();
            }
        }

        if (psw.getC()) {
            psw.setV();
        }
    }

    /**
     * Subtract SSSS DDDD (Set N in PSW if necessary)
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void subOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        validateLength(ssss, dddd);

        boolean carryIn = true;
//        this.overflow = false;
        psw.clearV();

        for (int i = 0; i < fetchWordSize; i++) {
            int index = fetchWordSize - i - 1;
            boolean a = ssss[index], b = dddd[index];

            // Perform xor operations
            XorGate xor = new XorGate();
            xor.set(b, true);
            xor.execute();

            // Perform full adder operations
            FullAdder adder = new FullAdder();
            adder.set(a, xor.getOutput(), carryIn);
            adder.execute();

            // Save result
            dddd[index] = adder.sum;
            carryIn = adder.carryOut;
        }

        if (carryIn) {
            psw.setV();
        } else {
            psw.clearV();
        }
//        overflow = lastCarry;
    }

    /**
     * 8-bit MULT SSSS DDDD, put result in DDDD (Set N, C, V in PSW if necessary)
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void mulOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        validateLength(ssss, dddd);

        AndGate andGate = new AndGate();
        NBitALU alu = new NBitALU(fetchWordSize * 2);
        alu.setA(new boolean[fetchWordSize * 2]);

        for (int i = 0; i < fetchWordSize; i++) {
            boolean[] tmp = new boolean[fetchWordSize * 2];
            for (int j = 0; j < fetchWordSize; j++) {
                andGate.set(dddd[fetchWordSize - i - 1], ssss[fetchWordSize - j - 1]);
                andGate.execute();
                tmp[fetchWordSize * 2 - j - i - 1] = andGate.getOutput();
            }
            alu.setB(tmp);
            alu.execute(NBitALU.ADD);
            alu.setA(alu.getSumBits());
        }

        for (int i = 0; i < fetchWordSize * 2; i++) {
            if (i < fetchWordSize) {
                dddd[fetchWordSize - i - 1] = alu.getSumBit(fetchWordSize * 2 - i - 1);
            } else {
                if (alu.getSumBit(fetchWordSize * 2 - i - 1)) {
                    psw.setV();
                    break;
                }
            }
        }
    }

    /**
     * Shift bits to the right
     *
     * @param bits Boolean array
     */
    void shiftRight(boolean[] bits) {
        for (int i = bits.length - 1; i > 0; i--) {
            bits[i] = bits[i - 1];
        }
        bits[0] = false;
    }

    /**
     * Shift bits to the left
     *
     * @param bits Boolean array
     */
    void shiftLeft(boolean[] bits) {
        for (int i = 0; i < bits.length - 1; i++) {
            bits[i] = bits[i + 1];
        }
        bits[bits.length - 1] = false;
    }

    /**
     * Compare two binary numbers represented in a boolean array.
     *
     * @param a Boolean array
     * @param b Boolean array
     * @return  -1 if a > b,
     *          0 if a == b,
     *          1 if a < b
     */
    int compare(boolean[] a, boolean[] b) {
        XorGate xorGate = new XorGate();
        OrGate orGate = new OrGate();

        // Calculate offsets in the event of differing array lengths
        int aOffset = Math.max(0, b.length - a.length);
        int bOffset = Math.max(0, a.length - b.length);

        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            boolean aVal = false, bVal = false;

            // Get the value of a
            try {
                aVal = a[i - aOffset];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Defaults to false
            }

            try {
                bVal = b[i - bOffset];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Defaults to false
            }

            // Continue looping until a non-matching bit is found
            xorGate.set(aVal, bVal);
            xorGate.execute();
            if (!xorGate.getOutput()) {
                continue;
            }

            // Perform comparison
            orGate.set(aVal, bVal);
            orGate.execute();

            // Perform XOR to check for equality
            xorGate.set(orGate.getOutput(), aVal);
            xorGate.execute();
            if (!xorGate.getOutput()) {
                // a > b
                return -1;
            } else {
                // a < b
                return 1;
            }
        }

        // a == b
        return 0;
    }

    /**
     * 8-bit DIV SSSS DDDD, put result in DDDD (Set N, C, V in PSW if necessary)
     *
     * @param ssss Dividend
     * @param dddd Divisor
     * @param psw PSW
     */
    public void divOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        validateLength(ssss, dddd);
        boolean[] dividend = new boolean[fetchWordSize], quotient = new boolean[fetchWordSize];
        int index = 0;

        // Initialize the dividend
        dividend[dividend.length - 1] = ssss[0];

        while (index < dividend.length - 1) {
            // Check that the divisor is greater than the dividend
            while (compare(dddd, dividend) < 0) {
                // Shift dividend
                shiftLeft(dividend);
                dividend[dividend.length - 1] = ssss[++index];

                // Shift quotient
                shiftLeft(quotient);
                quotient[quotient.length - 1] = false;
            }

            // Shift quotient
            shiftLeft(quotient);
            quotient[quotient.length - 1] = true;

            boolean[] tmp = new boolean[quotient.length];
            tmp[quotient.length - 1] = true;

            // Multiply by divisor
            mulOp(dddd, tmp, psw);
            subOp(tmp, dividend, psw);

            if (index + 1 < dividend.length - 1) {
                // Shift dividend
                shiftLeft(dividend);
                dividend[dividend.length - 1] = ssss[++index];
            }
        }

        // Set carry flag if there is a remainder
        for (boolean bit : dividend) {
            if (bit) {
                psw.setC();
                break;
            }
        }

        // Move quotient to dddd
        movOp(quotient, dddd, psw);
    }

    /**
     * Move contents in SSSS to DDDD
     *
     * @param ssss SSSS
     * @param dddd DDDD
     * @param psw PSW
     */
    public void movOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        for (int i = 0; i < fetchWordSize; i++) {
            dddd[i] = ssss[i];
        }
    }

    /*
     * Single Operand Instructions
     */

    /**
     * Put zeros in DDDD. Set C in PSW.
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void clrOp(boolean[] dddd, FetchPSW psw) {
        for (int i = 0; i < dddd.length; i++) {
            dddd[i] = false;
        }
        psw.setC();

        psw.clearV();
        psw.clearN();
        psw.clearZ();
        psw.clearC();
    }

    /**
     * Put ones in DDDD
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void setOp(boolean[] dddd, FetchPSW psw) {
        for (int i = 0; i < dddd.length; i++) {
            dddd[i] = true;
        }
    }

    /**
     * Increment content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void incOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        addOp(ssss, dddd, psw);
    }

    /**
     * Decrement content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void decOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        subOp(ssss, dddd, psw);
    }

    /**
     * Negate contents of DDDD
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void negOp(boolean[] dddd, FetchPSW psw) {
        gateOperation(new NotGate(), dddd);
    }

    /*
     * Branching Ops
     */

    /**
     * Branch to DDDD if previous op is not zero
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void bneOp(boolean[] dddd, FetchPSW psw) {
        // TODO: Implement
    }

    /**
     * Branch to DDDD if previous op is zero
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void beqOp(boolean[] dddd, FetchPSW psw) {
        // TODO: Implement
    }

    /*
     * Halt
     */

    /**
     * Stop
     *
     * @param dddd DDDD
     * @param psw PSW
     */
    public void hltOp(boolean[] dddd, FetchPSW psw) {
        // TODO: Implement
    }

    /**
     * Convert boolean array to integer
     *
     * @param value Boolean array
     * @param psw PSW
     * @return Integer
     */
    public int booleanToInt(boolean[] value, FetchPSW psw) {
        if (psw.getZ()) {
            return 0;
        }

        double num = psw.getN() ? -Math.pow(2, fetchWordSize) : 0;
        num += (psw.getV() ? Math.pow(2, fetchWordSize) : 0);
        for (int i = 0; i < fetchWordSize; i++) {
            num += (value[i] ? Math.pow(2, fetchWordSize - i - 1) : 0);
        }
        return (int) num;
    }

    /**
     * Convert boolean array to integer
     *
     * @param value Boolean array
     * @return Integer
     */
    public int booleanToInt(boolean[] value) {
        double x = 0;
        for (int i = 0; i < value.length; i++) {
            x += (value[i] ? Math.pow(2, value.length - i - 1) : 0);
        }
        return (int) x;
    }

}
