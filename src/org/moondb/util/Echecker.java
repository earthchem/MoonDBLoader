package org.moondb.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.moondb.dao.UtilityDao;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;
import org.moondb.parser.XlsParser;

public class Echecker {
	private static final String ECHECKERLOG = "log\\echecker.txt";
	
	private static void writeLog(BufferedWriter bw, String content) throws IOException {
		bw.write(content);
		bw.newLine();
	}
	
	private static boolean valueCheckPass (HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String value = null;
		boolean result = true;
		Integer rowStart = RowCellPos.DATA_ROW_B.getValue();
		Integer rowEnd = XlsParser.getLastRowNum(workbook, sheetName);
		Integer colStart = null;

		
		switch (sheetName) {
		case "ROCKS":
			colStart = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
		case "MINERALS":
			colStart = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
		case "INCLUSIONS":
			colStart = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
		}
		
		for (int i=rowStart; i<rowEnd; i++) {
			HSSFRow row = workbook.getSheet(sheetName).getRow(i);
			Integer colEnd = XlsParser.getLastCellNum(workbook.getSheet(sheetName), RowCellPos.VARIABLE_ROW_B.getValue());
			for(int j=colStart; j<colEnd; j++) {
				value = XlsParser.getCellValueString(row.getCell(j));
				if(value != null) {     //skip null value
					if(value.contains(",")) {
						value = value.substring(0, value.indexOf(',')).trim();							
					}
					if(value.contains(".")) {
						if (value.indexOf('.') == 0) {
							value = "0" + value;
						}
					}
					try {
						Double.parseDouble(value);
					} catch (NumberFormatException e) {
						String content = "The value " + value + " at Row: " + i+1 + " Col: " + j+1 + " in the sheet " + sheetName + " is not numeric";
						writeLog(bw, content);
						result = false;
					}
				}
			}
		}
		return result;
	}
	
	private static boolean variableCheckPass(HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		String[] variables = XlsParser.getCVCodes(workbook, sheetName, "Variable");
		for(int j=0; j<variables.length; j++) {
			String varCode = null;
			if(variables[j].contains("[")) {
				varCode = variables[j].substring(0, variables[j].indexOf('[')); //remove type notation like [TE]
			} else {
				varCode = variables[j];
			}
			int varTypeNum = MoonDBType.VARIABLE_TYPE_MV.getValue();
			if(!UtilityDao.isVariableExist(varCode,varTypeNum)) {
	        	content = "Variable <" + variables[j] +"> in the sheet " + sheetName + " not found";
	        	writeLog(bw, content);
	        	result = false;
			}				
		}
		return result;
	}
	
	private static boolean unitCheckPass(HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		
		String[] units = XlsParser.getCVCodes(workbook, sheetName, "Unit");
		for(int j=0; j<units.length; j++) {
			if(!UtilityDao.isUnitExist(units[j])) {
        		content = "Unit <" + units[j] +"> in the sheet " + sheetName + " not found";
        		writeLog(bw, content);	
        		result = false;
			}
		}			
		return result;
	}

