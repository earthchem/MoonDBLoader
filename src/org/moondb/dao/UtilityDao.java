package org.moondb.dao;

import java.util.List;

import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.util.DatabaseUtil;

public class UtilityDao {

	public static boolean isCitationExist(String moondbNum) {
		String query = "SELECT COUNT(*) FROM citation WHERE moondbnum='" + moondbNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
		
	}
	
	public static boolean isDatasetExist(String datasetCode) {
		String query = "SELECT COUNT(*) FROM dataset WHERE dataset_code='" + datasetCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isSamplingFeatureExist(String samplingFeatureCode) {
		String query = "SELECT COUNT(*) FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isMethodExist(String methodCode) {
		String query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodCode + "' AND method_type_num=3";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}

	public static boolean isVariableExist(String variableCode) {
		String query = "SELECT COUNT(*) FROM variable WHERE variable_code='" + variableCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
	public static boolean isUnitExist(String unitAbbr) {
		String query = "SELECT COUNT(*) FROM unit WHERE unit_abbreviation='" + unitAbbr + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
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
	
	public static Integer getCitationNum(String moondbNum) {
		String query = "SELECT citation_num FROM citation WHERE moondbnum='" + moondbNum + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getDatasetNum(String datasetCode) {
		String query = "SELECT dataset_num FROM dataset WHERE dataset_code='" + datasetCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getSamplingFeatureNum(String samplingFeatureCode) {
		String query = "SELECT sampling_feature_num FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}

	/*
	 * Save Data to MoonDB
	 */
	public static void saveDatasets(Datasets datasets) {
		List<Dataset> dss = datasets.getDatasets();
	
		for(Dataset ds: dss) {
			String datasetCode = ds.getDatasetCode();
			String datasetType = ds.getDatasetType();
			String datasetTitle = ds.getDatasetTitle();
			int citationNum = ds.getCitationNum();
			String query;
			if (!isDatasetExist(datasetCode)) {
				//save to table dataset
				query = "INSERT INTO dataset(dataset_type,dataset_code,dataset_title) VALUES('"+datasetType+"','"+datasetCode+"','"+datasetTitle+"')";
				DatabaseUtil.update(query);
				Integer datasetNum = getDatasetNum(datasetCode);
				Integer relationshipTypeNum = 3;
				//save to table citation_dataset
				query = "INSERT INTO citation_dataset(citation_num,dataset_num,relationship_type_num) VALUES('"+citationNum+"',"+datasetNum+",'"+relationshipTypeNum+"')";
				DatabaseUtil.update(query);
			}			
	
		}
	}
	
	public static void saveSamplingFeature(SamplingFeatures samplingFeatures) {
		List<SamplingFeature> sfs = samplingFeatures.getSamplingFeatures();

		for(SamplingFeature sf: sfs) {
			String sfCode = sf.getSamplingFeatureCode();
			String sfParentCode = sf.getParentSamplingFeatureCode();
			String sfComment = sf.getSamplingFeatureComment();
			Integer sfTypeNum = sf.getSamplingFeatureTypeNum();
			
			String query;
			if (!isSamplingFeatureExist(sfCode)) {
				//save to table sampling_feature
				query = "INSERT INTO sampling_feature(sampling_feature_type_num,sampling_feature_code,sampling_feature_description) VALUES('"+sfTypeNum+"','"+sfCode+"','"+sfComment+"')";
				DatabaseUtil.update(query);
				
				if(sfParentCode != null) {
					Integer sfNum = getSamplingFeatureNum(sfCode);
					Integer sfParentNum = getSamplingFeatureNum(sfParentCode);
					Integer relationshipTypeNum = 9;  //isSubSampleOf
					//save to table related_feature
					query = "INSERT INTO related_feature(sampling_feature_num,related_sampling_feature_num,relationship_type_num) VALUES('"+sfNum+"',"+sfParentNum+",'"+relationshipTypeNum+"')";
					DatabaseUtil.update(query);
				}

			}			

		}
	}
}