package bullsandcows.server;

import java.time.Instant;
import java.util.*;

public class Game {

    private static final Random RANDOM = new Random();
    private final String id;
    private final String secret;
    private boolean finished;
    private final Instant startTime;
    private Instant finishTime;
    private final List<MoveResult> moveResults;

    public Game() {
        this.id = UUID.randomUUID().toString();
        this.secret = generateSecret();
        this.finished = false;
        this.startTime = Instant.now();
        this.moveResults = new ArrayList<>();
    }

    private String generateSecret() {
        List<Character> digits = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) {
            digits.add(c);
        }
        Collections.shuffle(digits);

        StringBuilder secretBuilder = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            secretBuilder.append(digits.get(i));
        }
        return secretBuilder.toString();
    }

    public MoveResult makeMove(String guess) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < 4; i++) {
            char guessDigit = guess.charAt(i);
            char secretDigit = secret.charAt(i);

            if (guessDigit == secretDigit) {
                bulls++;
            } else if (secret.indexOf(guessDigit) != -1) {
                cows++;
            }
        }

        MoveResult result = new MoveResult(guess, bulls, cows);
        moveResults.add(result);

        if (bulls == 4) {
            finished = true;
            finishTime = Instant.now();
        }

        return result;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getId() {
        return id;
    }

    public List<MoveResult> getMoveResults() {
        return moveResults;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", secret='" + secret + '\'' +
                ", finished=" + finished +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", moveResults=" + moveResults +
                '}';
    }
}
