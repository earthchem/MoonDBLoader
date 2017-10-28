package org.moondb.dao;

import java.util.ArrayList;
import java.util.List;

import org.moondb.model.Action;
import org.moondb.model.Actions;
import org.moondb.model.Author;
import org.moondb.model.Citation;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.Landmark;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.util.DatabaseUtil;

public class UtilityDao {

	public static String formatQueryString (String str) {

		if(str != null && str.contains("'")) {
			
			str = str.replaceAll("'", "''"); 
		} 
		
		return str;
	}
	
	public static boolean isCitationExist(String moondbNum) {
		String query = "SELECT COUNT(*) FROM citation WHERE moondbnum='" + moondbNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
		
	}
	
	public static boolean isOrgExist(Integer orgNum) {
		if (orgNum == null)
			return false;
		String query = "SELECT COUNT(*) FROM organization WHERE organization_num='" + orgNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
		
	}
	
	public static boolean isDatasetExist(String datasetCode) {
		datasetCode = formatQueryString(datasetCode);
		String query = "SELECT COUNT(*) FROM dataset WHERE dataset_code='" + datasetCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isSamplingFeatureExist(String samplingFeatureCode, int samplingFeatureTypeNum) {
		samplingFeatureCode = formatQueryString(samplingFeatureCode);
		String query = "SELECT COUNT(*) FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "' AND sampling_feature_type_num='" + samplingFeatureTypeNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isActionExist(int actionTypeNum, int methodNum, String actionName) {
		actionName = formatQueryString(actionName);
		String query = "SELECT COUNT(*) FROM action WHERE method_num='" + methodNum + "' AND action_name='" + actionName + "' AND action_type_num='" + actionTypeNum +"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
	
	public static boolean isActionExist(int methodNum, int datasetNum, int actionTypeNum) {
		String query = "SELECT COUNT(*) FROM action WHERE method_num='" + methodNum + "' AND dataset_num='" + datasetNum + "' AND action_type_num='" + actionTypeNum +"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
	public static boolean isFeatureActionExist(int samplingFeatureNum, int actionNum) {
		String query = "SELECT COUNT(*) FROM feature_action WHERE sampling_feature_num='" + samplingFeatureNum + "' AND action_num='" + actionNum +"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}
	
	public static boolean isResultExist(int featureActionNum, int variableNum) {
		String query = "SELECT COUNT(*) FROM result WHERE feature_action_num='" + featureActionNum + "' AND variable_num='" + variableNum +"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isNumericDataExist(int resultNum, double value, int unitNum) {
		String query = "SELECT COUNT(*) FROM numeric_data WHERE result_num='" + resultNum + "' AND value_meas='" + value +"' AND unit_num='" + unitNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static boolean isMethodExist(String methodTech, Integer methodLabNum, String methodComment) {
		methodTech = formatQueryString(methodTech);
		methodComment = formatQueryString(methodComment);
		String query = null;
		
		if (methodComment == null && methodLabNum != null) {
			query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodTech +"' AND organization_num='" + methodLabNum + "' AND method_description is "+methodComment + " AND method_type_num=3";
		} else if (methodComment == null && methodLabNum == null) {
			query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodTech +"' AND organization_num is " + methodLabNum + " AND method_description is "+methodComment + " AND method_type_num=3";
		} else if (methodComment != null && methodLabNum == null) {
			query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodTech +"' AND organization_num is " + methodLabNum + " AND method_description='"+methodComment + "' AND method_type_num=3";
		} else {
			query = "SELECT COUNT(*) FROM method WHERE method_code='" + methodTech +"' AND organization_num='" + methodLabNum + "' AND method_description='"+methodComment + "' AND method_type_num=3";
		}
		
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}

	public static boolean isVariableExist(String variableCode) {
		variableCode = formatQueryString(variableCode);
		String query = "SELECT COUNT(*) FROM variable WHERE variable_code='" + variableCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;	
	}
	
	public static boolean isVariableExist(String variableCode, int variableTypeNum) {
		variableCode = formatQueryString(variableCode);
		String query = "SELECT COUNT(*) FROM variable WHERE variable_code='" + variableCode + "' AND variable_type_num='"+variableTypeNum+"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count >0)
			return true;
		else
			return false;	
	}
	
	public static boolean isUnitExist(String unitAbbr) {
		unitAbbr = formatQueryString(unitAbbr);
		String query = "SELECT COUNT(*) FROM unit WHERE unit_abbreviation='" + unitAbbr + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count == 1)
			return true;
		else
			return false;	
	}
	
	public static long getCountOfVariable(String variableCode) {
		variableCode = formatQueryString(variableCode);
		String query = "SELECT COUNT(*) from variable where variable_code='" + variableCode + "'";
		return (long)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getVariableNum(String variableCode) {
		variableCode = formatQueryString(variableCode);
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
		variableCode = formatQueryString(variableCode);
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
	
	
	/*
	 * Fit for Expedition Method
	 */
	public static Integer getMethodNum(String methodCode, int methodTypeNum) {
		methodCode = formatQueryString(methodCode);
		String query = "SELECT method_num FROM method WHERE method_code='" + methodCode + "' AND method_type_num='" + methodTypeNum + "'" ;
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	/*
	 * Fit for Lab Analysis method
	 */
	public static Integer getMethodNum(String methodTech, Integer methodLabNum, String methodComment) {
		methodTech = formatQueryString(methodTech);
		methodComment = formatQueryString(methodComment);
		
		String query = null;
		if(methodComment == null) {
			query = "SELECT method_num FROM method WHERE method_code='" + methodTech + "' AND organization_num=" + methodLabNum + " AND method_description is " + methodComment + " AND method_type_num=3";
		} else {
			query = "SELECT method_num FROM method WHERE method_code='" + methodTech + "' AND organization_num=" + methodLabNum + " AND method_description='" + methodComment + "' AND method_type_num=3";
		}
		//System.out.println(query);
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
		datasetCode = formatQueryString(datasetCode);
		String query = "SELECT dataset_num FROM dataset WHERE dataset_code='" + datasetCode + "'";
		//System.out.println(query);
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getActionNum(int datasetNum, int methodNum, int actionTypeNum) {
		String query = "SELECT action_num FROM action WHERE dataset_num='" + datasetNum + "' AND method_num='" + methodNum +"' AND action_type_num='" + actionTypeNum + "'";
		//System.out.println(query);
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getActionNum(String actionName, int methodNum, int actionTypeNum) {
		actionName = formatQueryString(actionName);
		String query = "SELECT action_num FROM action WHERE action_name='" + actionName + "' AND method_num='" + methodNum +"' AND action_type_num='" + actionTypeNum + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getFeatureActionNum (int samplingFeatureNum, int actionNum) {
		String query = "SELECT feature_action_num FROM feature_action WHERE sampling_feature_num='" + samplingFeatureNum + "' AND action_num='" + actionNum + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getResultNum (int featureActionNum, int variableNum) {
		String query = "SELECT result_num FROM result WHERE feature_action_num='" + featureActionNum + "' AND variable_num='" + variableNum + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getSamplingFeatureNum(String samplingFeatureCode, int samplingFeatureTypeNum) {
		samplingFeatureCode = formatQueryString(samplingFeatureCode);
		String query = "SELECT sampling_feature_num FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "' AND sampling_feature_type_num='" + samplingFeatureTypeNum +"'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}

	public static Integer getFeatureOfInterestNum(String foitName,int foitTypeNum) {
		//if (foitName.contains("'")) 
		//	foitName = foitName.replaceAll("'", "''");
		foitName = formatQueryString(foitName);
		String query = "SELECT feature_of_interest_cv_num FROM feature_of_interest_cv WHERE feature_of_interest_cv_name='" + foitName + "' AND feature_of_interest_type_num='" + foitTypeNum +"'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getMaterialNum (String materialCode) {
		String query = "SELECT material_num FROM material WHERE material_code='" + materialCode  +"'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static Integer getTaxonomicClassifierNum (String tcName) {
		String query = "SELECT taxonomic_classifier_num FROM taxonomic_classifier WHERE taxonomic_classifier_name='" + tcName  +"'";
		//System.out.println(query);
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
		
	public static void saveSamplingFeatureTaxonomicClassifier(int sfNum, int tcNum) {
		String query = "INSERT INTO sampling_feature_taxonomic_classifier(sampling_feature_num,taxonomic_classifier_num) VALUES("+sfNum+"," + tcNum +")";
		DatabaseUtil.update(query);
	}
	
	public static void saveSamplingFeatureMaterial(int sfNum, int materialNum) {
		String query = "INSERT INTO sampling_feature_material(sampling_feature_num,material_num) VALUES("+sfNum+"," + materialNum +")";
		DatabaseUtil.update(query);
	}
	
	/*
	 * Save Data to MoonDB
	 */
	public static void saveDatasets(Datasets datasets) {
		List<Dataset> dss = datasets.getDatasets();
	
		for(Dataset ds: dss) {
			String datasetCode = formatQueryString(ds.getDatasetCode());
			String datasetType = ds.getDatasetType();
			String datasetTitle = formatQueryString(ds.getDatasetTitle());
			int citationNum = ds.getCitationNum();
			String query;
			if (!isDatasetExist(datasetCode)) {
				//save to table dataset
				query = "INSERT INTO dataset(dataset_type,dataset_code,dataset_title) VALUES('"+datasetType+"','"+datasetCode+"','"+datasetTitle+"')";
				DatabaseUtil.update(query);
				Integer datasetNum = getDatasetNum(datasetCode);
				Integer relationshipTypeNum = 3;
				//save to table citation_dataset
				query = "INSERT INTO citation_dataset(citation_num,dataset_num,relationship_type_num) VALUES("+citationNum+","+datasetNum+","+relationshipTypeNum+")";
				DatabaseUtil.update(query);
			}			
	
		}
	}
	
	public static void saveSamplingFeature (SamplingFeature samplingFeature) {
		String sfCode = samplingFeature.getSamplingFeatureCode();
		String sfName = formatQueryString(samplingFeature.getSamplingFeatureName());
		String sfParentCode = samplingFeature.getParentSamplingFeatureCode();
		String sfComment = formatQueryString(samplingFeature.getSamplingFeatureComment());
		Integer sfTypeNum = samplingFeature.getSamplingFeatureTypeNum();
		
		String query;
		if (!isSamplingFeatureExist(sfCode, sfTypeNum)) {
			//save to table sampling_feature
			if(sfComment == null) {
				query = "INSERT INTO sampling_feature(sampling_feature_type_num,sampling_feature_code,sampling_feature_name,sampling_feature_description) VALUES("+sfTypeNum+",'"+sfCode+"','"+sfName+"',"+sfComment+")";
			} else {
				query = "INSERT INTO sampling_feature(sampling_feature_type_num,sampling_feature_code,sampling_feature_name,sampling_feature_description) VALUES("+sfTypeNum+",'"+sfCode+"','"+sfName+"','"+sfComment+"')";
			}
			DatabaseUtil.update(query);
			
			if(sfParentCode != null && sfParentCode != sfCode) {
				Integer sfNum = getSamplingFeatureNum(sfCode, sfTypeNum);
				Integer sfParentNum = getSamplingFeatureNum(sfParentCode,MoonDBType.SAMPLING_FEATURE_TYPE_SPECIMEN.getValue()); //Parent sampling feature must be specimen
				Integer relationshipTypeNum = 9;  //isSubSampleOf
				//save to table related_feature
				query = "INSERT INTO related_feature(sampling_feature_num,related_sampling_feature_num,relationship_type_num) VALUES("+sfNum+","+sfParentNum+","+relationshipTypeNum+")";
				//System.out.println(query);
				DatabaseUtil.update(query);
			}
		}
	}
	
	public static void saveSamplingFeatures(SamplingFeatures samplingFeatures) {
		List<SamplingFeature> sfs = samplingFeatures.getSamplingFeatures();

		for(SamplingFeature sf: sfs) {
			saveSamplingFeature(sf);
		}
	}
	
	
	public static void saveAction (Action action) {
		String actionName = formatQueryString(action.getActionName());
		int actionTypeNum = action.getActionTypeNum();
		int methodNum = action.getMethodNum();
		
		String query;
		if (!isActionExist(actionTypeNum, methodNum, actionName)) {
			//save to table action
			query = "INSERT INTO action(action_type_num,method_num,action_name) VALUES("+actionTypeNum+","+methodNum+",'"+actionName +"')";
			DatabaseUtil.update(query);
		}	
	}
	
	public static void saveActions(Actions actions) {
		List<Action> actionList = actions.getActions();
	
		for(Action action: actionList) {
			String actionName = formatQueryString(action.getActionName());
			String actionDescription = formatQueryString(action.getActionDesctription());
			int actionTypeNum = action.getActionTypeNum();
			int methodNum = action.getMethodNum();
			int datasetNum = action.getDatasetNum();
			String query;
			if (!isActionExist(methodNum,datasetNum,actionTypeNum)) {
				//save to table action
				query = "INSERT INTO action(action_type_num,method_num,action_name,action_description,dataset_num) VALUES("+actionTypeNum+","+methodNum+",'"+actionName+"','"+actionDescription+"',"+datasetNum+")";
				DatabaseUtil.update(query);
			}			
	
		}
	}
	
	public static void saveFeatureAction(int samplingFeatureNum, int actionNum) {
		if (!isFeatureActionExist(samplingFeatureNum, actionNum)) {
			String query = "INSERT INTO feature_action(sampling_feature_num, action_num) values(" + samplingFeatureNum +"," + actionNum + ")";
			DatabaseUtil.update(query);	
		}
	}
	
	public static void saveResult(int featureActionNum, int variableNum, int resultTypeNum, String valueType) {
		//int resultTypeNum = MoonDBType.RESULT_TYPE_MEASUREMENT.getValue();
		int processingLevelNum = MoonDBType.PROCESSING_LEVEL_RAW_DATA.getValue();
		int valueCount = 1;
		//String valueType = "numeric";
		if (!isResultExist(featureActionNum, variableNum)) {
			String query = "INSERT INTO result(feature_action_num, result_type_num, variable_num, processing_level_num, value_count, value_type) values(" + featureActionNum + "," + resultTypeNum + "," + variableNum + "," + processingLevelNum + "," + valueCount + ",'" + valueType + "')";
			DatabaseUtil.update(query);
		}
	}
	
	public static void saveNumericData(int resultNum, double value, int unitNum) {
		if (!isNumericDataExist(resultNum, value, unitNum)) {
			String query = "INSERT INTO numeric_data(result_num, value_meas, unit_num) values(" + resultNum + "," + value + "," + unitNum + ")";
			DatabaseUtil.update(query);
		}
	}
	
	public static void saveTextData (int resultNum, String value, String note) {
		String query = "INSERT INTO text_data(result_num, text_data_value, text_data_note) values(" + resultNum + ",'" + value + "','" + note + "')";
		DatabaseUtil.update(query);
	}
	
	public static void saveMethods(Methods methods) {
		List<Method> methodList = methods.getMethods();
		
		for(Method method: methodList) {
			String methodTech = method.getMethodTechnique();
			Integer methodLabNum = method.getMethodLabNum();
			String methodComment = formatQueryString(method.getMethodComment());
			String methodName = method.getMethodName();
			Integer methodTypeNum = method.getMethodTypeNum();
			
			String query;
			if(!isMethodExist(methodTech,methodLabNum,methodComment)) {
				//save to table method
				if(methodComment == null && methodLabNum != null) {
					query = "INSERT INTO method(method_type_num, method_code,organization_num,method_description, method_name) VALUES("+ methodTypeNum + ",'"+ methodTech+"',"+methodLabNum+","+methodComment +",'" + methodName+"')";

				} else if (methodComment == null && methodLabNum == null) {
					query = "INSERT INTO method(method_type_num, method_code,organization_num,method_description, method_name) VALUES("+ methodTypeNum + ",'"+ methodTech+"',"+methodLabNum+","+methodComment +",'" + methodName+"')";

				} else if (methodComment != null && methodLabNum == null) {
					query = "INSERT INTO method(method_type_num, method_code,organization_num,method_description, method_name) VALUES("+ methodTypeNum + ",'"+ methodTech+"',"+methodLabNum+",'"+methodComment +"','" + methodName+"')";

				} else {
					query = "INSERT INTO method(method_type_num, method_code,organization_num,method_description, method_name) VALUES("+ methodTypeNum + ",'"+ methodTech+"',"+methodLabNum+",'"+methodComment +"','" + methodName+"')";
				}
				
				DatabaseUtil.update(query);
			}
		}
		
	}
	
	public static boolean isAnnotationExist(int annotationTypeNum, String annotationText, int citationNum) {
		annotationText = formatQueryString(annotationText);
		String query = "SELECT COUNT(*) FROM annotation WHERE annotation_type_num='" + annotationTypeNum + "' AND annotation_text='"+annotationText + "' AND data_source_num='" + citationNum +"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count >0)
			return true;
		else
			return false;	
	}
	
	public static Integer getAnnotationNum (int annotationTypeNum, String annotationText, int citationNum) {
		annotationText = formatQueryString(annotationText);
		String query = "SELECT annotation_num FROM annotation WHERE annotation_type_num='" + annotationTypeNum + "' AND annotation_text='" + annotationText + "' AND data_source_num='" + citationNum + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public static void saveAnnotation(int annotationTypeNum, String annotationText, int citationNum) {
		annotationText = formatQueryString(annotationText);
		if (!isAnnotationExist(annotationTypeNum, annotationText, citationNum)) {
			String query = "INSERT INTO annotation(annotation_type_num, annotation_text, data_source_num) values('" + annotationTypeNum + "','" + annotationText + "','" + citationNum + "')";
			DatabaseUtil.update(query);
		}	
	}
	
	public static boolean isSamplingFeatureAnnotationExist(int samplingFeatureNum, int annotationNum) {
		String query = "SELECT COUNT(*) FROM sampling_feature_annotation WHERE sampling_feature_num='" + samplingFeatureNum +"' AND annotation_num='" + annotationNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count >0)
			return true;
		else
			return false;	
	}
	
	public static void saveSamplingFeatureAnnotation(int samplingFeatureNum, int annotatioNum) {
		if (!isSamplingFeatureAnnotationExist(samplingFeatureNum, annotatioNum)) {
			String query = "INSERT INTO sampling_feature_annotation(sampling_feature_num, annotation_num) values('" + samplingFeatureNum + "','" + annotatioNum + "')";
			DatabaseUtil.update(query);
		}
	}
	
	public static boolean isDatasetResultExist (int datasetNum, int resultNum) {
		String query = "SELECT COUNT(*) FROM dataset_result WHERE dataset_num='" + datasetNum +"' AND result_num='" + resultNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count >0)
			return true;
		else
			return false;
	}
	
	public static void saveDatasetResult (int datasetNum, int resultNum) {
		if (!isDatasetResultExist(datasetNum, resultNum)) {
			String query = "INSERT INTO dataset_result(dataset_num, result_num) values('" + datasetNum + "','" + resultNum + "')";
			DatabaseUtil.update(query);
		}
	}
	
	public static boolean isFeatureOfInterestExist (int sfNum, int foitNum) {
		String query = "SELECT COUNT(*) FROM feature_of_interest WHERE sampling_feature_num='" + sfNum + "' AND feature_of_interest_cv_num='" + foitNum + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public static void saveFeatureOfInterest (int sfNum, int foitNum) {
		if (!isFeatureOfInterestExist(sfNum, foitNum)) {
			String query = "INSERT INTO feature_of_interest(sampling_feature_num,feature_of_interest_cv_num) values('" + sfNum + "','" + foitNum + "')";
			DatabaseUtil.update(query);
		}
	}
	
	
	//get all samplingFeatures
	public static List<Integer> getSFNums() {
		List<Integer> samplingFeatureNums = new ArrayList<Integer>();
		String query = "SELECT sampling_feature_num FROM sampling_feature WHERE sampling_feature_type_num=1 and sampling_feature_num>46335 ORDER BY sampling_feature_num ASC";
		List<Object []> sfNums = DatabaseUtil.getRecords(query);
		for (Object [] sfNum : sfNums) {
			samplingFeatureNums.add((Integer)sfNum[0]);
		}
		return samplingFeatureNums;
	}
	
	//get child sampling featues
	public static List<String> getChildSamplingFeatures(int samplingFeatureNum) {
		List<String> childSfCodes = new ArrayList<String>();
		//get child_sampling_feature
		String query = "SELECT sf.sampling_feature_code from related_feature rf join sampling_feature sf on rf.sampling_feature_num=sf.sampling_feature_num where rf.related_sampling_feature_num="+ samplingFeatureNum +" and sf.sampling_feature_type_num=1";
		List<Object []> sfCodes = DatabaseUtil.getRecords(query);
		for (Object [] sfCode : sfCodes) {
			childSfCodes.add((String)sfCode[0]);
		}
		return childSfCodes;
	}
	
	//get analysis results by samplingFeatureNum
	public static List<Object []> getAnalysisResuts(int samplingFeatureNum) {
		String query = "SELECT distinct sampling_feature_num,sampling_feature_name,analysis_comment,dataset_code,dataset_title,citation_code FROM view_analysis where parent_sampling_feature_num=" + samplingFeatureNum;
		List<Object []> results = DatabaseUtil.getRecords(query);
		return results;
	}
	
	//get analysis results by samplingFeatureNum
	public static List<Object []> getAnalysisData(int samplingFeatureNum) {
		String query = "SELECT variable_code,value,unit,method_code,lab_name,method_description FROM view_analysis where sampling_feature_num=" + samplingFeatureNum;
		List<Object []> results = DatabaseUtil.getRecords(query);
		return results;
	}
	
    public static Citation getCitation(String citationCode) {
    	citationCode = formatQueryString(citationCode);
    	Citation citation = new Citation();
    	String query = "SELECT title,publication_year,journal,issue,volume,pages,citation_type,citation_num from citation where citation_code='" + citationCode + "'";
    	List<Object []> ct = DatabaseUtil.getRecords(query);
    	citation.setCitationCode(citationCode);
    	citation.setRefTitle((String) ct.get(0)[0]);
    	citation.setRefYear((Integer) ct.get(0)[1]);
    	citation.setRefJournal((String) ct.get(0)[2]);
    	citation.setRefIssue((String) ct.get(0)[3]);
    	citation.setRefVolume((String) ct.get(0)[4]);
    	citation.setRefPages((String) ct.get(0)[5]);
    	citation.setRefType((String) ct.get(0)[6]);
    	citation.setCitationNum((Integer) ct.get(0)[7]);
    	
    	query = "SELECT citation_external_identifier FROM  citation_external_identifier WHERE external_identifier_system_num=3 AND citation_num=" + citation.getCitationNum();
    	String refDOI = (String) DatabaseUtil.getUniqueResult(query);
    	citation.setRefDOI(refDOI);
    	
    	query = "select first_name,last_name,author_order from author_list al join person p on al.person_num=p.person_num where citation_num=" + citation.getCitationNum();
    	List<Object []> refAuthors = DatabaseUtil.getRecords(query);
    	List<Author> authors = new ArrayList<Author>();
    	for (Object[] refAuthor: refAuthors) {
    		Author author = new Author();
    		author.setFirstName((String) refAuthor[0]);
    		author.setLastName((String) refAuthor[1]);
    		author.setFullName(author.getLastName()+", "+ author.getFirstName());
    		author.setAuthorOrder((Integer) refAuthor[2]);
    		authors.add(author);
    	}
    	citation.setAuthors(authors);
    	return citation;   	
    }
	//get samplingFeature by samplingFeatureNum
	public static SamplingFeature getSamplingFeature(int samplingFeatureNum) {
		SamplingFeature samplingFeature = new SamplingFeature();
		String query = "SELECT sf.sampling_feature_code,sf.sampling_feature_name,sf.sampling_feature_description,sft.sampling_feature_type_name FROM sampling_feature sf JOIN sampling_feature_type sft ON sf.sampling_feature_type_num=sft.sampling_feature_type_num WHERE sampling_feature_num=" + samplingFeatureNum;
		List<Object []> sf = DatabaseUtil.getRecords(query);
		
		samplingFeature.setSamplingFeatureCode((String) sf.get(0)[0]);
		samplingFeature.setSamplingFeatureName((String) sf.get(0)[1]);
		samplingFeature.setSamplingFeatureComment((String) sf.get(0)[2]);
		samplingFeature.setSamplingFeatureTypeName((String) sf.get(0)[3]);

		
		//get parent_sampling_feature
		query = "SELECT sf.sampling_feature_code from related_feature rf join sampling_feature sf on rf.related_sampling_feature_num=sf.sampling_feature_num where rf.sampling_feature_num="+ samplingFeatureNum;
		String sfParentSfCode = (String) DatabaseUtil.getUniqueResult(query);
		samplingFeature.setParentSamplingFeatrureCode(sfParentSfCode);

		//get child_sampling_feature
		List<String> childSfCodes = new ArrayList<String>();
		query = "SELECT sf.sampling_feature_code from related_feature rf join sampling_feature sf on rf.sampling_feature_num=sf.sampling_feature_num where rf.related_sampling_feature_num="+ samplingFeatureNum + " AND sf.sampling_feature_type_num=1";
		List<Object []> sfCodes = DatabaseUtil.getRecords(query);
		for (Object [] sfCode : sfCodes) {
			childSfCodes.add((String)sfCode[0]);
		}
		samplingFeature.setChildSfCodes(childSfCodes);

		//get sampling_feature_material_code and sampling_feature_material_name
		if (sfParentSfCode != null) {
			query = "select material_code,material_name from sampling_feature_material sfm join material mt on sfm.material_num=mt.material_num join sampling_feature sf ON sfm.sampling_feature_num = sf.sampling_feature_num where sf.sampling_feature_code='" + sfParentSfCode + "'";

		} else {
			query = "select material_code,material_name from sampling_feature_material sfm join material mt on sfm.material_num=mt.material_num  where sfm.sampling_feature_num=" + samplingFeatureNum;
		}
		List<Object []> sfm = DatabaseUtil.getRecords(query);
		if(sfm.size() != 0) {
			samplingFeature.setMaterialCode((String) sfm.get(0)[0]);
			samplingFeature.setMaterialName((String) sfm.get(0)[1]);
		}
		
		//get taxon name and parent taxon name
		if(sfParentSfCode != null) {
			query = "select tc.taxonomic_classifier_name,tc1.taxonomic_classifier_name from sampling_feature_taxonomic_classifier sftc join taxonomic_classifier tc on sftc.taxonomic_classifier_num=tc.taxonomic_classifier_num join taxonomic_classifier tc1 on tc.parent_taxonomic_classifier_num=tc1.taxonomic_classifier_num join sampling_feature sf ON sftc.sampling_feature_num = sf.sampling_feature_num where sf.sampling_feature_code='" + sfParentSfCode + "'";

		} else {
			query = "select tc.taxonomic_classifier_name,tc1.taxonomic_classifier_name from sampling_feature_taxonomic_classifier sftc join taxonomic_classifier tc on sftc.taxonomic_classifier_num=tc.taxonomic_classifier_num join taxonomic_classifier tc1 on tc.parent_taxonomic_classifier_num=tc1.taxonomic_classifier_num where sftc.sampling_feature_num=" + samplingFeatureNum;
		}
		List<Object []> sftc = DatabaseUtil.getRecords(query);
		if(sftc.size() != 0) {
			samplingFeature.setTaxonName((String) sftc.get(0)[0]);
			samplingFeature.setParentTaxonName((String) sftc.get(0)[1]);
		}
		
		//get mission name
		//copy mission info from parent if parent exist
		if (sfParentSfCode != null) {
			query ="SELECT action_name from sampling_feature sf join feature_action fa on sf.sampling_feature_num=fa.sampling_feature_num join action a on fa.action_num=a.action_num where action_type_num=11 and sf.sampling_feature_code='" + sfParentSfCode + "'";

		} else {
			query ="SELECT action_name from sampling_feature sf join feature_action fa on sf.sampling_feature_num=fa.sampling_feature_num join action a on fa.action_num=a.action_num where action_type_num=11 and sf.sampling_feature_num=" + samplingFeatureNum;
		}
		String missionName = (String) DatabaseUtil.getUniqueResult(query);
		samplingFeature.setMissionName(missionName);
		
		//get sampling method name
		query = "SELECT me.method_name from sampling_feature sf join feature_action fa on sf.sampling_feature_num=fa.sampling_feature_num join action a on fa.action_num=a.action_num join method me on a.method_num=me.method_num where action_type_num=21 and sf.sampling_feature_num=" + samplingFeatureNum;
		String samplingTechniqueName = (String) DatabaseUtil.getUniqueResult(query);
		samplingFeature.setSamplingTechniqueName(samplingTechniqueName);

		//get landmark station
		query = "select foic.feature_of_interest_cv_name,foicei.feature_of_interest_cv_external_id,foicei.feature_of_interest_cv_external_identifier_url,public.st_y(foic.feature_of_interest_geometry),public.st_x(foic.feature_of_interest_geometry) from sampling_feature sf join feature_of_interest foi on sf.sampling_feature_num = foi.sampling_feature_num join feature_of_interest_cv foic on foi.feature_of_interest_cv_num= foic.feature_of_interest_cv_num JOIN feature_of_interest_cv_external_identifier foicei ON foic.feature_of_interest_cv_num = foicei.feature_of_interest_cv_num where feature_of_interest_type_num=1 and sf.sampling_feature_num=" + samplingFeatureNum;
		List<Object []> lm = DatabaseUtil.getRecords(query); 
		Landmark landmark = new Landmark();
		if(lm.size() != 0) {
			landmark.setLandmarkName((String) lm.get(0)[0]);
			landmark.setGpnfID(Integer.parseInt((String) lm.get(0)[1]));
			landmark.setGpnfURL((String) lm.get(0)[2]);
			landmark.setLatitude((Double) lm.get(0)[3]);
			landmark.setLongitude((Double) lm.get(0)[4]);	
			samplingFeature.setLandMark(landmark);
		} else {
			landmark.setLandmarkName(null);
			landmark.setGpnfID(null);
			landmark.setGpnfURL(null);
			landmark.setLatitude(null);
			landmark.setLongitude(null);
			samplingFeature.setLandMark(landmark);
		}


		
		//get lunar station
		query = "select foic.feature_of_interest_cv_name from sampling_feature sf join feature_of_interest foi on sf.sampling_feature_num = foi.sampling_feature_num join feature_of_interest_cv foic on foi.feature_of_interest_cv_num= foic.feature_of_interest_cv_num where feature_of_interest_type_num=2 and sf.sampling_feature_num=" + samplingFeatureNum;
		String lunarStation = (String) DatabaseUtil.getUniqueResult(query);
		samplingFeature.setLunarStation(lunarStation);
		
		//get return contationer
		query = "select foic.feature_of_interest_cv_name from sampling_feature sf join feature_of_interest foi on sf.sampling_feature_num = foi.sampling_feature_num join feature_of_interest_cv foic on foi.feature_of_interest_cv_num= foic.feature_of_interest_cv_num where feature_of_interest_type_num=3 and sf.sampling_feature_num=" + samplingFeatureNum;
		String returnContainer = (String) DatabaseUtil.getUniqueResult(query);
		samplingFeature.setReturnContainer(returnContainer);
		
		return samplingFeature;
	}
	
	public static List<Integer> getDatasetNums(int citationNum) {
		List<Integer> datasetNums = new ArrayList<Integer>();
		String query = "SELECT dataset_num FROM citation_dataset where citation_num ='" + citationNum + "'";
		List<Object []> dsNums = DatabaseUtil.getRecords(query);

		for (Object [] dsNum : dsNums) {
			datasetNums.add((Integer)dsNum[0]);
		}
		return datasetNums;
		
	}
	
	public static List<Integer> getResultNums (int datasetNum) {
		List<Integer> resultNums = new ArrayList<Integer>();
		String query = "SELECT result_num FROM dataset_result where dataset_num ='" + datasetNum + "'";
		List<Object []> rsNums = DatabaseUtil.getRecords(query);

		for (Object [] rsNum : rsNums) {
			resultNums.add((Integer)rsNum[0]);
		}
		return resultNums;
	}
	
	public static List<Integer> getActionNums (int datasetNum) {
		List<Integer> actionNums = new ArrayList<Integer>();
		String query = "SELECT action_num FROM action where dataset_num ='" + datasetNum + "'";
		List<Object []> acNums = DatabaseUtil.getRecords(query);

		for (Object [] acNum : acNums) {
			actionNums.add((Integer)acNum[0]);
		}
		return actionNums;
	}
	
	public static List<Integer> getSfNumsByActionNum (int actionNum) {
		List<Integer> samplingFeatureNums = new ArrayList<Integer>();
		String query = "SELECT sampling_feature_num FROM feature_action where action_num ='" + actionNum + "'";
		List<Object[]> sfNums = DatabaseUtil.getRecords(query);
		
		for (Object[] sfNum : sfNums) {

			samplingFeatureNums.add((Integer)sfNum[0]);

		}
		return samplingFeatureNums;
	}
	
	
	public static void cleanPaperData(String moondbNum) {
		List<String> queries = new ArrayList<String>();
		String query = null;
				
		int citationNum = getCitationNum(moondbNum);
		List<Integer> dsNums = getDatasetNums(citationNum);
		for(Integer dsNum : dsNums) {

			List<Integer> rsNums = getResultNums(dsNum);
			query = "DELETE FROM dataset_result where dataset_num='" + dsNum + "'";
			queries.add(query);
			
			for(Integer rsNum : rsNums) {
				query = "DELETE FROM numeric_data where result_num='" + rsNum + "'";
				queries.add(query);

				query = "DELETE FROM result where result_num='" + rsNum + "'";
				queries.add(query);
			}

	        List<Integer> sfNums = new ArrayList<Integer>();		
			List<Integer> acNums = getActionNums(dsNum);
			for(Integer acNum : acNums) {
				if(getSfNumsByActionNum(acNum).size()>0)
					sfNums.addAll(getSfNumsByActionNum(acNum));
				query = "DELETE FROM feature_action where action_num='" + acNum + "'";
				queries.add(query);

				query = "DELETE FROM action where action_num='" + acNum + "'";
				queries.add(query);		
			}
			
			
			for(Integer sfNum : sfNums) {
				
				query = "DELETE FROM related_feature where sampling_feature_num='" + sfNum + "'";
				queries.add(query);
				query = "DELETE FROM sampling_feature_annotation where sampling_feature_num='" + sfNum + "'";
				queries.add(query);
				query = "DELETE FROM sampling_feature where sampling_feature_num='" + sfNum + "'";
				queries.add(query);
			}
	
			query = "DELETE FROM citation_dataset where dataset_num='" + dsNum + "'";
			queries.add(query);
			query = "DELETE FROM dataset where dataset_num='" + dsNum + "'";
			queries.add(query);
		}
		
		if (queries.size() > 0)
			DatabaseUtil.update(queries);
	}
	
	public static void cleanMoonDB () {
		List<String> queries = new ArrayList<String>();
		String query = null;
		
		query = "DELETE FROM numeric_data";
		queries.add(query);
		query = "ALTER SEQUENCE numeric_data_numeric_data_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM text_data";
		queries.add(query);
		
		query = "DELETE FROM dataset_result";
		queries.add(query);
		query = "ALTER SEQUENCE dataset_result_bridge_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM result";
		queries.add(query);
		query = "ALTER SEQUENCE result_result_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM citation_dataset";
		queries.add(query);
		query = "ALTER SEQUENCE citation_dataset_citation_dataset_num_seq RESTART WITH 1";
		queries.add(query);
		
		
		query = "DELETE FROM dataset";
		queries.add(query);
		query = "ALTER SEQUENCE dataset_dataset_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM feature_action";
		queries.add(query);
		query = "ALTER SEQUENCE feature_action_feature_action_num_seq RESTART WITH 1";
		queries.add(query);
		
		
		query = "DELETE FROM action";
		queries.add(query);
		query = "ALTER SEQUENCE action_action_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM sampling_feature_material";
		queries.add(query);
		
		query = "DELETE FROM sampling_feature_taxonomic_classifier";
		queries.add(query);
		query = "ALTER SEQUENCE sampling_feature_taxonomic_classifier_bridge_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM related_feature";
		queries.add(query);
		query = "ALTER SEQUENCE related_feature_related_feature_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM sampling_feature_annotation";
		queries.add(query);
		query = "ALTER SEQUENCE sampling_feature_annotation_sampling_feature_annotation_num_seq RESTART WITH 1";
		queries.add(query);

		query = "DELETE FROM feature_of_interest";
		queries.add(query);
		query = "ALTER SEQUENCE feature_of_interest_feature_of_interest_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM sampling_feature";
		queries.add(query);	
		query = "ALTER SEQUENCE sampling_feature_sampling_feature_num_seq RESTART WITH 1";
		queries.add(query);
		
		query = "DELETE FROM annotation";
		queries.add(query);		
		query = "ALTER SEQUENCE annotation_annotation_num_seq RESTART WITH 1";
		queries.add(query);
		
		DatabaseUtil.update(queries);
	}
	
}