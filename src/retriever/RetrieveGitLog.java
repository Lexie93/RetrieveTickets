package retriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RetrieveGitLog {
	
	private static final Logger LOGGER = Logger.getLogger(RetrieveGitLog.class.getName());
	
	private RetrieveGitLog(){
		//not called
	}

	public static String retrieveLastDate(String id, String path){
		ProcessBuilder pb = new ProcessBuilder("git", "log", "--date=short",  "--pretty=format:\"%cd\"", "--max-count=1", "--grep=" + id + "$", "--grep=" + id + "[^0-9]");
		pb.directory(new File(path));
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
