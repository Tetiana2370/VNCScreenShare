package model;

public class VNCServerParams {

    public static final String PASSWORD = "-passwd";
    public static final String VIEW_ONLY = "-viewonly";
    //more than one viewer can connect at one time
    public static final String MANY_VIEWERS = "-shared";
    /*Exit after the first successfully connected viewer
    disconnects, opposite of -forever. This is the Default.*/
    public static final String ONCE = "-once";
    /* Keep listening for more connections rather than exiting\n as soon as the first client(s) disconnect. Same as -many */
    public static final String KEEP_LISTENING_AFTER_DISCONNECT = "-forever";
    /* Create an outer loop restarting the x11vnc process
    whenever it terminates. */
    public static final String LOOP = "-loop";
    public static final String ACCEPT = "- accept";
    /*
 Run a command (possibly to prompt the user at the
X11 display) to decide whether an incoming client should be allowed to connect or not. string is an external command
 run via system(3) or some special cases described below. Be sure to quote string if it contains spaces, shell characters, etc.
 If the external command returns 0 the client is accepted, otherwise the client is rejected
    * */
    public static final String ENABLE_REMOTE_COMMANDS = "-yesremote";
    /* reduces output information */
    public static final String QUIET = "-quiet";
    public static final String NO_QUIET = "-noquiet";

}
