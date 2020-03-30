package retriever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.util.logging.Logger;
import java.util.logging.Level;

public class RetrieveJiraTickets {
	
	private static final Logger LOGGER = Logger.getLogger(RetrieveJiraTickets.class.getName());

	   private static String readAll(Reader rd) throws IOException {
		      StringBuilder sb = new StringBuilder();
		      int cp;
		      while ((cp = rd.read()) != -1) {
		         sb.append((char) cp);
		      }
		      return sb.toString();
		   }

	   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
	      InputStream is = new URL(url).openStream();
	      try {
	         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	         String jsonText = readAll(rd);
	         JSONArray json = new JSONArray(jsonText);
	         return json;
	       } finally {
	         is.close();
	       }
	   }

	   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	      InputStream is = new URL(url).openStream();
	      try {
	         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	         String jsonText = readAll(rd);
	         JSONObject json = new JSONObject(jsonText);
	         return json;
	       } finally {
	         is.close();
	       }
	   }


	  
	   public static String[] retrieveFixDates(String projName) {
			   
		   
		   ArrayList<String> res = new ArrayList<>();
		   Integer j = 0, i = 0, total = 1;
		   
	      //Get JSON API for closed bugs w/ AV in the project
	      do {
	         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
	         j = i + 1000;
	         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	                + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
	                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	                + i.toString() + "&maxResults=" + j.toString();
	         try {
	        	 JSONObject json = readJsonFromUrl(url);
	        	 JSONArray issues = json.getJSONArray("issues");
	        	 total = json.getInt("total");
	        	 for (; i < total && i < j; i++) {
	        		 //Iterate through each bug
	        		 String key = issues.getJSONObject(i%1000).get("key").toString();
	        		 String lastDate=RetrieveGitLog.retrieveLastDate(key);
	        		 if (lastDate!=null) {
	        			 //System.out.println(lastDate);
	        			 res.add(lastDate);
	        		 }
	        	 }  
	         } catch (Exception e) {
	        	 LOGGER.log(Level.SEVERE, e.getMessage(), e);
	         }
	      } while (i < total);
	      String[] result=new String[res.size()];
	      result=res.toArray(result);
	      return result;
	   }

	 public static void main(String[] args){
		
		String projName ="PARQUET";
		
		String[] dates=retrieveFixDates(projName);
		LocalDate[] arrayOfDates = new LocalDate[dates.length];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		try {
			FileWriter writer = new FileWriter("C:/Users/Alex/Desktop/Università/ISW2/Falessi/Progetto/RetrieveTickets/trunk/dates.csv");
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
			for(int i=0; i<dates.length; i++){
				arrayOfDates[i] = LocalDate.parse(dates[i], formatter);
			}
			Arrays.sort(arrayOfDates);
			for(int i=0; i<arrayOfDates.length; i++){
				printer.printRecord(arrayOfDates[i]);
			}
		    printer.flush();
		    printer.close();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return;
	 }
	
}
