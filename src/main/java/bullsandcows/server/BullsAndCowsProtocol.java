package bullsandcows.server;

import telran.net.*;
import java.util.*;

public class BullsAndCowsProtocol implements Protocol {

    private Map<String, Game> activeGames;

    public BullsAndCowsProtocol(Map<String, Game> activeGames) {
        this.activeGames = activeGames;
    }

    @Override
    public Response getResponse(Request request) {
        String requestType = request.requestType();
        String requestData = request.requestData();

        return switch (requestType) {
            case "startGame" -> startNewGame();
            case "makeMove" -> processMove(requestData);
            default -> new Response(ResponseCode.WRONG_REQUEST_TYPE, "Invalid request type: " + requestType);
        };
    }

    private Response startNewGame() {
        Game game = new Game();
        activeGames.put(game.getId(), game);
        return new Response(ResponseCode.OK, "New game started with ID: " + game.getId());
    }

    private Response processMove(String requestData) {
        String[] data = requestData.split(";");
        String gameId = data[0];
        String guess = data[1];

        Game game = activeGames.get(gameId);
        if (game == null) {
            return new Response(ResponseCode.WRONG_REQUEST_DATA, "Game not found with ID: " + gameId);
        }

        MoveResult result = game.makeMove(guess);
        if (game.isFinished()) {
            activeGames.remove(gameId);
        }

        return new Response(ResponseCode.OK, result.toString());
    }
}
