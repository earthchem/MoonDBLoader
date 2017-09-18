package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.RowCellPos;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;

public class SamplingFeatureParser {
	
	private static String formatSamplingFeatureName (String samplingFeatureName) {
		/*
		 * if cell type is numeric, the return value will be formatted as numeric, like value 1000 will be returned as 1000.0
		 * need to be converted to 1000,0
		 */
		if(samplingFeatureName.endsWith(".0")) {          
			samplingFeatureName = samplingFeatureName.replaceAll(".0", ",0");
		} 
		/*
		 * if cell type is string, the return value will be formatted as string, like value 1000 will be returned as 1000
		 * need to be converted to 1000,0
		 */
		else if (!samplingFeatureName.contains(",")) {  
			samplingFeatureName = samplingFeatureName + ",0";		
		}
		
		return samplingFeatureName;
	}
	
	public static SamplingFeatures parseSamplingFeature (HSSFWorkbook workbook, String sheetName, String moondbNum) {
		
		SamplingFeatures samplingFeatures = new SamplingFeatures();
		HSSFSheet sheet = workbook.getSheet(sheetName);
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<SamplingFeature> sfList = new ArrayList<SamplingFeature>();
		
		int sfTypeNum = 0;
		int beginRowNum = 0;
		switch(sheetName) {
		case "SAMPLES":
			sfTypeNum = 1;  //Specimen sampling feature
			beginRowNum = RowCellPos.SAMPLES_ROW_B.getValue();
			break;
		case "ROCKS":
			sfTypeNum = 2;  //RockAnalysis sampling feature
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			break;
		case "MINERALS":
			sfTypeNum = 3;  //MineralAnalysis sampling feature
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			break;
		case "INCLUSIONS":
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			sfTypeNum = 4;  //InclusionAnalysis sampling feature
			break;
		}
		while (rowIterator.hasNext()) {
			SamplingFeature samplingFeature = new SamplingFeature();
			Row row = rowIterator.next();

			if(row.getRowNum()>= beginRowNum) {
				String resultNum = XlsParser.getCellValueString(row.getCell(0));   //corresponding to SAMPLE_ID in sheet SAMPLES, ANALYSIS NO. in sheet ROCKS,MINERIALS and INCLUSIONS 
				if (resultNum.equals("-1.0") || resultNum.equals("-1")) {    //data ending at the row
					break;
				}
				
				String samplingFeatureCode = null;
				String samplingFeatureName = null;
				String samplingFeatureComment = null;
				String parentSamplingFeatureCode = null;
				
				if(sheetName == "SAMPLES") {
					samplingFeatureName = XlsParser.getCellValueString(row.getCell(1));
					samplingFeatureName = formatSamplingFeatureName(samplingFeatureName);
					
					//keep samplingFeatureCode be same as samplingFeatureName for specimen sampling feature
					samplingFeatureCode = samplingFeatureName;
					
					//sampling feature is root sampling feature, no parent sampling feature
					if(samplingFeatureCode.endsWith(",0")) {
						parentSamplingFeatureCode = null;
					} 
					//sampling feature is not root sampling feature
					else {
						parentSamplingFeatureCode = samplingFeatureCode.substring(0, samplingFeatureCode.indexOf(',')) + ",0";
					}	
					
					samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));	
					
				} else if(sheetName == "ROCKS"){
					samplingFeatureName = formatSamplingFeatureName(XlsParser.getCellValueString(row.getCell(2)));
					samplingFeatureCode = samplingFeatureName + "#" + resultNum + "#" + moondbNum;
					parentSamplingFeatureCode = samplingFeatureName;
					samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));	
					
				} else if (sheetName == "MINERALS") {
					samplingFeatureName = formatSamplingFeatureName(XlsParser.getCellValueString(row.getCell(2)));
					samplingFeatureCode = samplingFeatureName + "#" + resultNum + "#" + moondbNum;
					parentSamplingFeatureCode = samplingFeatureName;
					String spotID = XlsParser.getCellValueString(row.getCell(4));
					if(spotID.endsWith(".0"))
						spotID = spotID.replaceAll(".0", "").trim();
					samplingFeatureName = samplingFeatureName + "#" + spotID;
					samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));						
					
				} else if (sheetName == "INCLUSIONS") {
					samplingFeatureName = formatSamplingFeatureName(XlsParser.getCellValueString(row.getCell(2)));
					samplingFeatureCode = samplingFeatureName + "#" + resultNum + "#" + moondbNum;
					parentSamplingFeatureCode = samplingFeatureName;
					String spotID = XlsParser.getCellValueString(row.getCell(3));
					if(spotID.endsWith(".0"))
						spotID = spotID.replaceAll(".0", "").trim();
					samplingFeatureName = samplingFeatureName + "#" + spotID;
					samplingFeatureComment = null;				
				}


				samplingFeature.setSamplingFeatureCode(samplingFeatureCode);
				samplingFeature.setSamplingFeatureName(samplingFeatureName);
				samplingFeature.setSamplingFeatureComment(samplingFeatureComment);
				samplingFeature.setParentSamplingFeatrureCode(parentSamplingFeatureCode);
				samplingFeature.setSamplingFeatureTypeNum(sfTypeNum);
				sfList.add(samplingFeature);
			}	
		}
		samplingFeatures.setSamplingFeatures(sfList);
		return samplingFeatures;
	}

}
