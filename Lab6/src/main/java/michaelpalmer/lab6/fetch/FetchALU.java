package michaelpalmer.lab6.fetch;

import com.sun.javaws.exceptions.InvalidArgumentException;
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

    protected String name;

    // number of bits that FetchALU can work with
    public int fetchWordSize = 8;

    // maximum decimal number that FetchALU can work with
    public int fetchMaxNumber = (int) Math.pow(2, fetchWordSize);

    public XorGate[] xorGates;
    public AndGate[] andGates;
    public OrGate[] orGates;
    public NotGate[] notGates;
    public FullAdder[] fullAdders;

    public int op;

    public FetchPSW psw; // for testing purposes
    public boolean[] test; // for testing purposes
    public boolean[] aTest; // for testing purposes
    public boolean[] bTest; // for testing purposes;

    public FetchALU() {
        name = "FetchALU";

        psw = new FetchPSW(); // for testing
        test = new boolean[fetchWordSize];
        aTest = new boolean[fetchWordSize];
        bTest = new boolean[fetchWordSize];

        // 0 => A+B; 1 => A-B
        op = 0;

        fullAdders =  new FullAdder[fetchWordSize];
        xorGates = new XorGate[fetchWordSize];
        andGates = new AndGate[fetchWordSize];
        orGates = new OrGate[fetchWordSize];
        notGates = new NotGate[fetchWordSize];

        // TODO: setup wires (boolean arrays?)

        for (int i = 0; i < fetchWordSize; i++) {
            String s = Integer.toString(i);
            xorGates[i] = new XorGate();
            andGates[i] = new AndGate();
            orGates[i] = new OrGate();
            notGates[i] = new NotGate();
            fullAdders[i] = new FullAdder("fa" + s);
        }

    }

    public void encodeOp(boolean[] op) {

    }

    /*
     * Double Operand Instructions
     */

    /**
     * 8-bit logical bit-wise AND SSSS DDDD
     *
     * @param ssss
     * @param dddd
     * @param psw PSW
     */
    public void andOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        if (ssss.length != fetchWordSize || dddd.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] sum = new boolean[fetchWordSize];
        boolean[] carry = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean s = ssss[fetchWordSize - i - 1];
            boolean d = dddd[fetchWordSize - i - 1];
            AndGate and = new AndGate();
            and.set(s, d);
            and.execute();
            sum[fetchWordSize - i - 1] = and.getOutput();
            carry[fetchWordSize - i - 1] = false;
        }

        // TODO: Store result somewhere
    }

    /**
     * 8-bit logical bit-wise OR SSSS DDDD
     *
     * @param ssss
     * @param dddd
     * @param psw PSW
     */
    public void orOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        if (ssss.length != fetchWordSize || dddd.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] sum = new boolean[fetchWordSize];
        boolean[] carry = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean s = ssss[fetchWordSize - i - 1];
            boolean d = dddd[fetchWordSize - i - 1];
            OrGate or = new OrGate();
            or.set(s, d);
            or.execute();
            sum[fetchWordSize - i - 1] = or.getOutput();
            carry[fetchWordSize - i - 1] = false;
        }

        // TODO: Store result somewhere
    }

    /**
     * 8-bit logical bit-wise XOR SSSS DDDD
     *
     * @param ssss
     * @param dddd
     * @param psw
     */
    public void xorOp(boolean[] ssss, boolean[] dddd, FetchPSW psw) {
        if (ssss.length != fetchWordSize || dddd.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        boolean[] sum = new boolean[fetchWordSize];
        boolean[] carry = new boolean[fetchWordSize];

        for (int i = 0; i < fetchWordSize; i++) {
            boolean s = ssss[fetchWordSize - i - 1];
            boolean d = dddd[fetchWordSize - i - 1];
            XorGate xor = new XorGate();
            xor.set(s, d);
            xor.execute();
            sum[fetchWordSize - i - 1] = xor.getOutput();
            carry[fetchWordSize - i - 1] = false;
        }

        // TODO: Store result somewhere
    }

    /**
     * ADD DDDD SSSS (Set C in PSW if necessary)
     *
     * @param dddd
     * @param ssss
     * @param psw
     */
    public void addOp(boolean[] dddd, boolean[] ssss, FetchPSW psw) {
        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
        }

        String a = "", b = "", sum = "", carryVal = "", divider = "";
        boolean[] carry = new boolean[fetchWordSize];
        int aVal;

        for (int i = 0; i < fetchWordSize; i++) {
            FullAdder adder = new FullAdder();
            adder.set(dddd[fetchWordSize - i - 1], ssss[fetchWordSize - i - 1], psw.getC());
            adder.execute();
            test[fetchWordSize - i - 1] = adder.sum;
            carry[fetchWordSize - i - 1] = adder.carryOut;
            if (adder.carryOut) {
                psw.setC();
            } else {
                psw.clearC();
            }
        }

        for (int i = 0; i < fetchWordSize; i++) {
            a += dddd[i] ? "1" : "0";
            b += ssss[i] ? "1" : "0";
            carryVal += carry[i] ? "1" : "0";
            sum += test[i] ? "1" : "0";
            divider += "-";
        }
        aVal = booleanToInt(dddd, psw);

        if (psw.getC()) {
            psw.setV();
        }

        System.out.printf("Carry:    %s\n", carryVal);
        System.out.printf("          %s\n", divider);
        System.out.printf("Input A:  %s   (%d)\n", a, aVal);

        System.out.printf("Input B:  %s %s (%d)\n", b, "+", booleanToInt(ssss));

        System.out.printf("          %s\n", divider);
        System.out.printf("Result: %d %s   (%d)\n\n", psw.getV() ? 1 : 0, sum, booleanToInt(test, psw));
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
//        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
//            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
//        }

//        String a = "", b = "", xVal = "", sum = "", carryVal = "", divider = "";
//        boolean[] carry = new boolean[fetchWordSize], x = new boolean[fetchWordSize], result = new boolean[fetchWordSize];

//        int aVal = booleanToInt(dddd, psw), bVal = booleanToInt(ssss);
//        for (int i = 0; i < fetchWordSize; i++) {
//            a += dddd[i] ? "1" : "0";
//            b += ssss[i] ? "1" : "0";
//        }
//        psw.setN();
//        psw.setC();

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



        /*

        for (int i = 0; i < fetchWordSize; i++) {
            XorGate xor = new XorGate();
            xor.set(ssss[fetchWordSize - i - 1], true);
            xor.execute();
            x[fetchWordSize - i - 1] = xor.getOutput();

            FullAdder adder = new FullAdder();
            adder.set(dddd[fetchWordSize - i - 1], x[fetchWordSize - i - 1], psw.getC());
            adder.execute();
            result[fetchWordSize - i - 1] = adder.sum;
            carry[fetchWordSize - i - 1] = adder.carryOut;
//            if (adder.carryOut) {
//                psw.setC();
//            } else {
//                psw.clearC();
//            }
        }

        for (int i = 0; i < fetchWordSize; i++) {
            xVal += x[i] ? "1" : "0";
            carryVal += carry[i] ? "1" : "0";
            sum += result[i] ? "1" : "0";
            divider += "-";
        }

//        if (psw.getC()) {
//            psw.setV();
//        }

*/
        /*
        boolean allZero = true;
        for (int i = 0; i < test.length; i++) {
            if (test[i]) {
                allZero = false;
                break;
            }
        }
        boolean allOne = true;
        for (int i = 0; i < test.length; i++) {
            if (!test[i]) {
                allOne = false;
                break;
            }
        }

        if (!psw.getV() && allOne) {
            psw.setN();
        } else if (!psw.getV() && allZero) {
            psw.clearN();
        }

        if (allZero) {
            psw.setZ();
            psw.clearV();
            psw.clearN();
        } else if (allOne) {
            psw.setN();
            psw.clearZ();
        } else {
            psw.clearZ();
        }
        */
/*
        System.out.printf("Carry:    %s\n", carryVal);
        System.out.printf("          %s\n", divider);
        System.out.printf("Input A:  %s   (%03d)\n", a, aVal);
        System.out.printf("Input B:  %s %s (%03d) [%s before XOR]\n", xVal, "-", bVal, b);
        System.out.printf("          %s\n", divider);
        System.out.printf("Result: %d %s   (%03d)\n\n", psw.getV() ? 1 : 0, sum, booleanToInt(result, psw));
*/
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
//        if (dddd.length != fetchWordSize || ssss.length != fetchWordSize) {
//            throw new IndexOutOfBoundsException("Array length must be " + fetchWordSize);
//        }
        boolean[] result = new boolean[fetchWordSize];

        AndGate andGate = new AndGate();
        NBitALU alu = new NBitALU(fetchWordSize * 2);
        alu.setA(new boolean[fetchWordSize * 2]);

//        System.out.println("DDDD: " + booleanToInt(dddd, psw));
//        System.out.println("SSSS: " + booleanToInt(ssss, psw));

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
//            System.out.println("A: " + alu.getA());
//            System.out.println("B: " + alu.getB());
//            System.out.println("Sum: " + alu.getSum());
//            System.out.println("----------");
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
        test = ssss;
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
        test = dddd;
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
        test = dddd;
    }

    /**
     * Increment content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd
     * @param psw
     */
    public void incOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        addOp(dddd, ssss, psw);
    }

    /**
     * Decrement content of DDDD by one (set N, C, V in PSW if necessary)
     *
     * @param dddd
     * @param psw
     */
    public void decOp(boolean[] dddd, FetchPSW psw) {
        boolean[] ssss = new boolean[fetchWordSize];
        ssss[fetchWordSize - 1] = true;
        subOp(dddd, ssss, psw);
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

    public int nGateCounter() {
        return 1;
    }

    public void testALU() {
        boolean[] d = new boolean[] {false, false, false, true, true, false, true, true};
        boolean[] s = new boolean[] {false, false, false, false, false, true, false, true};


        boolean[] a = new boolean[] {false, false, false, false, false, true, true, false};
        boolean[] b = new boolean[] {false, false, false, false, false, true, false, true};

        boolean[] r = divOp(d, s, psw);
        System.out.print("RESULT: ");
        for (boolean bit: r) {
            System.out.print(bit ? "1": "0");
        }
        System.out.println();


//        int val;
//        clrOp(test, psw);
//        String s;
//        psw.print();
//        System.out.println("incOp()");
//        for (int i = 0; i < 300; i++) {
//            incOp(test, psw);
//            val = booleanToInt(test);
//            s = String.format("%03d", val);
//            System.out.print(s + " ");
//            psw.print();
//        }

        /*
        clrOp(test, psw);

        for (int i = 0; i < test.length; i++) {
            test[i] = true;
        }

        System.out.println("decOp()");
        psw = new FetchPSW();
        for (int i = 0; i < 300; i++) {
            decOp(test, psw);
            val = booleanToInt(test);
            s = String.format("%03d", val);
            System.out.print(s + " ");
            psw.print();
        }
        */

        /*
        decOp(test, psw);
        psw.print();
        */

//        boolean[] a = new boolean[fetchWordSize];
//        boolean[] b = new boolean[fetchWordSize];
////        for(int i = 0; i < fetchWordSize; i++) {
////            a[i] = true;
////            b[i] = true;
////        }
//        a[6] = true;
//        a[6] = true;
//
//        divOp(a, b, psw);
//        System.out.print("test = ");
//        for(int i = 0; i < test.length; i++) {
//            System.out.print(test[i] ? 1 : 0);
//        }
//        System.out.println("\nResult: " + booleanToInt(test, psw));
//        psw.print();

//
//        // TODO: val = nGateCounter();
//
//        aTest[0] = bTest[0] = true;
//        aTest[1] = bTest[1] = false;
//        aTest[2] = bTest[2] = true;
//        aTest[3] = bTest[3] = false;
//        aTest[4] = bTest[4] = true;
//        aTest[5] = bTest[5] = false;
//        aTest[6] = bTest[6] = true;
//        aTest[7] = bTest[7] = false;
//
//        andOp(aTest, bTest, psw);
//
//        aTest[0] = true; bTest[0] = false;
//        aTest[1] = false; bTest[1] = true;
//        aTest[2] = true; bTest[2] = false;
//        aTest[3] = false; bTest[3] = true;
//        aTest[4] = true; bTest[4] = false;
//        aTest[5] = false; bTest[5] = true;
//        aTest[6] = true; bTest[6] = false;
//        aTest[7] = false; bTest[7] = true;

    }

}
