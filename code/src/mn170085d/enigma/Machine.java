package mn170085d.enigma;

import mn170085d.Globals;

import java.util.Arrays;
import java.util.Collections;

public class Machine {
    private Rotor[] rotors = new Rotor[3];
    private Reflector reflector;
    private Plugboard plugboard = new Plugboard();
    private int lastRotation = 0;

    public Machine(String right, String middle, String left, String reflector, String rightRing, String rightStart, String middleRing, String middleStart, String leftRing, String leftStart) {
        this.rotors[0] = new Rotor(Globals.AVAILABLE_ROTORS.get(right), rightRing, rightStart);
        this.rotors[1] = new Rotor(Globals.AVAILABLE_ROTORS.get(middle), middleRing, middleStart);
        this.rotors[2] = new Rotor(Globals.AVAILABLE_ROTORS.get(left), leftRing, leftStart);
        this.reflector = new Reflector(Globals.AVAILABLE_REFLECTORS.get(reflector));
    }

    public Machine(String right, String middle, String left, String reflector) {
        this(right, middle, left, reflector, Globals.DEFAULT_MACHINE_CONFIG, Globals.DEFAULT_MACHINE_CONFIG, Globals.DEFAULT_MACHINE_CONFIG, Globals.DEFAULT_MACHINE_CONFIG, Globals.DEFAULT_MACHINE_CONFIG, Globals.DEFAULT_MACHINE_CONFIG);
    }

    public String cryptText(String text) {
        StringBuilder result = new StringBuilder();

        for (char letter: text.toCharArray()) {
            boolean isLetter = (Character.toUpperCase(letter) >= Globals.A_CODE) && (Character.toUpperCase(letter) <= Globals.Z_CODE);
            if (!isLetter) {
                result.append(letter);
                continue;
            }

            boolean isLowerCase = Character.isLowerCase(letter);
            letter = this.cryptLetter(Character.toUpperCase(letter));
            letter = isLowerCase ? Character.toLowerCase(letter) : Character.toUpperCase(letter);
            result.append(letter);
        }

        return result.toString();
    }

    public int cryptPlugboard(int input) {
        return this.plugboard.getMapping(input);
    }

    public int cryptRotor(Rotor rotor, int input, int mode) {
        return rotor.crypt(input, mode);
    }

    public int cryptReflector(int input) {
        return reflector.crypt(input);
    }

    private char cryptLetter(char letter) {
        int input = letter - Globals.A_CODE;

        rotateRotors();

        input = cryptPlugboard(input);

        for (Rotor rotor: this.rotors) {
            input = cryptRotor(rotor, input, Globals.ENCRYPT);
        }

        input = cryptReflector(input);

        Rotor[] reversedRotorList = this.rotors.clone();
        Collections.reverse(Arrays.asList(reversedRotorList));

        for (Rotor rotor: reversedRotorList) {
            input = cryptRotor(rotor, input, Globals.DECRYPT);
        }

        input = cryptPlugboard(input);

        return (char)(input + Globals.A_CODE);
    }

    public void rotateRotors() {
        lastRotation = 0b000;

        if (this.rotors[Globals.MID_ROTOR].isTurnover()) {
            this.rotors[Globals.SLOW_ROTOR].rotate();
            this.rotors[Globals.MID_ROTOR].rotate();    //double step
            lastRotation |= Globals.ROTATE_SLOW | Globals.ROTATE_MID;
        } else if (this.rotors[Globals.FAST_ROTOR].isTurnover()) {
            this.rotors[Globals.MID_ROTOR].rotate();
            lastRotation |= Globals.ROTATE_MID;
        }

        this.rotors[Globals.FAST_ROTOR].rotate();
        lastRotation |= Globals.ROTATE_FAST;
    }

    public Plugboard getPlugboard() {
        return this.plugboard;
    }

    public void setPlugboardPair(int a, int b) {
        this.plugboard.insertPair(a, b);
    }

    public void removePlugboardPair(int a) {
        this.plugboard.removePair(a);
    }

    public void resetPlugboard() {
        this.plugboard.reset();
    }

    public Rotor getRotor(int type) {
        return this.rotors[type];
    }

    public void setRotor(Rotor rotor, int type) {
        this.rotors[type] = rotor;
    }

    public void setRotorOffset(int type, int offset) {
        this.rotors[type].setOffset(offset);
    }

    public void incRotorOffset(int type) {
        this.rotors[type].setOffset((this.rotors[type].getOffset() + 1) % Globals.ALPHABET_SIZE);
    }

    public void decRotorOffset(int type) {
        this.rotors[type].setOffset((this.rotors[type].getOffset() + Globals.ALPHABET_SIZE - 1) % Globals.ALPHABET_SIZE);
    }

    public void setRotorRing(int type, int ring) {
        this.rotors[type].setRing(ring);
    }

    public void configureRotor(int type,int offset, int ring) {
        this.setRotorOffset(type, offset);
        this.setRotorRing(type, ring);
    }

    public Rotor[] getRotors() {
        return rotors;
    }

    public Reflector getReflector() {
        return reflector;
    }

    public int getLastRotation() {
        return lastRotation;
    }

    public static void main(String[] args) {
        Machine m = new Machine("III", "II", "I", "B");

        Rotor r = m.getRotor(Globals.FAST_ROTOR);
        r.setRing('D');
        r.setOffset('M');

        r = m.getRotor(Globals.MID_ROTOR);
        r.setRing('P');
        r.setOffset('F');

        r = m.getRotor(Globals.SLOW_ROTOR);
        r.setRing('Z');
        r.setOffset('J');

        m.setPlugboardPair('A', 'N');

        String text= "naca";
        String a = m.cryptText(text);
        System.out.println(a);
    }
}
