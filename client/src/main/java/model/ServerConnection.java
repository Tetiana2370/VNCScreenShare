package model;

import java.net.ServerSocket;

public class ServerConnection {

    private int serverPort;
    private ServerSocket serverSocket;
    private final ConnectionParams connectionParams;

    public ServerConnection(ConnectionParams connectionParams) throws Exception {
        this.connectionParams = connectionParams;
        createServerSocket();
        this.connectionParams.setPort(this.serverPort);
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
        MulticastMessageSender multicastMessageSender = new MulticastMessageSender(this.connectionParams);
        new Thread(multicastMessageSender).start();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public String getPasswordForVNC() {
        return this.connectionParams.getPasswordForVNC();
    }
}
