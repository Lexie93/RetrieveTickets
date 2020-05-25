package utilities;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVWriter {
	
	private static final Logger LOGGER = Logger.getLogger(CSVWriter.class.getName());
	
	private CSVWriter(){
		//not called
	}

	public static void writeSortedDates(String[] dates, String outName){
		LocalDate[] arrayOfDates = new LocalDate[dates.length];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		try (
			FileWriter writer = new FileWriter("C:/Users/Alex/Desktop/Università/ISW2/Falessi/Progetto/RetrieveTickets/trunk/" + outName);
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);)
		{
			for(int i=0; i<dates.length; i++){
				arrayOfDates[i] = LocalDate.parse(dates[i], formatter);
			}
			Arrays.sort(arrayOfDates);
			for(int i=0; i<arrayOfDates.length; i++){
				printer.printRecord(arrayOfDates[i]);
			}
		    printer.flush();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
