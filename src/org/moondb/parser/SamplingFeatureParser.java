package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;

public class SamplingFeatureParser {
	public static SamplingFeatures parseSamplingFeature (HSSFWorkbook workbook) {
		
		SamplingFeatures samplingFeatures = new SamplingFeatures();
		HSSFSheet sheet = workbook.getSheet("SAMPLES");
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<SamplingFeature> sfList = new ArrayList<SamplingFeature>();
		while (rowIterator.hasNext()) {
			SamplingFeature samplingFeature = new SamplingFeature();
			Row row = rowIterator.next();
			//data starting from row 3
			if(row.getRowNum()>1) {
				String sampleID = XlsParser.getCellValueString(row.getCell(0));
				if (sampleID.equals("-1.0") || sampleID.equals("-1")) {    //data ending at the row
					break;
				}
				String samplingFeatureCode = XlsParser.getCellValueString(row.getCell(1));
				String parentSamplingFeatureCode = XlsParser.getCellValueString(row.getCell(2));
				String samplingFeatureComment = XlsParser.getCellValueString(row.getCell(3));

				samplingFeature.setSamplingFeatureCode(samplingFeatureCode);
				samplingFeature.setSamplingFeatureComment(samplingFeatureComment);
				if(parentSamplingFeatureCode != null && parentSamplingFeatureCode.endsWith(".0")) {
					parentSamplingFeatureCode = parentSamplingFeatureCode.replaceAll(".0", ",0");
				}
				samplingFeature.setParentSamplingFeatrureCode(parentSamplingFeatureCode);
				samplingFeature.setSamplingFeatureTypeNum(1);   //Specimen
				sfList.add(samplingFeature);
			}	
		}
		samplingFeatures.setSamplingFeatures(sfList);
		return samplingFeatures;
	}

}
