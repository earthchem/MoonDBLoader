package org.moondb.parser;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.moondb.model.RowCellPos;

public class XlsParser {
	/*
	 * Check if Ending symbol "-1" exist in the first column of sheet
	 * if there is "-1" in the cell(?,0), return true
	 * if there is no "-1" in the cell(?,0), return false
	 */
	public static boolean isRowEndingSymbolExist(HSSFWorkbook workbook,String sheetName) {
		boolean result = false;
		int beginRowNum;
		int symbolCellNum;
		switch (sheetName) {
		case "ROCKS":
		case "MINERALS":
		case "INCLUSIONS":
			beginRowNum = RowCellPos.DATA_ROW_B.getValue();
			symbolCellNum = 2;
			break;
		default:
			symbolCellNum = 0;
			beginRowNum = 0;
	}
		HSSFSheet sheet = workbook.getSheet(sheetName);
		for(int i=beginRowNum; i<=sheet.getLastRowNum();i++) {
			HSSFRow row = sheet.getRow(i);
			String value = getCellValueString(row.getCell(symbolCellNum));
			if (value != null) {
				if(value.equals("-1.0") || value.equals("-1")) {
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
		HSSFRow row = sheet.getRow(RowCellPos.VARIABLE_ROW_B.getValue());
		for(int i=beginCellNum; i<=row.getLastCellNum(); i++) {
			String value = getCellValueString(row.getCell(i));
			if (value != null) {
				if(value.equals("-1.0") || value.equals("-1")) {
					result = true;
					break;
				} 
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
		HSSFRow row = sheet.getRow(RowCellPos.DATA_ROW_B.getValue());
		String value = getCellValueString(row.getCell(0));
		if(value.equals("-1.0") || value.equals("-1")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String[] getCVCodes(HSSFWorkbook workbook,String sheetName, String cvName) {
		int rowNum = -1;
		switch (cvName) {
		case "Variable":
			rowNum = RowCellPos.VARIABLE_ROW_B.getValue();
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
		
		int lastCellNum = getLastCellNum(sheet,RowCellPos.VARIABLE_ROW_B.getValue());
        int dataEntrySize = lastCellNum-beginCellNum;

        String[] result = new String[dataEntrySize];
		for(int i=beginCellNum; i<lastCellNum; i++) {
			result[i-beginCellNum] = getCellValueString(row.getCell(i));
		}
		return result;
	}
	

	
	/*
	 * 
	 * Return the last cell number of the specific row in the sheet by the ending character
	 */
	public static Integer getLastCellNum(HSSFSheet sheet, Integer rowNum) {
		Integer lastCellNum = null;
		HSSFRow row = sheet.getRow(rowNum);
		
		String value;
		for(int i=row.getFirstCellNum(); i<=row.getLastCellNum(); i++) {	
			value = getCellValueString(row.getCell(i));
			if( value != null) {
				if(value.equals("-1.0") || value.equals("-1")) {
					lastCellNum = i;
					break;
				}
			}
		}
		return lastCellNum;
	}
	
	/*
	 * if cell type is numeric, the return value will be formatted as numeric, like value 1000 will be returned as 1000.0
	 * ".0" need to be removed
	 */
	public static String formatString (String str) {

		if(str != null && str.endsWith(".0")) {          
			str = str.replaceAll(".0", "").trim();
		} 
		
		return str;
	}
	
	/*
	 *convert value of cell to String
	 */
	public static String getCellValueString(Cell cell) {  
	    String result = null;
	    if (cell != null) {
		    switch (cell.getCellTypeEnum()) {
		    case BOOLEAN:
		    	result = Boolean.toString(cell.getBooleanCellValue()).trim();
	        case NUMERIC: // numeric value in Excel
	            result = Double.toString(cell.getNumericCellValue()).trim();
	            break;
	        case STRING: // String Value in Excel 
	            result = cell.getStringCellValue().trim();
	            break;
	        default:   //return other type as NULL
	        	result = null;
	            //throw new RuntimeException("There is no support for this type of cell");                        
		    }	    	
	    } else {
	    	result = null;
	    }

	    return result;
	}
}
