package org.moondb.model;
/*
 * MoonDB(table:dataset)	Template(sheet:TABLE_TITLE)	Java(Class:Dataset)	
 * dataset_type											datasetType
 * dataset_code											datasetCode
 * dataset_title			TABLE_TITLE					datasetTitle
 * 							Number of TABLE_IN_REF		tableNum
 */
public class Dataset {
	private String datasetType;
	private String datasetCode;
	private String datasetTitle;
	private String tableNum;
	
	public String getDatasetType() {
		return datasetType;
	}
	
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	
	public String getDatasetCode() {
		return datasetCode;
	}
	
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	
	public String getDatasetTitle() {
		return datasetTitle;
	}
	
	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}
	
	public String getTableNum() {
		return tableNum;
	}
	
	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}
}
