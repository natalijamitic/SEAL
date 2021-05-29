package mn170085d.enigma;

import mn170085d.Globals;

public class Rotor {
    private char turnover;
    private int offset = 0;
    private int ring = 0;
    private char[] encryptMap;
    private char[] decryptMap = new char[26];

    public Rotor(RotorConfig configuration) {
        this.turnover = configuration.getTurnover();
        this.encryptMap = configuration.getWiring().toCharArray();
        for (int i = 0; i < Globals.ALPHABET_SIZE; i++) {
            char letter = this.encryptMap[i];
            this.decryptMap[letter - Globals.A_CODE] = (char)(i + Globals.A_CODE);
        }
    }

    public Rotor(RotorConfig configuration, String ring, String start) {
        this(configuration);
        this.ring = ring.charAt(0) - Globals.A_CODE;
        this.offset = start.charAt(0) - Globals.A_CODE;
    }

    public int crypt(int input, int mode) {
        char[] map = (mode == Globals.ENCRYPT) ? this.encryptMap : this.decryptMap;
        int inputWheel = (Globals.ALPHABET_SIZE + input + this.offset - this.ring) % Globals.ALPHABET_SIZE;
        int outputWheel = map[inputWheel] - Globals.A_CODE;
        int output = (Globals.ALPHABET_SIZE + outputWheel - this.offset + this.ring) % Globals.ALPHABET_SIZE;

        return output;
    }

    public void rotate() {
        this.offset = (this.offset + 1) % Globals.ALPHABET_SIZE;
    }

    public boolean isTurnover() {
        return this.turnover - Globals.A_CODE == this.offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        offset = offset >= Globals.A_CODE ? offset - Globals.A_CODE : offset;

        this.offset = offset;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        ring = ring >= Globals.A_CODE ? ring - Globals.A_CODE : ring;

        this.ring = ring;
    }

    public char[] getEncryptMap() {
        return encryptMap;
    }
}
