package org.moondb.dao;



import org.moondb.model.*;
import org.moondb.util.DatabaseUtil;

public class CitationDao {

	private Citation citation;
	
	public CitationDao(Citation citation) {
		this.citation = citation;
	}
	
	public boolean isCitationExist() {
		String query = "SELECT COUNT(*) FROM citation WHERE moondbnum='" + citation.getRefNum() + "'";
		System.out.println(query);
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
		
	}
	
	public Integer getCitationNum( ) {
		String query = "SELECT citation_num FROM citation WHERE moondbnum='" + citation.getRefNum() + "'";
		Integer citationNum = (Integer)DatabaseUtil.getUniqueResult(query);
		return citationNum;
	}
	
	public void saveDataToDB() {
		String query;
		if (!isCitationExist()) {
			//save data to citation table
			
			query = "INSERT INTO citation(title,publication_year,citation_link,journal,issue,volume,pages,moondbnum,citation_type) VALUES('"+citation.getRefTitle()+"',"+citation.getRefYear()+",'"+citation.getRefUrl()+"','"+citation.getRefJournal()+"','"+citation.getRefIssue()+"','"+citation.getRefVolume()+"','"+citation.getRefPages()+"','"+citation.getRefNum()+"','"+citation.getRefType()+"')";
			System.out.println(query);
			DatabaseUtil.update(query);
			if(citation.getRefDOI() != null) {
				//save data to citation_external_identifier table
				Integer citationNum = getCitationNum();
				Integer externalIdentifierSystemNum = 3;
				query = "INSERT INTO citation_external_identifier(citation_num,external_identifier_system_num,citation_external_identifier) VALUES("+citationNum+","+externalIdentifierSystemNum+",'"+citation.getRefDOI()+"')";
				DatabaseUtil.update(query);
			}
		}
	}
}
