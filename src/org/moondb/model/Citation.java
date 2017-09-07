package org.moondb.model;
/*
 * MoonDB(table:citation)		Template(Bibtex:element)		Java(class:Citation)
 * title						title						    refTitle
 * citation_type				type							refType
 * journal						journal							refJournal
 * volume						volume							refVolume
 * issue						Issue							refIssue
 * publication_year				year							refYear
 * citation_link				url								refUrl
 * pages						pages							refPages
 * 								DOI								refDOI
 * 								author							authors
 *moondbnum						moondbnum						refNum
 *citation_num													citationNum
 *citation_code													citationCode
 */

import java.util.List;



public class Citation {

	private String refTitle;
	private String refType;
	private String refJournal;
	private String refVolume;
	private String refIssue;
	private Integer refYear;
	private String refPages;
	private String refDOI;
	private String refUrl;
	private String refNum;          //Identifier for citation from Bibtext file
	private Integer citationNum;    //Citation ID from table citation;
	private String citationCode;
	private List<Author> authors;

	
	public String getRefTitle() {
		return refTitle;
	}
	

	public void setRefTitle(String refTitle) {
		this.refTitle = refTitle;
	}
	
	public String getRefJournal() {
		return refJournal;
	}
	
	public void setRefJournal(String refJournal) {
		this.refJournal = refJournal;
	}
	
	public String getRefType() {
		return refType;
	}
	
    public void setRefType(String refType) {
		this.refType = refType;
	}
	
	public String getRefVolume() {
		return refVolume;
	}
	
	public void setRefVolume(String refVolume) {
		this.refVolume = refVolume;
	}

	public String getRefIssue() {
		return refIssue;
	}
	
	public void setRefIssue(String refIssue) {
		this.refIssue = refIssue;
	}

	public String getRefPages() {
		return refPages;
	}
	
	public void setRefPages(String refPages) {
		this.refPages = refPages;
	}
	
	public Integer getRefYear() {
		return refYear;
	}
	
	public void setRefYear(Integer refYear) {
		this.refYear = refYear;
	}
	
	public String getRefDOI() {
		return refDOI;
	}
	
	public void setRefDOI(String refDOI) {
		this.refDOI = refDOI;
	}
	
	public String getRefUrl() {
		return refUrl;
	}
	
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}

	
	public String getRefNum() {
		return refNum;
	}
	
	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}
	
    public Integer getCitationNum() {
    	return citationNum;
    }
    
    public void setCitationNum(Integer citationNum) {
    	this.citationNum = citationNum;
    }
    
    public String getCitationCode() {
    	return citationCode;
    }
    
    public void setCitationCode(String citationCode) {
    	this.citationCode = citationCode;
    }
    
	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
}
