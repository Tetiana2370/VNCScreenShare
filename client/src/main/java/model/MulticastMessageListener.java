package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastMessageListener {

    public static final String MULTICAST_ADDRESS = "239.255.100.100";
    public static final int PORT = 30000;
    public static final int DEFAULT_BYTE_ARRAY_SIZE = 512;

    private static MulticastSocket multicastSocket = null;

    public static DatagramPacket listen() {
        try {
            multicastSocket = new MulticastSocket(PORT);
            multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
            return receiveDatagramPacket();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            multicastSocket.close();
        }
    }
    
    private static DatagramPacket receiveDatagramPacket() throws IOException {
        byte[] message = new byte[DEFAULT_BYTE_ARRAY_SIZE];
        DatagramPacket packet = new DatagramPacket(message, message.length);
        multicastSocket.receive(packet);
        return packet;
    }
}
