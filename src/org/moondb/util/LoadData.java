package org.moondb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.CitationDao;
import org.moondb.dao.DatasetDao;
import org.moondb.model.Citation;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.parser.DatasetParser;
import org.moondb.parser.SamplingFeatureParser;
public class LoadData {

	
	
	
	public static void main(String[] args) throws IOException{
		File file = new File("data\\MoonDB 0623.xls");
		String fileName = file.getName();
		FileInputStream inputStream = new FileInputStream(file);
		Citation citation = new Citation();
		citation.setRefNum(fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf(".")));
		CitationDao citationDao = new CitationDao(citation);
		citation.setCitationCode(citationDao.getCitationCode());
		citation.setCitationNum(citationDao.getCitationNum());
		System.out.println(citation.getRefNum());
		try {

			//Get the workbook instance for XLS file 	
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			//ExcelParser excelParser = new ExcelParser();
			//excelParser.setLastColumnNum(workbook, "TABLE_TITLES");
			//System.out.println("last Column is:"+ excelParser.getLastColumnNum());
			//excelParser.setLastRowNum(workbook, "TABLE_TITLES");
			//System.out.println("last Row is:"+ excelParser.getLastRowNum());

			//Get first sheet from the workbook
			Datasets dss = DatasetParser.parseDataset(workbook);
			//SamplingFeatures sfs = SamplingFeatureParser.parseSamplingFeature(workbook);
			List<SamplingFeature> sfs = SamplingFeatureParser.parseSamplingFeature(workbook).getSamplingFeatures();
			for(SamplingFeature sf: sfs) {
				System.out.println(sf.getSamplingFeatureCode());
				System.out.println(sf.getSamplingFeatureComment());
				System.out.println(sf.getSamplingFeatureTypeNum());
			}
			//DatasetDao dsDao = new DatasetDao(citation,dss);
			//dsDao.saveDataToDB();
			HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
			//Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				//For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					switch(cell.getCellTypeEnum()) {		
						case BOOLEAN:
							System.out.print(cell.getBooleanCellValue() + "\t\t");
							break;
						case NUMERIC:
							System.out.print(cell.getNumericCellValue() + "\t\t");
							break;
						case STRING:
							System.out.print(cell.getStringCellValue() + "\t\t");
							break;
					default:
						break;
					}
				}
				System.out.println("");
			}

			workbook.close();
		} finally {
		
			inputStream.close();
		}
	}
}
