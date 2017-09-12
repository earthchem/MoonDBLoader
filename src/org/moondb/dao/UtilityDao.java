package org.moondb.dao;

import org.moondb.util.DatabaseUtil;

public class UtilityDao {

	public static Integer getVariableNum(String variableCode) {
		String query = "SELECT variable_num FROM variable WHERE variable_code='" + variableCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getUnitNum(String unitAbbr) {
		String query = "SELECT unit_num FROM unit WHERE unit_abbreviation='" + unitAbbr + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getMethodNum(String methodCode) {
		String query = "SELECT method_num FROM method WHERE method_code='" + methodCode + "' AND method_type_num=3";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static String getCitationCode(String moondbNum) {
		String query = "SELECT citation_code FROM citation WHERE moondbnum='" + moondbNum + "'";
		String citationCode = (String)DatabaseUtil.getUniqueResult(query);
		return citationCode;
	}
	
	public static Integer getDatasetNum(String datasetCode) {
		String query = "SELECT dataset_num FROM dataset WHERE dataset_code='" + datasetCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
}
