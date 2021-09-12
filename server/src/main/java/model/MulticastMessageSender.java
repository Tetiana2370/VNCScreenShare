package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastMessageSender implements Runnable {

    private static final String MULTICAST_ADDRESS = "239.255.100.100";
    private static final int UDP_PORT = 30000;
    private DatagramSocket datagramSocket = null;
    private final ConnectionParameters connectionParams;

    public MulticastMessageSender(ConnectionParameters connectionParams) {
        this.connectionParams = connectionParams;
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket();
            byte[] message = ConnectionParameters.convertToString(connectionParams).getBytes();
            DatagramPacket datagramPacket =
                    new DatagramPacket(message, message.length, InetAddress.getByName(MULTICAST_ADDRESS), UDP_PORT);
            while (Thread.currentThread().isAlive()) {
                datagramSocket.send(datagramPacket);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.out.println("DatagramPacket send failed -> " + e.getMessage());
        } finally {
            datagramSocket.close();
        }
    }
}