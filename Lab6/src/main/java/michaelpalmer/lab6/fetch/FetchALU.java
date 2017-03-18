package michaelpalmer.lab6.fetch;

import michaelpalmer.lab6.alu.NBitALU;
import michaelpalmer.lab6.alu.adders.FullAdder;
import michaelpalmer.lab6.alu.gates.AndGate;
import michaelpalmer.lab6.alu.gates.NotGate;
import michaelpalmer.lab6.alu.gates.OrGate;
import michaelpalmer.lab6.alu.gates.XorGate;

/**
 * Fetch ALU
 */
public class FetchALU {

    private String name;

    // number of bits that FetchALU can work with
    private int fetchWordSize;

    // maximum decimal number that FetchALU can work with
    private int fetchMaxNumber = (int) Math.pow(2, fetchWordSize);

    private int op;

    public FetchALU() {
        this(8);
    }

    public FetchALU(int fetchWordSize) {
        name = "FetchALU";

        // 0 => A+B; 1 => A-B
        op = 0;

        this.fetchWordSize = fetchWordSize;
    }

    public void encodeOp(boolean[] op) {

    }

    /*
     * Double Operand Instructions
     */

    /**
     * 8-bit logical bit-wise AND DDDD SSSS
     *
     * @param dddd
     * @param ssss
     * @param psw PSW
     * @return Result
     */
    public boolean[] andOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] result = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean d = dddd[fetchWordSize - i - 1], s = ssss[fetchWordSize - i - 1];
            AndGate and = new AndGate();
            and.set(d, s);
            and.execute();
            result[fetchWordSize - i - 1] = and.getOutput();
        }

        return result;
    }

    /**
     * 8-bit logical bit-wise OR SSSS DDDD
     *
     * @param ssss
     * @param dddd
     * @param psw PSW
     * @return Result
     */
    public boolean[] orOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        if (ssss.length != fetchWordSize || dddd.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] result = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean s = ssss[fetchWordSize - i - 1];
            boolean d = dddd[fetchWordSize - i - 1];
            OrGate or = new OrGate();
            or.set(s, d);
            or.execute();
            result[fetchWordSize - i - 1] = or.getOutput();
        }

        return result;
    }

    /**
     * 8-bit logical bit-wise XOR DDDD SSSS
     *
     * @param dddd
     * @param ssss
     * @param psw
     * @return Result
     */
    public boolean[] xorOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] result = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean d = dddd[fetchWordSize - i - 1];
            boolean s = ssss[fetchWordSize - i - 1];
            XorGate xor = new XorGate();
            xor.set(d, s);
            xor.execute();
            result[fetchWordSize - i - 1] = xor.getOutput();
        }

        return result;
    }

    /**
     * ADD DDDD SSSS (Set C in PSW if necessary)
     *
     * @param dddd
     * @param ssss
     * @param psw
     * @return Result
     */
    public boolean[] addOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] result = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            FullAdder adder = new FullAdder();
            adder.set(dddd[fetchWordSize - i - 1], ssss[fetchWordSize - i - 1], psw.getC());
            adder.execute();
            result[fetchWordSize - i - 1] = adder.sum;
            if (adder.carryOut) {
                psw.setC();
            } else {
                psw.clearC();
            }
        }

        if (psw.getC()) {
            psw.setV();
        }

        return result;
    }

    /**
     * SUB SSSS DDDD (Set N in PSW if necessary)
     *
     * @param ssss
     * @param dddd
     * @param psw
     * @return Result
     */
    public boolean[] subOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean carryIn = true;

//        this.overflow = false;
        psw.clearV();

        boolean[] x = new boolean[fetchWordSize];
        boolean[] carry = new boolean[fetchWordSize];
        boolean[] result = new boolean[fetchWordSize];
        for (int i = 0; i < fetchWordSize; i++) {
            boolean a = dddd[fetchWordSize - i - 1];
            boolean b = ssss[fetchWordSize - i - 1];
            XorGate xor = new XorGate();
            xor.set(b, true);
            xor.execute();
            x[fetchWordSize - i - 1] = xor.getOutput();

            FullAdder adder = new FullAdder();
            adder.set(a, xor.getOutput(), carryIn);
            adder.execute();
            result[fetchWordSize - i - 1] = adder.sum;
            carry[fetchWordSize - i - 1] = adder.carryOut;
            carryIn = adder.carryOut;
        }

        carry[fetchWordSize - 1] = true;

        if (carryIn) {
            psw.setV();
        } else {
            psw.clearV();
        }
