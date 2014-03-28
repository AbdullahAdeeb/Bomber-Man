package GameServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

//I can move the logging() method to run(). 
public class Logger {

    private static Logger log = null;
    private FileWriter fw = null;
    BufferedWriter bw;

    private Logger() throws IOException {

        new File("GameLogs.txt").delete();
        fw = new FileWriter("GameLogs.txt", true);

        bw = new BufferedWriter(fw);
    }

    public static synchronized void writeLogFile(final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Logger.log == null) {
                        Logger.log = new Logger();
                    }
                    Logger.log.writeLog(message);
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).run();
    }

    private synchronized void writeLog(String message) throws Exception {

        try {
            bw.write(message);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
