package mn170085d;

import javafx.scene.paint.*;
import mn170085d.enigma.ReflectorConfig;
import mn170085d.enigma.RotorConfig;

import java.util.HashMap;
import java.util.Map;

public class Globals {
    public static final int A_CODE = 65;
    public static final int Z_CODE = 90;
    public static final int ALPHABET_SIZE = 26;

    public static final int DECRYPT = 0;
    public static final int ENCRYPT = 1;

    public static final int FAST_ROTOR = 0;
    public static final int MID_ROTOR = 1;
    public static final int SLOW_ROTOR = 2;

    public static final int REFLECTOR = 3;
    public static final int PLUGBOARD = 4;

    public static final int ROTATE_FAST = 0b001;
    public static final int ROTATE_MID  = 0b010;
    public static final int ROTATE_SLOW = 0b100;

    public static final Paint LAMPBOARD_OFF_FILL = Paint.valueOf("#26232478");
    public static final Paint LAMPBOARD_OFF_STROKE = Paint.valueOf("#1e1e1e");
    public static final Paint LAMPBOARD_OFF_TEXT_FILL = Paint.valueOf("#33332e");
    public static final Paint LAMPBOARD_ON_FILL = Paint.valueOf("#f0ff80");
    public static final Paint LAMPBOARD_ON_STROKE = Paint.valueOf("#1e1e1e");
    public static final Paint LAMPBOARD_ON_TEXT_FILL = Paint.valueOf("#000000");
    public static final Paint DEFAULT_PAINT = Paint.valueOf("#000000");
    public static final Paint BACK_PAINT = Paint.valueOf("#ffea00");
    public static final Paint FORWARD_PAINT = Paint.valueOf("#ff0000");
    public static final Color BACK_COLOR = Color.valueOf("#ffea00");
    public static final Color FORWARD_COLOR = Color.valueOf("#ff0000");
    public static final Color ROTOR_STATE_DEFAULT_COLOR = Color.valueOf("#c1c1c1");
    public static final Color ROTOR_STATE_DARK_COLOR = Color.valueOf("#585e71");
    public static final LinearGradient PLUGBOARD_GRADIENT = new LinearGradient(0.165, 1, 0.5, 0.70, true, CycleMethod.NO_CYCLE, new Stop(1,Color.BLACK), new Stop(0, Color.color(0.81, 0.81, 0.81)));

    public static final String CSS_CLASS_ON_DISPLAY = "displaying";

    public static final String EXPORT_FILE_NAME = "exportedFileBySEAL";
    public static final String FILE_EXTENSION = ".txt";

    public static final String reflectorTypes[] = {"A", "B", "C"};
    public static final String rotorTypes[] = {"I", "II", "III", "IV", "V"};
    public static final String DEFAULT_MACHINE_CONFIG = "A";

    public static final String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    // Available Rotor Configurations (Wiring + Turnover)
    public static final String I_WIRING = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    public static final Character I_TURNOVER = 'Q';
    public static final String II_WIRING = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
    public static final Character II_TURNOVER = 'E';
    public static final String III_WIRING = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
    public static final Character III_TURNOVER = 'V';
    public static final String IV_WIRING = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
    public static final Character IV_TURNOVER = 'J';
    public static final String V_WIRING = "VZBRGITYUPSDNHLXAWMJQOFECK";
    public static final Character V_TURNOVER = 'Z';
    public static final Map<String, RotorConfig> AVAILABLE_ROTORS = new HashMap<String, RotorConfig>() {
        {
            put("I", new RotorConfig(Globals.I_WIRING, Globals.I_TURNOVER));
            put("II", new RotorConfig(Globals.II_WIRING, Globals.II_TURNOVER));
            put("III", new RotorConfig(Globals.III_WIRING, Globals.III_TURNOVER));
            put("IV", new RotorConfig(Globals.IV_WIRING, Globals.IV_TURNOVER));
            put("IV", new RotorConfig(Globals.V_WIRING, Globals.V_TURNOVER));
        }
    };

    // Available Reflector Configurations (Wiring)
    public static final String A_WIRING = "EJMZALYXVBWFCRQUONTSPIKHGD";
    public static final String B_WIRING = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    public static final String C_WIRING = "FVPJIAOYEDRZXWGCTKUQSBNMHL";
    public static final Map<String, ReflectorConfig> AVAILABLE_REFLECTORS = new HashMap<String, ReflectorConfig>() {
        {
            put("A", new ReflectorConfig(Globals.A_WIRING));
            put("B", new ReflectorConfig(Globals.B_WIRING));
            put("C", new ReflectorConfig(Globals.C_WIRING));
        }
    };
}
