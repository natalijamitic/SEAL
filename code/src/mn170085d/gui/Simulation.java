package mn170085d.gui;

import mn170085d.Globals;
import mn170085d.enigma.Machine;
import mn170085d.enigma.Plugboard;
import mn170085d.enigma.Reflector;
import mn170085d.enigma.Rotor;

public class Simulation {
    private LineDrawer lineDrawer;
    private Machine machine;

    public Simulation(Machine machine, LineDrawer lineDrawer) {
        this.lineDrawer = lineDrawer;
        this.machine = machine;
    }

    public void connectLines() {
        // fast, mid, slow
        Rotor[] rotors = machine.getRotors();
        for (int i = 0; i < rotors.length; i++) {
            char map[] = rotors[i].getEncryptMap();
            for (int letter = 0; letter < map.length; letter++) {
                int letterWithRingOffset = (letter + rotors[i].getRing()) % Globals.ALPHABET_SIZE;
                int mappingWithRingOffset = (map[letter] + rotors[i].getRing() - Globals.A_CODE) % Globals.ALPHABET_SIZE + Globals.A_CODE;
                lineDrawer.drawLineBetweenLetters(i, (char)(letterWithRingOffset + Globals.A_CODE), (char)mappingWithRingOffset);
            }
        }

        Plugboard plugboard = machine.getPlugboard();
        Reflector reflector = machine.getReflector();
        for (int letter = 0; letter < Globals.ALPHABET_SIZE; letter++) {
            lineDrawer.drawLineBetweenLetters(Globals.PLUGBOARD, (char)(letter + Globals.A_CODE), (char)(plugboard.getMapping(letter) + Globals.A_CODE));
            lineDrawer.drawLineBetweenLetters(Globals.REFLECTOR, (char)(letter + Globals.A_CODE), (char)(reflector.crypt(letter) + Globals.A_CODE));
        }
    }

    public char simulateCryption(char letter) {
        boolean isLowerCase = Character.isLowerCase(letter);

        machine.rotateRotors();
        rotateDrawing();
        refreshWires();

        int input = Character.toUpperCase(letter) - Globals.A_CODE;
        lineDrawer.drawInputLabel(input);

        int output = machine.cryptPlugboard(input);
        lineDrawer.paintLineBetweenEntries(Globals.PLUGBOARD, input, output, Globals.FORWARD_PAINT);

        Rotor[] rotors =  machine.getRotors();
        for (int i = 0; i < rotors.length; i++) {
            input = output;
            output = machine.cryptRotor(rotors[i], input, Globals.ENCRYPT);
            lineDrawer.paintLineBetweenEntries(i, input, output, Globals.FORWARD_PAINT);
        }

        input = output;
        output = machine.cryptReflector(input);
        lineDrawer.paintLineBetweenEntries(Globals.REFLECTOR, input, output, Globals.BACK_PAINT);

        for (int i = rotors.length - 1; i >= 0; i--) {
            input = output;
            output = machine.cryptRotor(rotors[i], input, Globals.DECRYPT);
            lineDrawer.paintLineBetweenEntries(i, output, input, Globals.BACK_PAINT);
        }

        input = output;
        output = machine.cryptPlugboard(input);
        lineDrawer.paintLineBetweenEntries(Globals.PLUGBOARD, output, input, Globals.BACK_PAINT);

        lineDrawer.drawOutputLabel(output);

        letter = (char)(output + Globals.A_CODE);
        letter = isLowerCase ? Character.toLowerCase(letter) : Character.toUpperCase(letter);

        return letter;
    }

    public void rotateDrawing() {
        int rotateInfo = machine.getLastRotation();

        if ((rotateInfo & Globals.ROTATE_FAST) != 0) {
            lineDrawer.rotate(Globals.FAST_ROTOR);
        }
        if ((rotateInfo & Globals.ROTATE_MID) != 0) {
            lineDrawer.rotate(Globals.MID_ROTOR);
        }
        if ((rotateInfo & Globals.ROTATE_SLOW) != 0) {
            lineDrawer.rotate(Globals.SLOW_ROTOR);
        }
    }

    public void refreshWires(){
        lineDrawer.removeAllLines();
        connectLines();
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
        matchDrawingWithRotors();   // handle offset;
        refreshWires();
    }

    public void matchDrawingWithRotors() {
        for (int type = 0; type < 3; type ++) {
            lineDrawer.matchWithRotor(machine.getRotor(type), type);
        }
    }
}
