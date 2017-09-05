package org.moondb.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.moondb.dao.CitationDao;
import org.moondb.dao.PersonDao;
import org.moondb.model.Author;
import org.moondb.model.Citation;

import java.io .IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class LoadRef {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = null;
		try {
			reader =  new BufferedReader(new InputStreamReader(new FileInputStream("data\\moondbcitation.txt"),Charset.forName("UTF-8")));

		    String line;
		    ArrayList<String> entry = new ArrayList<String>();
		    while ((line = reader.readLine()) != null) {
		    	System.out.println(line);
		    	if (line.startsWith("@")) {
		    		System.out.println("Bibitem");
			        entry = new ArrayList<String>();
		    	} else if (line.startsWith("}")) {
			        System.out.println(entry);
			        Citation citation = BibEntryParser.parseBibEntry(entry);
			        CitationDao citationDao = new CitationDao(citation);
			        citationDao.saveDataToDB();
			        citation.setCitationNum(citationDao.getCitationNum());
			        PersonDao personDao = new PersonDao(citation);
			        personDao.saveData();
			        System.out.println("Title is :" + citation.getRefTitle());
			        System.out.println("Type is :" + citation.getRefType());
			        System.out.println("Journal is :" + citation.getRefJournal());
			        System.out.println("Volume is :" + citation.getRefVolume());
			        System.out.println("Issue is :" + citation.getRefIssue());

			        System.out.println("DOI is :" + citation.getRefDOI());
			        System.out.println("URL is :" + citation.getRefUrl());
			        System.out.println("Year is :" + citation.getRefYear());
			        System.out.println("Num is :" + citation.getRefNum());
			        List<Author> authors = citation.getAuthors();
			        authors.forEach(author->System.out.println(author.getFullName()));
		    		
		    	} else {
		    		if (!line.isEmpty())
		    			entry.add(line.trim());
		    	}
		    	
		    		

		    }
		} finally {
			reader.close();
		}
	}
}
