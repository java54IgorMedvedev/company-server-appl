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
            System.out.println("Could not restore data from file: " + e.getMessage());
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
                try {
                    serverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        scanner.close();

        try {
            ((Persistable) company).save(FILE_NAME);
        } catch (Exception e) {
            System.out.println("Could not save data to file: " + e.getMessage());
        }
        System.out.println("Server shut down and data saved successfully.");
    }
}
