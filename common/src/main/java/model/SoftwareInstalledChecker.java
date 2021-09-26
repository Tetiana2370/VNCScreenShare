package model;

import javax.swing.*;
import java.io.IOException;

public class SoftwareInstalledChecker {

    public static void showErrorIfNotInstalled(String softwareName){
        try {
            Process process = Runtime.getRuntime().exec("which " + softwareName);
            if(process.getInputStream().readAllBytes().length == 0) {
                String message = "<html>" + softwareName + " nie jest zainstalowany.<br/> Zainstaluj program aby móc korzystać z aplikacji" + "</html>";
                JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

        } catch (IOException e){
            String message = "Nie powiodło się sprawdzenie czy program " + softwareName + "jest zainstalowany -> " + e.getMessage();
            JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}
