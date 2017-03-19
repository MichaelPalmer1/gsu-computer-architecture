package michaelpalmer.lab6.fetch;

/**
 * Fetch Mem
 */
public class FetchMem {

    public static final boolean TYPE_PROG = true, TYPE_DATA = false;
    private static final int DEFAULT_SIZE = 1000, DEFAULT_OFFSET = 0000;
    private static final boolean DEFAULT_TYPE = TYPE_DATA;

    private byte[] v;
    private int offset, size;
    private boolean type; // {prog=1, data=0};

    /**
     * Create new instance of FetchMem
     */
    public FetchMem() {
        this(DEFAULT_SIZE, DEFAULT_OFFSET, DEFAULT_TYPE);
    }

    /**
     * Create new instance of FetchMem with specified type
     *
     * @param type TYPE_PROG or TYPE_DATA
     */
    public FetchMem(boolean type) {
        this(DEFAULT_SIZE, DEFAULT_OFFSET, type);
    }

    /**
     * Create new instance of FetchMem with specified size
     *
     * @param size Size
     */
    public FetchMem(int size) {
        this(size, DEFAULT_OFFSET, DEFAULT_TYPE);
    }

    /**
     * Create new instance of FetchMem with specified size and type
     *
     * @param size Size
     * @param type TYPE_PROG or TYPE_DATA
     */
    public FetchMem(int size, boolean type) {
        this(size, DEFAULT_OFFSET, type);
    }

    /**
     * Create new instance of FetchMem with specified size, offset, and type
     *
     * @param size Size
     * @param offset Offset
     * @param type TYPE_PROG or TYPE_DATA
     */
    public FetchMem(int size, int offset, boolean type) {
        this.size = size;
        this.offset = offset;
        this.type = type;
        v = new byte[size];
    }

    /**
     * Get the type of this instance
     *
     * @return Boolean
     */
    public boolean getType() {
        return type;
    }

    /**
     * Get the size of this instance
     *
     * @return Integer
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the offset of this instance
     *
     * @return Integer
     */
    public int getOffset() {
        return offset;
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
     * Get the value of a byte
     *
     * @param index Byte index
     * @return Byte
     */
    public byte getByte(int index) {
        if (index > v.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return v[index];
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

}
