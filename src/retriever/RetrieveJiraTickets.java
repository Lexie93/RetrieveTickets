package retriever;

import java.util.ArrayList;

import org.json.JSONObject;

import utilities.JSONReader;

import org.json.JSONArray;

import java.util.logging.Logger;
import java.util.logging.Level;

public class RetrieveJiraTickets {
	
	private static final Logger LOGGER = Logger.getLogger(RetrieveJiraTickets.class.getName());
	
	private RetrieveJiraTickets(){
		//not called
	}
	  
	public static String[] retrieveFixDates(String projName, String path) {
		  
		ArrayList<String> res = new ArrayList<>();
		Integer j = 0;
		Integer i = 0;
	   	Integer total = 1;
	   
	   	//Get JSON API for closed bugs w/ AV in the project
	    do {
	    	//Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
	         j = i + 1000;
	         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	                + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
	                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	                + i.toString() + "&maxResults=" + j.toString();
	         try {
	        	 JSONObject json = JSONReader.readJsonFromUrl(url);
	        	 JSONArray issues = json.getJSONArray("issues");
	        	 total = json.getInt("total");
	        	 for (; i < total && i < j; i++) {
	        		 //Iterate through each bug
	        		 String key = issues.getJSONObject(i%1000).get("key").toString();
	        		 String lastDate=RetrieveGitLog.retrieveLastDate(key, path);
	        		 if (lastDate!=null) {
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
	
}
