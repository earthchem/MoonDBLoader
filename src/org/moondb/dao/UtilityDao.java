package org.moondb.dao;

import java.util.List;

import org.moondb.model.Action;
import org.moondb.model.Actions;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.Method;
import org.moondb.model.Methods;
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
	
	public static boolean isSamplingFeatureExist(String samplingFeatureCode, int samplingFeatureTypeNum) {
		String query = "SELECT COUNT(*) FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "' AND sampling_feature_type_num='" + samplingFeatureTypeNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	
	public static boolean isActionExist(String actionName) {
		String query = "SELECT COUNT(*) FROM action WHERE action_name='" + actionName + "' AND action_type_num=20";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
	public static boolean isMethodExist(String methodTech, Integer methodLabNum, String methodComment) {
		String query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodTech +"' AND orgnization_num='" + methodLabNum + "' AND method_description='"+methodComment + "' AND method_type_num=3";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}

	public static boolean isVariableExist(String variableCode) {
		String query = "SELECT COUNT(*) FROM variable WHERE variable_code='" + variableCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}
	
	public static boolean isVariableExist(String variableCode, int variableTypeNum) {
		String query = "SELECT COUNT(*) FROM variable WHERE variable_code='" + variableCode + "' AND variable_type_num='"+variableTypeNum+"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count >0)
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
	
	public static long getCountOfVariable(String variableCode) {
		String query = "SELECT COUNT(*) from variable where variable_code='" + variableCode + "'";
		return (long)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getVariableNum(String variableCode) {
		if (getCountOfVariable(variableCode) > 1) {
			String query = "SELECT variable_num, variable_type_num FROM variable WHERE variable_code='" + variableCode + "'";
			List<Object[]> records = DatabaseUtil.getRecords(query);
			Integer varNum = null;
			for(Object[] record: records) {
				switch ((int)record[1]) {
				case 9:                          //MAJ
					varNum = (int)record[0];
					break;
				case 6:                          //EM
					varNum = (int)record[0];
					break;
				case 7:                          //IR
					varNum = (int)record[0];
					break;
				case 13:                         //US
					varNum = (int)record[0];
					break;
				}
			}
			return varNum;
		} else {
			String query = "SELECT variable_num FROM variable WHERE variable_code='" + variableCode + "'";
			return (Integer)DatabaseUtil.getUniqueResult(query);	
		}

	}
	
	public static Integer getVariableNum(String variableCode, int variableTypeNum) {
		String query = "SELECT variable_num FROM variable WHERE variable_code='" + variableCode + "' AND variable_type_num='"+variableTypeNum +"'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getVariableTypeNum(String variableTypeCode) {
		String query = "SELECT variable_type_num FROM variable_type WHERE variable_type_code='" + variableTypeCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getUnitNum(String unitAbbr) {
		String query = "SELECT unit_num FROM unit WHERE unit_abbreviation='" + unitAbbr + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getMethodNum(String methodTech, Integer methodLabNum, String methodComment) {
		String query = "SELECT method_num FROM method WHERE method_code='" + methodTech + "' AND organization_num=" + methodLabNum + "' AND method_description='" + methodComment + "' AND method_type_num=3";
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
	
	public static Integer getActionNum(String actionName) {
		String query = "SELECT action_num FROM action WHERE action_name='" + actionName + "' AND action_type_num=20";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getSamplingFeatureNum(String samplingFeatureCode, int samplingFeatureTypeNum) {
		String query = "SELECT sampling_feature_num FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "' AND sampling_feature_type_num='" + samplingFeatureTypeNum +"'";
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
			String sfName = sf.getSamplingFeatureName();
			String sfComment = sf.getSamplingFeatureComment();
			Integer sfTypeNum = sf.getSamplingFeatureTypeNum();
			
			String query;
			if (!isSamplingFeatureExist(sfCode, sfTypeNum)) {
				//save to table sampling_feature
				query = "INSERT INTO sampling_feature(sampling_feature_type_num,sampling_feature_code,sampling_feature_name,sampling_feature_description) VALUES('"+sfTypeNum+"','"+sfCode+"','"+sfName+"','"+sfComment+"')";
				DatabaseUtil.update(query);
				
				if(sfParentCode != null) {
					Integer sfNum = getSamplingFeatureNum(sfCode, sfTypeNum);
					Integer sfParentNum = getSamplingFeatureNum(sfParentCode,1); //Parent sampling feature must be specimen
					Integer relationshipTypeNum = 9;  //isSubSampleOf
					//save to table related_feature
					query = "INSERT INTO related_feature(sampling_feature_num,related_sampling_feature_num,relationship_type_num) VALUES('"+sfNum+"',"+sfParentNum+",'"+relationshipTypeNum+"')";
					DatabaseUtil.update(query);
				}

			}			

		}
	}
	
	public static void saveActions(Actions actions) {
		List<Action> actionList = actions.getActions();
	
		for(Action action: actionList) {
			String actionName = action.getActionName();
			String actionDescription = action.getActionDesctription();
			int actionTypeNum = action.getActionTypeNum();
			int methodNum = action.getMethodNum();
			int datasetNum = action.getDatasetNum();
			String query;
			if (!isActionExist(actionName)) {
				//save to table action
				query = "INSERT INTO action(action_type_num,method_num,action_name,action_description,dataset_num) VALUES('"+actionTypeNum+"','"+methodNum+"','"+actionName+"',"+actionDescription+",'"+datasetNum+"')";
				DatabaseUtil.update(query);
			}			
	
		}
	}
	
	public static void saveMethods(Methods methods) {
		List<Method> methodList = methods.getMethods();
		
		for(Method method: methodList) {
			String methodTech = method.getMethodTechnique();
			Integer methodLabNum = method.getMethodLabNum();
			String methodComment = method.getMethodComment();
			String query;
			if(!isMethodExist(methodTech,methodLabNum,methodComment)) {
				//save to table method
				query = "INSERT INTO method(method_code,organization_num,method_description) VALUES('"+ methodTech+"','"+methodLabNum+"','"+methodComment+"')";
				DatabaseUtil.update(query);
			}
		}
		
	}
}