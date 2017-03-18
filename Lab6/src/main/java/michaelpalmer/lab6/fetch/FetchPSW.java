package michaelpalmer.lab6.fetch;

/**
 * Fetch PSW
 */
public class FetchPSW {

    // Negative, Zero, Overflow, Carry
    private boolean N = false, Z = false, V = false, C = false;
    protected String name;

    /**
     * Create new PSW instance
     */
    public FetchPSW() {
        this("PSW");
    }

    /**
     * Create a new PSW instance with custom name
     *
     * @param name Instance name
     */
    public FetchPSW(String name) {
        this.name = name;
    }

    /**
     * Set flags
     *
     * @param N Negative
     * @param Z Zero
     * @param V Overflow
     * @param C Carry
     */
    public void setFlags(boolean N, boolean Z, boolean V, boolean C) {
        this.N = N;
        this.Z = Z;
        this.V = V;
        this.C = C;
    }

    /**
     * Enable the N flag
     */
    public void setN() {
        N = true;
    }

    /**
     * Disable the N flag
     */
    public void clearN() {
        N = false;
    }

    /**
     * Get the value of the N flag
     *
     * @return Boolean
     */
    public boolean getN() {
        return N;
    }

    /**
     * Enable the Z flag
     */
    public void setZ() {
        Z = true;
    }

    /**
     * Disable the Z flag
     */
    public void clearZ() {
        Z = false;
    }

    /**
     * Get the value of the Z flag
     *
     * @return Boolean
     */
    public boolean getZ() {
        return Z;
    }

    /**
     * Enable the V flag
     */
    public void setV() {
        V = true;
    }

    /**
     * Disable the V flag
     */
    public void clearV() {
        V = false;
    }

    /**
     * Get the value of the V flag
     *
     * @return Boolean
     */
    public boolean getV() {
        return V;
    }

    /**
     * Enable the C flag
     */
    public void setC() {
        C = true;
    }

    /**
     * Disable the C flag
     */
    public void clearC() {
        C = false;
    }

    /**
     * Get the value of the C flag
     *
     * @return Boolean
     */
    public boolean getC() {
        return C;
    }

    /**
     * Output the value of this PSW
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Generate string representation of this PSW
     *
     * @return String
     */
    public String toString() {
        return String.format("%s: N=%b, Z=%b, V=%b, C=%b", name, N, Z, V, C);
    }

}
