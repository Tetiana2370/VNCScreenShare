package model;

import java.net.ServerSocket;
public class ClientConnection {

    private int serverPort;
    private ServerSocket serverSocket;
    private final ConnectionParameters connectionParameters;

    public ClientConnection(ConnectionParameters connectionParameters) throws Exception {
        this.connectionParameters = connectionParameters;
        createServerSocket();
        this.connectionParameters.setPort(this.serverPort);
    }

    void createServerSocket() throws Exception {
        ServerSocket serverSocket = new ServerSocket(0);
        this.serverPort = serverSocket.getLocalPort();
        try {
            int LISTENING_PORT = 30000;
            this.serverSocket = new ServerSocket(LISTENING_PORT);
            this.serverPort = this.serverSocket.getLocalPort();
            startMulticastThread();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startMulticastThread() {
        MulticastMessageSender multicastMessageSender = new MulticastMessageSender(this.connectionParameters);
        new Thread(multicastMessageSender).start();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public String getPasswordForVNC() {
        return this.connectionParameters.getPasswordForVNC();
    }
}
