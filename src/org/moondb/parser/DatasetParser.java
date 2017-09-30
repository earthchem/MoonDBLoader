package org.moondb.parser;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.RowCellPos;

public class DatasetParser {
	public static Datasets parseDataset(HSSFWorkbook workbook, String moondbNum) {
		
		Datasets datasets = new Datasets();
		
		String citationCode = UtilityDao.getCitationCode(moondbNum);
        int citationNum = UtilityDao.getCitationNum(moondbNum);
        
		HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
		
		int beginRowNum = RowCellPos.TABLE_TITLES_BEGIN_ROW_NUM.getValue();
		int lastRowNum = XlsParser.getLastRowNum(workbook, "TABLE_TITLES",beginRowNum,RowCellPos.TABLE_TILES_END_SYMBOL_COL_NUM.getValue());
		
		ArrayList<Dataset> datasetList = new ArrayList<Dataset>();
		for (int i = beginRowNum; i < lastRowNum; i++) {
			Dataset dataset = new Dataset();
			HSSFRow row = sheet.getRow(i);
			String tableCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));
			String datasetCode = citationCode + "#" + tableCode;  //create unique dataset_code by combing citation_code and number of TABLE_IN_REF
			String tableTitle = XlsParser.getCellValueString(row.getCell(1));
			dataset.setTableCode(tableCode);
			dataset.setDatasetTitle(tableTitle.toUpperCase());
			dataset.setDatasetType("Reference table");
			dataset.setDatasetCode(datasetCode.toUpperCase());
			dataset.setCitationNum(citationNum);
			datasetList.add(dataset);
		}

		datasets.setDatasets(datasetList);
		return datasets;
	}
}
