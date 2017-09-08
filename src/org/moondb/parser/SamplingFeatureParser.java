package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
				String tableNum = XlsParser.cellToString(row.getCell(0)).trim();
				if (tableNum.equals("-1.0")) {    //data ending at the row
					break;
				}
				String samplingFeatureCode = XlsParser.cellToString(row.getCell(1)).trim();
				Cell c = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				String samplingFeatureComment = null;
				if (c != null)
					samplingFeatureComment = XlsParser.cellToString(c).trim();
				samplingFeature.setSamplingFeatureCode(samplingFeatureCode);
				samplingFeature.setSamplingFeatureComment(samplingFeatureComment);
				samplingFeature.setSamplingFeatureTypeNum(1);   //Specimen
				sfList.add(samplingFeature);
			}	
		}
		samplingFeatures.setSamplingFeatures(sfList);
		return samplingFeatures;
	}

}
