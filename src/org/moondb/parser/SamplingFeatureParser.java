package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;

/*
 * Loading sampling feature info to class SamplingFeature from sheet SAMPLES, ROCKS, MINERALS and INCLUSIONS if data exists
 * Sheet "SAMPLES" holds specimen sampling features, they are parent sampling features of other analyzing sampling features
 * RockAnalysis sampling features are extracted from Sample Data part of the Sheet "ROCKS"
 * MineralAnalysis sampling features are extracted from Sample Data part of the sheet "MINERALS"
 * InclusionAnalysis sampling features are extracted from Sample Data part of the sheet "INCLUSIONS"
 * 
 * Update on 9/18/2017
 */

public class SamplingFeatureParser {
	
	
	public static SamplingFeatures parseSamplingFeature (HSSFWorkbook workbook, String sheetName, String moondbNum) {
		
		SamplingFeatures samplingFeatures = new SamplingFeatures();
		HSSFSheet sheet = workbook.getSheet(sheetName);
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<SamplingFeature> sfList = new ArrayList<SamplingFeature>();
		
		int sfTypeNum = -1;
		int beginRowNum = -1;
		int spotIDCellNum = -1;
		int endSignCellNum = -1;
		String sfTypeSign = null;
		
		switch(sheetName) {
		case "SAMPLES":
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_SPECIMEN.getValue(); //Specimen sampling feature
			beginRowNum = RowCellPos.SAMPLES_ROW_B.getValue();
			endSignCellNum = RowCellPos.SAMPLES_DATA_END_CELL_NUM.getValue();
			break;
		case "ROCKS":
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_ROCKANALYSIS.getValue();  //RockAnalysis sampling feature
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			sfTypeSign = "R";
			endSignCellNum = RowCellPos.RMI_DATA_END_CELL_NUM.getValue();
			break;
		case "MINERALS":
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_MINERALANALYSIS.getValue();  //MineralAnalysis sampling feature
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			sfTypeSign = "M";
			spotIDCellNum = RowCellPos.MINERALS_SPOTID_COL_NUM.getValue();
			endSignCellNum = RowCellPos.RMI_DATA_END_CELL_NUM.getValue();
			break;
		case "INCLUSIONS":
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			sfTypeSign = "I";
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_INCLUSIONANALYSIS.getValue();  //InclusionAnalysis sampling feature
			spotIDCellNum = RowCellPos.INCLUSIONS_SPOTID_COL_NUM.getValue();
			endSignCellNum = RowCellPos.RMI_DATA_END_CELL_NUM.getValue();
			break;
		}
		while (rowIterator.hasNext()) {
			SamplingFeature samplingFeature = new SamplingFeature();
			Row row = rowIterator.next();

			if(row.getRowNum()>= beginRowNum) {
				String resultNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(endSignCellNum)));   //corresponding to SAMPLE_ID in sheet SAMPLES, ANALYSIS NO. in sheet ROCKS,MINERIALS and INCLUSIONS 
				if (resultNum.equals("-1")) {    //data ending at the row
					break;
				}
				String analysisNum = null;
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
					analysisNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));
					samplingFeatureName = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(2))); //corresponding to SAMPLE NAME in sheet ROCKS, MINERALS and INCLUSIONS
					samplingFeatureCode = samplingFeatureName + "#" + sfTypeSign + analysisNum + "#" + moondbNum;
					parentSamplingFeatureCode = samplingFeatureName;
					if (sheetName != "INCLUSIONS") {
						samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));	//corresponding to ANALYSIS COMMENT in sheet ROCKS and MINERALS 
					}
					
					if (sheetName != "ROCKS") {
						String spotID = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(spotIDCellNum)));
						//String spot_ID = XlsParser.getCellValueString(row.getCell(spotIDCellNum));

						//System.out.println("spot:" + spotID+ ":"+spot_ID);
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
		}
		samplingFeatures.setSamplingFeatures(sfList);
		return samplingFeatures;
	}

}
