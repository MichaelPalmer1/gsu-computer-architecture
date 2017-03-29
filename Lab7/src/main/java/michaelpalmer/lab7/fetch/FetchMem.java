package michaelpalmer.lab7.fetch;

/**
 * Fetch Mem
 */
public class FetchMem {

    public static final boolean TYPE_PROG = true, TYPE_DATA = false;
    private static final int DEFAULT_SIZE = 1000, DEFAULT_OFFSET = 0000;
    private static final boolean DEFAULT_TYPE = TYPE_DATA;

    private byte[] v;
    private cell[] mem;
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
        mem = new cell[size];
        for (int i = 0; i < mem.length; i++) {
            mem[i] = new cell();
        }
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

    public void setCell(int index) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            mem[index].set();
        }
    }

    public void setCell(int index, boolean[] code, boolean[] dddd) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else if (code.length != 4) {
            throw new IllegalArgumentException("Code must be 4 bits long");
        } else if (dddd.length != 8) {
            throw new IllegalArgumentException("DDDD must be 8 bits long");
        } else {
            mem[index].set(code, dddd);
        }
    }

    public void setCell(int index, boolean[] code, boolean[] ssss, boolean[] dddd) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else if (code.length != 4) {
            throw new IllegalArgumentException("Code must be 4 bits long");
        } else if (ssss.length != 8) {
            throw new IllegalArgumentException("SSSS must be 8 bits long");
        } else if (dddd.length != 8) {
            throw new IllegalArgumentException("DDDD must be 8 bits long");
        } else {
            mem[index].set(code, ssss, dddd);
        }
    }

    public void clearCell(int index) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            mem[index].clear();
        }
    }

    public boolean[] getCode(int index) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            cell c = mem[index];
            return c.getCode();
        }
    }

    public boolean[] getS(int index) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            cell c = mem[index];
            return c.getS();
        }
    }

    public boolean[] getD(int index) {
        if (index > mem.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            cell c = mem[index];
            return c.getD();
        }
    }

    public static boolean[] intToBoolean(int number) {
        boolean[] dddd = new boolean[8];
        for (int i = 0; i < dddd.length; i++) {
            dddd[dddd.length - i - 1] = (1 << i & number) != 0;
        }
        return dddd;
    }

    public static void intToBoolean(int number, boolean[] dddd) {
        for (int i = 0; i < dddd.length; i++) {
            dddd[dddd.length - i - 1] = (1 << i & number) != 0;
        }
    }

    public void testMem() {
        boolean[] code, ssss, dddd;
        code = new boolean[4];
        ssss = new boolean[8];
        dddd = new boolean[8];

        for (int i = 0; i < 8; i++) {
            intToBoolean(i, code);
            for (boolean aCode : code) {
                System.out.print(aCode + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < 8; i++) {
            intToBoolean(i, code);
            intToBoolean(i, ssss);
            intToBoolean(i, dddd);
            setCell(i, code, ssss, dddd);
            print(i);
        }
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
        cell c = mem[index];
        System.out.print("MEM: " + index + ": ");
        c.print();
        System.out.println();
    }

    private class cell {
        private int cellSize = 20;
        private boolean[] c;

        cell() {
            c = new boolean[cellSize];
        }

        public void set(boolean[] code, boolean[] ssss, boolean[] dddd) {
            int j = 0;
            for (boolean aCode : code) {
                c[j++] = aCode;
            }
            for (boolean s : ssss) {
                c[j++] = s;
            }
            for (boolean d : dddd) {
                c[j++] = d;
            }
        }

        public void set(boolean[] code, boolean[] dddd) {
            int j = 0;
            for (boolean aCode : code) {
                c[j++] = aCode;
            }
            j+= dddd.length;
            for (boolean aDddd : dddd) {
                c[j++] = aDddd;
            }
        }

        public void set() {
            for (int i = 0; i < c.length; i++) {
                c[i] = true;
            }
        }

        public void clear() {
            for (int i = 0; i < c.length; i++) {
                c[i] = false;
            }
        }

        public boolean[] getCode() {
            boolean[] code = new boolean[4];
            System.arraycopy(c, 0, code, 0, code.length);
            return code;
        }

        public boolean[] getS() {
            boolean[] s = new boolean[8];
            System.arraycopy(c, 4, s, 0, s.length);
            return s;
        }

        public boolean[] getD() {
            boolean[] d = new boolean[8];
            System.arraycopy(c, 12, d, 0, d.length);
            return d;
        }

        public void print() {
            for (boolean aC : c) {
                System.out.print(aC + " ");
            }
        }
    }

}
