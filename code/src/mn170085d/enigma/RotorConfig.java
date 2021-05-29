package mn170085d.enigma;

public class RotorConfig {
    private String wiring;
    private char turnover;

    public RotorConfig(String wiring, char turnover) {
        this.wiring = wiring;
        this.turnover = turnover;
    }

    public String getWiring() {
        return wiring;
    }

    public char getTurnover() {
        return turnover;
    }
}
