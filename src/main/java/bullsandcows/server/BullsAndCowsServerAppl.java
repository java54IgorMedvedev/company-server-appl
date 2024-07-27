package bullsandcows.server;

import telran.net.*;
import java.util.*;

public class BullsAndCowsServerAppl {

    private static final int PORT = 5000;
    private static Map<String, Game> activeGames = new HashMap<>();

    public static void main(String[] args) {
        Protocol protocol = new BullsAndCowsProtocol(activeGames);
        TcpServer tcpServer = new TcpServer(protocol, PORT);
        tcpServer.run();
    }
}
