package org.moondb.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelParser {
	private Integer lastColumnNum;
	private Integer lastRowNum;
	

	public Integer getLastColumnNum() {
		return lastColumnNum;
	}
	
	public void setLastColumnNum(HSSFWorkbook workbook, String sheetName) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(0);
		
		for(int i =0;i<row.getLastCellNum();i++) {
			HSSFCell cell = row.getCell(i);
			String value = cellToString(cell);
			System.out.println("value is:" + value);
			if (value == "-1" || value == "-1.0") {
				this.lastColumnNum = i;
				break;
			}			
		}			
	}
	
	public Integer getLastRowNum() {
		return lastRowNum;
	}
	
	public void setLastRowNum(HSSFWorkbook workbook, String sheetName) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
	
		for(int i=0;i<sheet.getLastRowNum();i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(0);
			String value = cellToString(cell);
			if (value == "-1" || value == "-1.0") {
				this.lastRowNum = i;
				break;
			}	
		}
			
	}
	//convert value of cell to String
	public static String cellToString(Cell cell) {  
	    Object result;
	    switch (cell.getCellTypeEnum()) {

	        case NUMERIC: // numeric value in Excel
	            result = cell.getNumericCellValue();
	            break;
	        case STRING: // String Value in Excel 
	            result = cell.getStringCellValue();
	            break;
	        case _NONE:
	        	result = cell.getStringCellValue();
	        	break;
	        default:  
	            throw new RuntimeException("There is no support for this type of cell");                        
	    }

	    return result.toString();
	}
}
