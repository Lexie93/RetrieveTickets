package retriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RetrieveGitLog {
	private final static Logger LOGGER = Logger.getLogger(RetrieveGitLog.class.getName());

	public static String retrieveLastDate(String ID){
		ProcessBuilder pb = new ProcessBuilder("git", "log", "--date=short", "--pretty=format:\"%cd\"", "--max-count=1", "--grep=" + ID + " ", "--grep=" + ID + ":");
		pb.directory(new File("C:/Users/Alex/Desktop/Università/ISW2/Falessi/Progetto/Parquet/parquet-mr"));
		String line=null;
		try {
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line = br.readLine();
		} catch(IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return line;
	}

}
