package mn170085d.enigma;

import mn170085d.Globals;

/**
 * This class handles the Reflector logic and maps input to output given a specific wiring.
 * The main logic can bee seen in {@link Reflector#crypt(int)}.
 */
public class Reflector {
    /**
     *  String representing the input (index) and output (value) mappings.
     */
    private String wiring;

    /**
     * Construct the reflector.
     * @param configuration Reflector configuration that contains the wiring mapping.
     */
   public Reflector(ReflectorConfig configuration) {
       this.wiring = configuration.getWiring();
   }

    /**
     * Given a reflector with wiring "EJMZALYXVBWFCRQUONTSPIKHGD" and entry point 3,
     * the output will be the index number of "M".
     * M is the 13th letter of alphabet so output is 13.
     * @param input Number/Index of entry.
     * @return number/index of output.
     */
   public int crypt(int input) {
       return this.wiring.charAt(input) - Globals.A_CODE;
   }
}
