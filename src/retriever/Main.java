package retriever;

import utilities.CSVWriter;

public class Main {
		
	public static void main(String[] args){
		
		String projName = "PARQUET";
		String outName = "dates.csv";
		
		String[] dates=RetrieveJiraTickets.retrieveFixDates(projName, "C:/Users/Alex/Desktop/Università/ISW2/Falessi/Progetto/Parquet/parquet-mr");
		
		CSVWriter.writeSortedDates(dates, outName);
	 }
}
