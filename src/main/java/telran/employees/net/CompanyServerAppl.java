package telran.employees.net;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

import java.util.Scanner;

public class CompanyServerAppl {

    private static final String FILE_NAME = "employeesTest.data";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Company company = new CompanyMapsImpl();
        try {
            ((Persistable) company).restore(FILE_NAME);
        } catch (Exception e) {
            System.out.println("Failed to restore data from file: " + e.getMessage());
        }

        Protocol protocol = new CompanyProtocol(company);
        TcpServer tcpServer = new TcpServer(protocol, PORT);

        Thread serverThread = new Thread(tcpServer::run);
        serverThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 'shutdown' to stop the server:");
            String command = scanner.nextLine().trim().toLowerCase();
            if ("shutdown".equals(command)) {
                tcpServer.shutdown();
                break;
            }
        }
        scanner.close();

        try {
            serverThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }

        try {
            ((Persistable) company).save(FILE_NAME);
            System.out.println("Server stopped successfully, data saved.");
        } catch (Exception e) {
            System.out.println("Failed to save data to file: " + e.getMessage());
        }
    }
}
