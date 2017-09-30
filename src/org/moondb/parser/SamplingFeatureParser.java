package org.moondb.parser;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;

/*
 * Loading sampling feature info to class SamplingFeature from sheet SAMPLES, ROCKS, MINERALS and INCLUSIONS if data exists
 * Create unique sampling_feature_code for each sample listed in sheet SAMPLES, ROCKS, MINERALS and INCLUSIONS
 * Make sampling_feature_code same as 'SAMPLE NAME' for each sample listed in sheet SAMPLES
 * Make sampling_feature_code follow the format "sampleName#analysisNum#moondbNum" for each sample listed in sheet ROCKS, MINERALS and INCLUSIONS
 * analysisNum is a serial number(starting from 1) for data records across sheet ROCKS, MINERALS and INCLUSIONS
 * baseNum is an offset number used to calculate analysisNum. 0 is baseNum for sheet ROCKS,counts of data records of sheet ROCKS is basNum for sheet MINERALS,
 * sum of counts of data records of sheet ROCKS and MINERALS is baseNum for sheet INCLUSIONS, 
 * because sheet ROCKS always is loaded firstly, then MINERALS, the last is INCLUSIONS.
 * 
 * Update on 9/30/2017
 */

public class SamplingFeatureParser {
	
	
	public static SamplingFeatures parseSamplingFeature (HSSFWorkbook workbook, String sheetName, String moondbNum, Integer baseNum) {
		
		SamplingFeatures samplingFeatures = new SamplingFeatures();
		HSSFSheet sheet = workbook.getSheet(sheetName);
			
		Integer sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_SPECIMEN.getValue(); //Specimen sampling feature
		Integer beginRowNum = null;
		Integer endSymbolColNum = null;
		
		switch(sheetName) {
		case "SAMPLES":
			beginRowNum = RowCellPos.SAMPLES_BEGIN_ROW_NUM.getValue();
			endSymbolColNum = RowCellPos.SAMPLES_DATA_END_SYMBOL_COL_NUM.getValue();
			break;
		case "ROCKS":
		case "MINERALS":
		case "INCLUSIONS":
			beginRowNum = RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue();
			endSymbolColNum = RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue();
			break;
		}

		Integer spotIDCellNum = (sheetName == "MINERALS")? RowCellPos.MINERALS_SPOTID_COL_NUM.getValue() : RowCellPos.INCLUSIONS_SPOTID_COL_NUM.getValue();

		
		
		int lastRowNum = XlsParser.getLastRowNum(workbook, sheetName,beginRowNum,endSymbolColNum);

		ArrayList<SamplingFeature> sfList = new ArrayList<SamplingFeature>();
		
		for (int i = beginRowNum; i < lastRowNum; i++) {
			SamplingFeature samplingFeature = new SamplingFeature();
			HSSFRow row = sheet.getRow(i);
			
			String samplingFeatureCode = null;
			String samplingFeatureName = null;
			String samplingFeatureComment = null;
			String parentSamplingFeatureCode = null;
			
			
			if(sheetName == "SAMPLES") {
				samplingFeatureName = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1))); //corresponding to SAMPLE NAME in sheet SAMPLES
				
				//keep samplingFeatureCode be same as samplingFeatureName for specimen sampling feature
				samplingFeatureCode = samplingFeatureName;
			
				parentSamplingFeatureCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(2)));  //corresponding STATION_NAME in sheet SAMPLES
				
				//no parent sampling feature if the sampling feature is root sampling feature 
				if(samplingFeatureCode.equals(parentSamplingFeatureCode)) {
					parentSamplingFeatureCode = null;
				} 
					
				samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));	
				
			} else {
				int analysisNum = i - beginRowNum + 1 + baseNum;
				samplingFeatureName = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(2))); //corresponding to SAMPLE NAME in sheet ROCKS, MINERALS and INCLUSIONS
				samplingFeatureCode = samplingFeatureName + "#" +  analysisNum + "#" + moondbNum;

				parentSamplingFeatureCode = samplingFeatureName;
				if (sheetName != "INCLUSIONS") {
					samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));	//corresponding to ANALYSIS COMMENT in sheet ROCKS and MINERALS 
				}
				
				if (sheetName != "ROCKS") {
					String spotID = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(spotIDCellNum)));
					if(spotID != null) {
						samplingFeatureName = samplingFeatureName + "#" + spotID;						
					}						
				}
			} 


			samplingFeature.setSamplingFeatureCode(samplingFeatureCode);
			samplingFeature.setSamplingFeatureName(samplingFeatureName);
			samplingFeature.setSamplingFeatureComment(samplingFeatureComment);
			samplingFeature.setParentSamplingFeatrureCode(parentSamplingFeatureCode);
			samplingFeature.setSamplingFeatureTypeNum(sfTypeNum);
			sfList.add(samplingFeature);
		}
		
		samplingFeatures.setSamplingFeatures(sfList);
		return samplingFeatures;
	}

}
