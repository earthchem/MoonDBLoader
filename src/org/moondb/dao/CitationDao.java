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
	
	public boolean isCitationCodeExist(String citationCode) {
		String query = "SELECT COUNT(*) FROM citation WHERE citation_code='" + citationCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}
	
	/*
	 * citationCode format: LastName of first author, publication_year
	 * if there are multiple papers of the author published in the same year, the character 'b~z' will be added after the publication_year
	 * AGRELL, 1970
     * AGRELL, 1970b
	*/
	public String generateUniqueCitationCode(String citationCode) {
		
		int i = 98; //char 'b'
		while(isCitationCodeExist(citationCode)) {
			if(Character.isDigit(citationCode.charAt(citationCode.length()-1))) {
				citationCode = citationCode +(char)i;
			} else {
				citationCode = citationCode.substring(0, citationCode.length()-1)+(char)i;
			}
			i++;
		}
		return citationCode;
	}
	
	public void saveDataToDB() {
		String query;
		if (!isCitationExist()) {
			String citationCode = generateUniqueCitationCode(citation.getCitationCode());
			//save data to table citation
			query = "INSERT INTO citation(title,publication_year,citation_link,journal,issue,volume,pages,moondbnum,citation_type,citation_code) VALUES('"+citation.getRefTitle()+"',"+citation.getRefYear()+",'"+citation.getRefUrl()+"','"+citation.getRefJournal()+"','"+citation.getRefIssue()+"','"+citation.getRefVolume()+"','"+citation.getRefPages()+"','"+citation.getRefNum()+"','"+citation.getRefType()+"','"+citationCode+"')";
			System.out.println(query);
			DatabaseUtil.update(query);
			if(citation.getRefDOI() != null) {
				//save data to table citation_external_identifier
				Integer citationNum = getCitationNum();
				Integer externalIdentifierSystemNum = 3;
				query = "INSERT INTO citation_external_identifier(citation_num,external_identifier_system_num,citation_external_identifier) VALUES("+citationNum+","+externalIdentifierSystemNum+",'"+citation.getRefDOI()+"')";
				DatabaseUtil.update(query);
			}
		}
	}
}
