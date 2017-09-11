package org.moondb.parser;


import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;

public class XlsParser {
	//Return the last cell number of the specific row in the sheet by the ending character
	public static Integer getLastCellNum(HSSFSheet sheet, Integer rowNum) {
		Integer lastCellNum = null;
		
		Iterator<Cell> cellIterator = sheet.getRow(rowNum).cellIterator();
		while(cellIterator.hasNext()) {	
			Cell cell = cellIterator.next();
			if(cellToString(cell).trim().equals("-1.0")) {
				lastCellNum = cell.getColumnIndex();
				break;
			}
		}
		return lastCellNum;
	}
	
	//convert value of cell to String
	public static String cellToString(Cell cell) {  
	    Object result = null;
	    switch (cell.getCellTypeEnum()) {

	        case NUMERIC: // numeric value in Excel
	            result = cell.getNumericCellValue();
	            break;
	        case STRING: // String Value in Excel 
	            result = cell.getStringCellValue();
	            break;
	        default:  
	            throw new RuntimeException("There is no support for this type of cell");                        
	    }

	    return result.toString();
	}
}
