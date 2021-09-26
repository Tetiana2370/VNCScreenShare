package model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class VNCViewerProcess {

    private final String serverIpAddress;
    private Process process;
    public static String SHELL_CMD = "sh";
    public static String COMMAND_CMD = "-c";
    public static final String APP_NAME = "xtightvncviewer";
    private final ConnectionParameters connectionParameters;


    public VNCViewerProcess(String serverIPAddress, ConnectionParameters connectionParameters) {
        this.serverIpAddress = serverIPAddress;
        this.connectionParameters = connectionParameters;
    }

    public Process start() throws IOException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);

            String vncViewerParams = APP_NAME + " " + serverIpAddress + " " + VNCViewerParameters.PASSWORD;
            vncViewerParams += connectionParameters.isFullscreenMode() ? VNCViewerParameters.FULLSCREEN_MODE : "";
            processBuilder.command(SHELL_CMD, COMMAND_CMD, vncViewerParams);
            this.process = processBuilder.start();
            setPassword();
            return this.process;
        } catch (Exception e) {
            throw new IOException("VNCViewerProcess: Nie powiodło się uruchomienie procesu VNCViewer -> " + e.getMessage(), e);
        }
    }

    public String getIpAddress() {
        return this.serverIpAddress;
    }

    public Process getProcess() {
        return this.process;
    }

    private void setPassword() throws IOException {
        try {
            OutputStream outputStream = this.process.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(this.connectionParameters.getPasswordForVNC());
            printStream.flush();
            printStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new IOException("Błąd przy próbie podania hasła -> " + e.getMessage(), e);
        }
    }
}
