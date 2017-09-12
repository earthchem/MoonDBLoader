package org.moondb.parser;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;

public class XlsParser {
	//Return the last cell number of the specific row in the sheet by the ending character
	public static Integer getLastCellNum(HSSFSheet sheet, Integer rowNum) {
		Integer lastCellNum = null;
		HSSFRow row = sheet.getRow(rowNum);
		
		String value;
		for(int i=row.getFirstCellNum(); i<row.getLastCellNum(); i++) {	
			value = getCellValueString(row.getCell(i));
			if( value != null) {
				if(value.equals("-1.0")) {
					lastCellNum = i;
					break;
				}
			}
		}
		return lastCellNum;
	}
	
	//convert value of cell to String
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
