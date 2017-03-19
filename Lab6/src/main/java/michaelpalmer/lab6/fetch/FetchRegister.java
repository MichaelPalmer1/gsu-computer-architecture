package michaelpalmer.lab6.fetch;

/**
 * Fetch Register
 */
public class FetchRegister {

    private int wordSize = 8;
    private String name;
    private boolean[] v;

    /**
     * Create new register
     */
    public FetchRegister() {
        this("Register");
    }

    /**
     * Create new register with custom name
     *
     * @param name Instance name
     */
    public FetchRegister(String name) {
        v = new boolean[wordSize];
        this.name = name;
    }

    /**
     * Set a bit
     *
     * @param index Bit index
     */
    public void setBit(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
        v[index] = true;
    }

    /**
     * Set all bits
     */
    public void setBits() {
        for (int i = 0; i < v.length; i++) {
            setBit(i);
        }
    }

    /**
     * Clear a bit
     *
     * @param index Bit index
     */
    public void clearBit(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
        v[index] = false;
    }

    /**
     * Clear all bits
     */
    public void clearBits() {
        for (int i = 0; i < v.length; i++) {
            clearBit(i);
        }
    }

    /**
     * Get value of a bit
     *
     * @param index Bit index
     * @return Boolean
     */
    public boolean getBit(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
        return v[index];
    }

    /**
     * Get bits
     *
     * @return Boolean array
     */
    public boolean[] getBits() {
        return v;
    }

    /**
     * Output the value of this register
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Generate string representation of this register
     *
     * @return String
     */
    public String toString() {
        String output = this.name + ":";
        for (boolean bit : v) {
            output += " " + bit;
        }
        return output;
    }

}
