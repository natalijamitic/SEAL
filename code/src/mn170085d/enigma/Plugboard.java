package mn170085d.enigma;

import mn170085d.Globals;

public class Plugboard {
    private int[] map = new int[26];

    public Plugboard(){
        this.reset();
    }

    public int getMapping(int a) {
        a = a >= Globals.A_CODE ? a - Globals.A_CODE : a;

        return this.map[a] == -1 ? a : this.map[a];
    }

    public void insertPair(int a, int b) {
        a = a >= Globals.A_CODE ? a - Globals.A_CODE : a;
        b = b >= Globals.A_CODE ? b - Globals.A_CODE : b;

        this.map[a] = b;
        this.map[b] = a;
    }

    public void removePair(int a) {
        a = a >= Globals.A_CODE ? a - Globals.A_CODE : a;

        this.map[this.map[a]] = -1;
        this.map[a] = -1;
    }

    public void reset() {
        for (int i = 0; i < Globals.ALPHABET_SIZE; i++) {
            map[i] = -1;
        }
    }
}