//        overflow = lastCarry;
        return result;
    }

    /**
     * 8-bit MULT DDDD SSSS, put result in DDDD (Set N, C, V in PSW if necessary)
     *
     * @param dddd
     * @param ssss
     * @param psw
     * @return Result
     */
    public boolean[] mulOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }
        boolean[] result = new boolean[fetchWordSize];

        AndGate andGate = new AndGate();
        NBitALU alu = new NBitALU(fetchWordSize * 2);
        alu.setA(new boolean[fetchWordSize * 2]);

        for (int i = 0; i < fetchWordSize; i++) {
            boolean[] tmp = new boolean[fetchWordSize * 2];
            for (int j = 0; j < fetchWordSize; j++) {
                int index = fetchWordSize * 2 - j - i - 1;
                andGate.set(dddd[fetchWordSize - i - 1], ssss[fetchWordSize - j - 1]);
                andGate.execute();
                tmp[index] = andGate.getOutput();
            }
            alu.setB(tmp);
            alu.execute(NBitALU.ADD);
            alu.setA(alu.getSumBits());
        }

        for (int i = 0; i < fetchWordSize * 2; i++) {
            if (i < fetchWordSize) {
                result[fetchWordSize - i - 1] = alu.getSumBit(fetchWordSize * 2 - i - 1);
            } else {
                if (alu.getSumBit(fetchWordSize * 2 - i - 1)) {
                    psw.setV();
                    break;
                }
            }
        }

        return result;
    }

    private int firstSignificantBit(boolean[] bits) {
        int index = 0;
        for (boolean bit: bits) {
            if (bit) {
                return index;
            }
            index++;
        }

        return -1;
    }

    private boolean[] shiftRight(boolean[] bits) {
        boolean[] result = new boolean[bits.length];
        for (int i = 0; i < bits.length - 1; i++) {
            result[i + 1] = bits[i];
        }
        return result;
    }

    private boolean[] shiftLeft(boolean[] bits) {
        boolean[] result;
        if (bits[0]) {
            result = new boolean[bits.length + 1];
            for (int i = 0; i < bits.length; i++) {
                result[i] = bits[i];
            }
        } else {
            result = new boolean[bits.length];
            for (int i = 0; i < bits.length - 1; i++) {
                result[i] = bits[i + 1];
            }
        }
        return result;
    }

    private void shiftLeftInline(boolean[] bits) {
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
    private int compare(boolean[] a, boolean[] b) {
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
     * 8-bit DIV DDDD SSSS, put result in DDDD (Set N, C, V in PSW if necessary)
     *
     * @param dddd Dividend
     * @param ssss Divisor
     * @param psw PSW
     * @return Result
     */
    public boolean[] divOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }
        boolean[] dividend = new boolean[fetchWordSize], quotient = new boolean[fetchWordSize];
        int index = 0;

        // Initialize the dividend
        dividend[dividend.length - 1] = dddd[0];

        while (index < dividend.length - 1) {
            // Check that the divisor is greater than the dividend
            while (compare(ssss, dividend) < 0) {
                // Shift dividend
                shiftLeftInline(dividend);
                dividend[dividend.length - 1] = dddd[++index];

                // Shift quotient
                shiftLeftInline(quotient);
                quotient[quotient.length - 1] = false;
            }

            // Shift quotient
            shiftLeftInline(quotient);
            quotient[quotient.length - 1] = true;

            boolean[] tmp = new boolean[quotient.length];
            tmp[quotient.length - 1] = true;

            // Multiply by divisor
            boolean[] mulResult = mulOp(tmp, ssss, psw);
            dividend = subOp(dividend, mulResult, psw);

            if (index + 1 < dividend.length - 1) {
                // Shift dividend
                shiftLeftInline(dividend);
                dividend[dividend.length - 1] = dddd[++index];
            }
        }

        // Set carry flag if there is a remainder
        for (boolean bit : dividend) {
            if (bit) {
                psw.setC();
                break;
            }
        }

        return quotient;
    }

    /**
     * Move contents in SSSS to DDDD
     *
     * @param dddd
     * @param ssss
     * @param psw
     */
    public void movOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
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
     * @param dddd
     * @param psw
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
     * @param dddd
     * @param psw
     */
    public void setOp(boolean[] dddd, FetchPSW psw) {
        for (int i = 0; i < dddd.length; i++) {
            dddd[i] = true;
        }
    }

    /**
     * Increment content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd
     * @param psw
     * @return Result
     */
    public boolean[] incOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        return addOp(dddd, ssss, psw);
    }

    /**
     * Decrement content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd
     * @param psw
     * @return Result
     */
    public boolean[] decOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        return subOp(dddd, ssss, psw);
    }

    /**
     * Negate contents of DDDD
     *
     * @param dddd
     * @param psw
     */
    public void negOp(boolean[] dddd, FetchPSW psw) {
        NotGate notGate = new NotGate();
        for (int i = 0; i < dddd.length; i++) {
            notGate.set(dddd[i]);
            notGate.execute();
            dddd[i] = notGate.getOutput();
        }
    }

    /*
     * Branching Ops
     */

    /**
     * Branch to DDDD if previous op is not zero
     *
     * @param dddd
     * @param psw
     */
    public void bneOp(boolean[] dddd, FetchPSW psw) {

    }

    /**
     * Branch to DDDD if previous op is zero
     *
     * @param dddd
     * @param psw
     */
    public void beqOp(boolean[] dddd, FetchPSW psw) {

    }

    /*
     * Halt
     */

    /**
     * Stop
     *
     * @param dddd
     * @param psw
     */
    public void hltOp(boolean[] dddd, FetchPSW psw) {

    }

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

    public int booleanToInt(boolean[] value) {
        double x = 0;
        for (int i = 0; i < value.length; i++) {
            x += (value[i] ? Math.pow(2, value.length - i - 1) : 0);
        }
        return (int) x;
    }

}
