package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;

public class DatasetParser {
	public static Datasets parseDataset(HSSFWorkbook workbook, String moondbNum) {
		
		Datasets datasets = new Datasets();
		
		String citationCode = UtilityDao.getCitationCode(moondbNum);
        int citationNum = UtilityDao.getCitationNum(moondbNum);
        
		HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<Dataset> datasetList = new ArrayList<Dataset>();
		while (rowIterator.hasNext()) {
			Dataset dataset = new Dataset();
			Row row = rowIterator.next();
			//data starting from row 2
			if(row.getRowNum()>0) {
				String tableNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));
				if (tableNum.equals("-1")) {    //data ending at the row
					break;
				}
				String datasetCode = citationCode + "#" + tableNum;  //create unique dataset_code by combing citation_code and number of TABLE_IN_REF
				String tableTitle = XlsParser.getCellValueString(row.getCell(1));
				dataset.setTableNum(tableNum);
				dataset.setDatasetTitle(tableTitle.toUpperCase());
				dataset.setDatasetType("Reference table");
				dataset.setDatasetCode(datasetCode.toUpperCase());
				dataset.setCitationNum(citationNum);
				datasetList.add(dataset);
			}	
		}
		datasets.setDatasets(datasetList);
		return datasets;
	}
}
