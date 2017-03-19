package michaelpalmer.lab6;

import michaelpalmer.lab6.fetch.FetchALU;
import michaelpalmer.lab6.fetch.FetchPSW;

public class Main {

    public static void main(String[] args) {
        FetchALU alu = new FetchALU();
        FetchPSW psw = new FetchPSW();
        boolean[] a = {false, false, false, true, false, true, false, false};
        boolean[] b = {false, false, false, true, false, true, false, false};
        alu.mulOp(a, b, psw);
        System.out.println(b);
    }
}
