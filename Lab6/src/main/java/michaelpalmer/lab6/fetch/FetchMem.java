package michaelpalmer.lab6.fetch;

/**
 * Fetch Mem
 */
public class FetchMem {

    private byte[] v;
    public int offset = 0000;
    public int size = 1000;
    public boolean type; // {prog=1, data=0};

    /**
     * Create new instance of Mem
     */
    public FetchMem() {
        v = new byte[size];
    }

    /**
     * Set a byte
     *
     * @param index Byte index
     */
    public void setByte(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        v[index] = 127;
    }

    /**
     * Clear a byte
     *
     * @param index Byte index
     */
    public void clearByte(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        v[index] = 0;
    }

    /**
     * Output the value of a byte
     *
     * @param index Byte index
     */
    public void print(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        System.out.println("MEM: " + index + ": " + v[index]);
    }

    public void testMem() {
        print(10);
        print(20);

        setByte(10);
        setByte(20);

        print(10);
        print(20);
    }

}
