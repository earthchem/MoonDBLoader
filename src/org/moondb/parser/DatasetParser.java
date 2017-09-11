package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;

public class DatasetParser {
	public static Datasets parseDataset(HSSFWorkbook workbook) {
		
		Datasets datasets = new Datasets();
		HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<Dataset> datasetList = new ArrayList<Dataset>();
		while (rowIterator.hasNext()) {
			Dataset dataset = new Dataset();
			Row row = rowIterator.next();
			//data starting from row 2
			if(row.getRowNum()>0) {
				String tableNum = XlsParser.cellToString(row.getCell(0)).trim();
				if (tableNum.equals("-1.0")) {    //data ending at the row
					break;
				}
				String tableTitle = XlsParser.cellToString(row.getCell(1)).trim();
				dataset.setTableNum(tableNum);
				dataset.setDatasetTitle(tableTitle);
				dataset.setDatasetType("Reference table");
				datasetList.add(dataset);
			}	
		}
		datasets.setDatasets(datasetList);
		return datasets;
	}
}
