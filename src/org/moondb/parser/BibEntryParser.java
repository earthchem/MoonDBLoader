package org.moondb.parser;

import java.util.ArrayList;

import org.moondb.model.Author;
import org.moondb.model.Citation;

public class BibEntryParser {
	public static Citation parseBibEntry(ArrayList<String> bibEntry) {
    	Citation citation = new Citation();
        try {
    		for(int i=0;i<bibEntry.size();i++) {
            	String[] token = bibEntry.get(i).split("=",2);
            	String key = token[0].trim().toUpperCase();
            	String value = token[1].substring(token[1].indexOf('{')+1, token[1].indexOf('}'));
            	if (value.indexOf("'") != -1) {
            		value = value.replace("'", "''");
            	}
            	//System.out.println("key is: " + key);
            	//System.out.println("value is: " + value);
                switch (key) {
                	case "TITLE":
                		citation.setRefTitle(value);
                		break;
                	case "JOURNAL":
                		citation.setRefJournal(value);
                		break;
                	case "VOLUME":
                		citation.setRefVolume(value);
                		break;
                	case "ISSUE":
                		citation.setRefIssue(value);
                		break;
                	case "PAGES":
                		citation.setRefPages(value);
                		break;
                	case "YEAR":
                		citation.setRefYear(Integer.parseInt(value));
                		break;
                	case "DOI":
                		citation.setRefDOI(value);
                		break;
                	case "URL":
                		citation.setRefUrl(value);
                		break;
                	case "TYPE":
                		citation.setRefType(value);
                		break;
                	case "MOONDBNUM":
                		citation.setRefNum(value);
                		break;
                	case "AUTHOR":
                		String[] splitNames = value.split(" and ");
                	    ArrayList<Author> authors = new ArrayList<Author>();
                		
                		for(int k=0;k<splitNames.length;k++) {
                			Author author = new Author();
                			String fullName = splitNames[k].toUpperCase();
                			String[] names = fullName.split(",", 2);
                			author.setFullName(fullName);
                			author.setLastName(names[0].trim());
                			author.setFirstName(names[1].trim());
                			authors.add(author);
                		}
                		citation.setAuthors(authors);
                		break;
                }
                
    		}
    		citation.setCitationCode(citation.getAuthors().get(0).getLastName() +", " + citation.getRefYear().toString());
    		return citation;
        } catch (Exception e) {
        	System.out.println("error in parser: " + e.toString());
        	System.out.println(e.getLocalizedMessage());

        	return null;
        }
	}
}
