package Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//I can move the logging() method to run(). 
public class Logger extends Thread {
	FileWriter fw;
	public Logger() throws IOException{
		
	fw = new FileWriter("GameLogs.txt", true);
		
	}
	public synchronized void writeLogFile(String message) throws Exception{
	    try {
	    	
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(message);
	            bw.newLine();
	            bw.close();
	        
	    } catch (Exception e) {
	        throw new Exception(e.getMessage());
	    }
	}
	
        /*
         * main just for testing the logging for now
         */
//	public static void main(String[] args) throws Exception
//	{
//		
//		Logging log = new Logging();
//		log.writeLogFile("derpp derp");
//	}
	
	 
}
