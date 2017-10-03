package org.moondb.parser;



import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.RowCellPos;

public class XlsParser {
	/*
	 * Check if Ending symbol "-1" exist in the first column of sheet
	 * if there is "-1" in the cell(?,0), return true
	 * if there is no "-1" in the cell(?,0), return false
	 */
	public static boolean isRowEndingSymbolExist(HSSFWorkbook workbook,String sheetName) {
		boolean result = false;
		int beginRowNum = -1;
		int symbolCellNum = -1;
		switch (sheetName) {
		case "ROCKS":
		case "MINERALS":
		case "INCLUSIONS":
			beginRowNum = RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue();
			symbolCellNum = 2;
			break;
		case "SAMPLES":
			beginRowNum = RowCellPos.SAMPLES_BEGIN_ROW_NUM.getValue();
			symbolCellNum = 0;
			break;
		case "TABLE_TITLES":
			beginRowNum = RowCellPos.TABLE_TITLES_BEGIN_ROW_NUM.getValue();
			symbolCellNum = 0;
			break;
		case "METHODS":
			beginRowNum = RowCellPos.METHODS_BEGIN_ROW_NUM.getValue();
			symbolCellNum = 0;
			break;
	}
		HSSFSheet sheet = workbook.getSheet(sheetName);
		for(int i=beginRowNum; i<=sheet.getLastRowNum();i++) {

			HSSFRow row = sheet.getRow(i);
			if(row == null && i > beginRowNum) {
				result = true;
				break;
			}
			String value = XlsParser.formatString(getCellValueString(row.getCell(symbolCellNum)));
			if (value != null) {
				if(value.equals("-1")) {
					result = true;
					break;
				} else {
					
				}
			}
			
		}
		return result;
	}
	
	/*
	 * Check if Ending symbol "-1" exist in the row that contains variable name of the specific sheet ROCKS,MINERALS and INCLUSIONS
	 * if there is "-1" in the cell(VARIABLE_ROW_B,?), return true
	 * if there is no "-1" in the cell(VARIABLE_ROW_B,?), return false
	 */ 
	public static boolean isColEndingSymbolExist(HSSFWorkbook workbook,String sheetName) {
		boolean result = false;
		int beginCellNum;
		switch (sheetName) {
			case "ROCKS":
				beginCellNum = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
				break;
			case "MINERALS":
				beginCellNum = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
				break;
			case "INCLUSIONS":
				beginCellNum = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
				break;
			default:
				beginCellNum = 0;
		}


		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(RowCellPos.VARIABLE_BEGIN_ROW_NUM.getValue());
		for(int i=beginCellNum; i<=row.getLastCellNum(); i++) {
			String value = XlsParser.formatString(getCellValueString(row.getCell(i)));
			if (value != null) {
				if(value.equals("-1")) {
					result = true;
					break;
				} 
			} else {
				result = false;
				break;
			}
		}
		return result;
	}
	
