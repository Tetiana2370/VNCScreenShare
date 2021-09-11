package model;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
    final static char[] allowedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();
    public static String generatePassword (int requiredLength) {

        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        while(password.length() < requiredLength){
            password.append(allowedSymbols[random.nextInt(allowedSymbols.length)]);
        }
        return password.toString();
    }
}