	public static void main(String[] args) throws IOException{
		
		
		File[] files = new File("data\\").listFiles();
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		if (files != null) {
	    	fw = new FileWriter(ECHECKERLOG);
	    	
	    	bw = new BufferedWriter(fw);
		    for (File file : files) {
				boolean result = true;

		    	String content;
		    	bw.write("*****************************");
		    	bw.newLine();
		    	FileInputStream inputStream = new FileInputStream(file);
		    	String fileName = file.getName();
		    	String moondbNum = fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf("."));
	    		writeLog(bw,moondbNum);

		    	
		    	//Citation checking
		    	if(!UtilityDao.isCitationExist(moondbNum)) {
		    		content = "Citation related to the file " + moondbNum + " not found";
		    		writeLog(bw,content);
		    		result = false;
		    	}

		    	System.out.println("File: " + file.getName());
		        try {
		        	HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		        	
		        	//Sheet TABLE_TITLES checking
		        	if(!XlsParser.isRowEndingSymbolExist(workbook, "TABLE_TITLES")) {
		        		content = "Row ending Symbol -1 check(TABLE_TITLES):   not found";
		        		writeLog(bw, content);
		        		result = false;
		        	}
		        	
		        	//Sheet SAMPLES checking
		        	if(!XlsParser.isRowEndingSymbolExist(workbook, "SAMPLES")) {
		        		content = "Row ending Symbol -1 check(SAMPLES):   not found";
		        		writeLog(bw, content);
		        		
		        		result = false;
		        	}		        	
		        	
		        	//Sheet METHODS checking
		        	if(!XlsParser.isRowEndingSymbolExist(workbook, "METHODS")) {
		        		content = "Row ending Symbol -1 check(METHODS):   not found";
		        		writeLog(bw, content);
		        		
		        		result = false;
		        	}
		        	
		        	//Sheet ROCKS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "ROCKS")) {
		        		if(XlsParser.isDataExist(workbook, "ROCKS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "ROCKS")) {        		
				        		//Variable code checking
				        		if (!variableCheckPass(workbook,"ROCKS",bw))
				        			result = false;
				        		
				        		//Unit code checking
				        		if (!unitCheckPass(workbook,"ROCKS",bw))
				        			result = false;
				        		//Numeric value checking
				        		if(!valueCheckPass(workbook,"ROCKS", bw))
				        			result = false;
		        			} else {
				        		content = "Col ending Symbol check(ROCKS):   not found";
				        		writeLog(bw, content);		        
				        		result = false;
		        			}
		        		}
		        	} else {
		        		content = "Row ending Symbol check(ROCKS):   Failed";
		        		writeLog(bw, content);
		        		result = false;
		        	}
		        	
		        	//Sheet MINERALS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "MINERALS")) {
		        		if(XlsParser.isDataExist(workbook, "MINERALS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "MINERALS")) {   
				        		//Variable code checking
				        		if(!variableCheckPass(workbook,"MINERALS",bw))
				        			result = false;
				        		
				        		//Unit code checking
				        		if(!unitCheckPass(workbook,"MINERALS",bw))
				        			result = false;
				        		
				        		//Numeric value checking
				        		if(!valueCheckPass(workbook,"MINERALS", bw))
				        			result = false;
		        			} else {
				        		content = "Col ending Symbol check(MINERALS):   not found";
				        		writeLog(bw, content);	
				        		result = false;
		        			}
		        		}		        		
		        	} else {
		        		content = "Row ending Symbol check(MINERALS):   not found";
		        		writeLog(bw, content);
		        		result = false;
		        	}
		        	
		        	//Sheet INCLUSIONS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "INCLUSIONS")) {
		        		if(XlsParser.isDataExist(workbook, "INCLUSIONS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "INCLUSIONS")) {
				        		//Variable code checking
				        		if(!variableCheckPass(workbook,"INCLUSIONS",bw))
				        			result = false;
				        		
				        		//Unit code checking
				        		if (!unitCheckPass(workbook,"INCLUSIONS",bw)) 
				        			result = false;
				        		if(!valueCheckPass(workbook,"INCLUSIONS", bw))
				        			result = false;
				        		
		        			} else {
				        		content = "Col ending Symbol check(INCLUSIONS):   not found";
				        		writeLog(bw, content);	
				        		result = false;
		        			}
		        		} 			        		
		        	} else {
		        		content = "Row ending Symbol check(INCLUSIONS):   not found";
		        		writeLog(bw, content);
		        		result = false;
		        	}
		        	
		        	workbook.close();
		        	 
		        }finally {
		        	 if (result) {
		        		 content = "Pass!";
		        		 writeLog(bw, content);
		        	 } else {
		        		 content = "Not Pass!";
		        		 writeLog(bw, content);
		        	 }
		        	 inputStream.close();

		        }
		    }
		    
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();
		}
	}
}
