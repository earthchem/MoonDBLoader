package org.moondb.model;
/*
 * MoonDB(table:dataset)	Template(sheet:TABLE_TITLE)	Java(Class:Dataset)	
 * dataset_type											datasetType
 * dataset_title			TABLE_TITLE					datasetTitle
 * dataset_code				Number of TABLE_IN_REF		tableNum
 */
public class Dataset {
	private String datasetType;
	private String datasetTitle;
	private String tableCode;  //used for creating dataset_code
	private String datasetCode;
	private int citationNum;
	
	public String getDatasetType() {
		return datasetType;
	}
	
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	
	
	public String getDatasetTitle() {
		return datasetTitle;
	}
	
	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}
	
	public String getTableCode() {
		return tableCode;
	}
	
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	public String getDatasetCode() {
		return datasetCode;
	}
	
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	
	public int getCitationNum() {
		return citationNum;
	}
	
	public void setCitationNum(int citationNum) {
		this.citationNum = citationNum;
	}

}
