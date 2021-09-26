package model;

public class ProcessTerminator extends Thread {
    private final Process process;
    private final String processName;

    public ProcessTerminator(Process process, String processName) {
        this.process = process;
        this.processName = processName;
    }

    public void run() {
        if (this.process != null && this.process.isAlive()) {
            this.process.descendants()
                    .forEach(ProcessHandle::destroy);
            this.process.children()
                    .forEach(ProcessHandle::destroy);
            this.process.destroy();
            System.out.println(processName + " process destroyed");
        }
    }

    public static void terminateProcess(Process process, String processName){
        if (process != null && process.isAlive()) {
            process.descendants()
                    .forEach(ProcessHandle::destroy);
            process.children()
                    .forEach(ProcessHandle::destroy);
            process.destroy();
            System.out.println(processName + " process destroyed");
        }
    }
}
