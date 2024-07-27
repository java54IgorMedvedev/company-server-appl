package bullsandcows.server;

public class MoveResult {

    private final String guess;
    private final int bulls;
    private final int cows;

    public MoveResult(String guess, int bulls, int cows) {
        this.guess = guess;
        this.bulls = bulls;
        this.cows = cows;
    }

    @Override
    public String toString() {
        return guess + ": " + bulls + " Bulls, " + cows + " Cows";
    }
}
