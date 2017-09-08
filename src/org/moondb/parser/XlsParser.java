package org.moondb.parser;


import org.apache.poi.ss.usermodel.Cell;

public class XlsParser {
	
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
	        default:  
	            throw new RuntimeException("There is no support for this type of cell");                        
	    }

	    return result.toString();
	}
}
