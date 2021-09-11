package model;

public class Report {

    public final static String CONNECTED_WITH = "Connected with";
    public final static String DISCONNECTED = "Disconnected from ";

    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void println(String msg, String arg) {
        System.out.println(msg + arg);
    }

    public static void println(Object object, String msg) {
        if (object != null) {
            println(object.getClass().getName() + ": " + msg);
        }
    }
}