	/*
	 * Check if data exist in specific sheet ROCKS, MINERALS and INCLUSIONS
	 * There is no data existing if the value of cell(8,0) is -1, return false
	 * There is data existing if the value of cell(8,0) is not -1, return true
	 */
	public static boolean isDataExist(HSSFWorkbook workbook,String sheetName) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue());
		String value = XlsParser.formatString(getCellValueString(row.getCell(RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue())));
		if(value != null) {
			if(value.equals("-1")) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static String[] getCVCodes(HSSFWorkbook workbook,String sheetName, String cvName) {
		int rowNum = -1;
		switch (cvName) {
		case "Variable":
			rowNum = RowCellPos.VARIABLE_BEGIN_ROW_NUM.getValue();
			break;
		case "Unit":
			rowNum = RowCellPos.UNIT_ROW_B.getValue();
			break;
		default:
			rowNum = -1;
		}
		
		HSSFSheet sheet = workbook.getSheet(sheetName);
		
		HSSFRow row = sheet.getRow(rowNum);

		int beginCellNum;
		switch (sheetName) {
			case "ROCKS":
				beginCellNum = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
				break;
			case "MINERALS":
				beginCellNum = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
				break;
			case "INCLUSIONS":
				beginCellNum = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
				break;
			default:
				beginCellNum = 0;
		}
		
		int lastCellNum = getLastColNum(sheet,RowCellPos.VARIABLE_BEGIN_ROW_NUM.getValue());
        int dataEntrySize = lastCellNum-beginCellNum;

        String[] result = new String[dataEntrySize];
		for(int i=beginCellNum; i<lastCellNum; i++) {
			result[i-beginCellNum] = getCellValueString(row.getCell(i));
		}
		return result;
	}
	

	
	/*
	 * 
	 * Return the last column number of the specific row in the sheet if the '-1' or null occurred in the assigned cell
	 */
	public static Integer getLastColNum(HSSFSheet sheet, Integer rowNum) {
		Integer lastColNum = null;
		Integer beginColNum = null;
		
		HSSFRow row = sheet.getRow(rowNum);
		
		switch (sheet.getSheetName()) {
		case "ROCKS":
			beginColNum = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
			break;
		case "MINERALS":
			beginColNum = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
			break;
		case "INCLUSIONS":
			beginColNum = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
			break;
		default:
			beginColNum = 0;
		}
		String value;
		for(int i= beginColNum; i<=row.getLastCellNum(); i++) {	
			value = XlsParser.formatString(getCellValueString(row.getCell(i)));
			if( value == null || value.equals("-1")) {
				lastColNum = i;
				break;
			}
		}
		return lastColNum;
	}
	
	/*
	 * 
	 * Return the last row number if the '-1' or null occurred in the assigned cell
	 */
	public static Integer getLastRowNum(HSSFWorkbook workbook, String sheetName, int beginRowNum, int endSymbolColNum) {
		Integer lastRowNum = null;
		
		HSSFSheet sheet = workbook.getSheet(sheetName);
		int totalRows = sheet.getPhysicalNumberOfRows();
		
		for (int i = beginRowNum; i <= totalRows; i ++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				lastRowNum = i;
				break;
			}
			
			String endColNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(endSymbolColNum)));

			if (endColNum == null || endColNum.equals("-1")) {
				lastRowNum = i;
				break;
			}
		}

		return lastRowNum;
		}
/*		
		System.out.println("counts of rows:" + sheet.getPhysicalNumberOfRows());
		Iterator<Row> rowIterator = sheet.iterator();
		int i =0;
		while (rowIterator.hasNext()) {
			i++;
			System.out.println("line num: " + i);
			Row row = rowIterator.next();
			if (row == null) {
			
				System.out.println("Row is null");
			}
			int curRowNum = row.getRowNum();
			System.out.println("cur row: " + curRowNum);
			//if (row.getRowNum() >= RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue()) {
			if (curRowNum >= beginRowNum) {
				//String endColNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue())));   //corresponding to SAMPLE_ID in sheet SAMPLES, ANALYSIS NO. in sheet ROCKS,MINERIALS and INCLUSIONS
				String endColNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(endSymbolColNum)));

				if (endColNum == null || endColNum.equals("-1")) {
					lastRowNum = curRowNum;
					break;
				}
			}
		}

		return lastRowNum;
	}*/
	
	
	/*
	 * if cell type is numeric, the return value will be formatted as numeric, like value 1000 will be returned as 1000.0
	 * ".0" need to be removed
	 */
	public static String formatString (String str) {

		if(str != null && str.endsWith(".0")) {
			
			str = str.substring(0, str.lastIndexOf('.'));
		} 
		
		return str;
	}
	
	/*
	 *convert value of cell to String
	 *null will be returned if the cell type is not NUMERIC or STRING
	 */
	public static String getCellValueString(Cell cell) {  
	    String result = null;
	    if (cell != null) {
		    switch (cell.getCellTypeEnum()) {
	        case NUMERIC: // numeric value in Excel
	        	result = String.valueOf(cell.getNumericCellValue());
	            break;
	        case STRING: // String Value in Excel 
	            result = cell.getStringCellValue().trim();
	            break;
			default:
				break;
		    }	    	
	    } 

	    return result;
	}
}
