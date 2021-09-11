package model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class VNCServerProcessBuilder {

    public static final String VNC_SERVER_PROCESS_NAME = "x11vnc";

    public static Process startProcess(String passwordForVNC) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(buildCommandList(passwordForVNC));
        try {
            Process process = processBuilder.start();
            OutputStream is = process.getOutputStream();
            is.write(passwordForVNC.getBytes());
            is.flush();
            return process;
        } catch (IOException e) {
            throw new Exception("VNCserver start process failed -> " + e.getMessage(), e);
        }
    }

    private static LinkedList<String> buildCommandList(String passwordForVNC) {
        LinkedList<String> commands = new LinkedList<>();
        commands.add("sh");
        commands.add("-c");
        String vncServerStartCommand = VNC_SERVER_PROCESS_NAME + " "
                + VNCServerParams.PASSWORD + " " + passwordForVNC + " "
                + VNCServerParams.KEEP_LISTENING_AFTER_DISCONNECT;
        commands.add(vncServerStartCommand);
        return commands;
    }
}
