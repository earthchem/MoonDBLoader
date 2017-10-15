package org.moondb.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.parser.MethodParser;
import org.moondb.parser.SamplingFeatureParser;
import org.moondb.parser.XlsParser;

public class Echecker {
	private static final String ECHECKERLOG = "echecker.txt";
	
	private static void writeLog(BufferedWriter bw, String content) throws IOException {
		bw.write(content);
		bw.newLine();
	}
	
	private static boolean valueCheckPass (HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String value = null;
		boolean result = true;
		Integer rowStart = RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue();
		Integer endSymbolColNum = RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue();
		Integer rowEnd = XlsParser.getLastRowNum(workbook, sheetName, rowStart, endSymbolColNum);
		Integer colStart = null;

		
		switch (sheetName) {
		case "ROCKS":
			colStart = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
			break;
		case "MINERALS":
			colStart = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
			break;
		case "INCLUSIONS":
			colStart = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
			break;
		}
		
		for (int i=rowStart; i<rowEnd; i++) {
			HSSFRow row = workbook.getSheet(sheetName).getRow(i);
			Integer colEnd = XlsParser.getLastColNum(workbook.getSheet(sheetName), RowCellPos.VARIABLE_BEGIN_ROW_NUM.getValue());
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
						String content = "The value " + value + " at Row: " + (i+1) + " Col: " + (j+1) + " in the sheet " + sheetName + " is not numeric";
						writeLog(bw, content);
						result = false;
					}
				}
			}
		}
		return result;
	}
	
	private static boolean methodCheckPass (HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		
		HSSFSheet sheet = workbook.getSheet("METHODS");
		int beginRowNum = RowCellPos.METHODS_BEGIN_ROW_NUM.getValue();
		int lastRowNum = XlsParser.getLastRowNum(workbook, "METHODS",beginRowNum,RowCellPos.METHODS_END_SYMBOL_COL_NUM.getValue());
		ArrayList<String> methodCodeList = new ArrayList<String>();
		for (int i = beginRowNum; i < lastRowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			String methodCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1))); 
			methodCodeList.add(methodCode);
		}
		
		String[] methods = XlsParser.getCVCodes(workbook, sheetName, "Method");
		for (int j=0; j< methods.length; j++) {
			if (methods[j] != null) {
				if (!methodCodeList.contains(methods[j])){
		        	content = "Method code <" + methods[j] +"> in the sheet METHODS" + " not found";
		        	writeLog(bw, content);
		        	result = false;
				}				
			} else {
	        	content = "Method code in the sheet " + sheetName + "at column " + j + " is null";
	        	writeLog(bw, content);
	        	result = false;
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
	private static boolean datasetCheckPass (HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		
		HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
		int beginRowNum = RowCellPos.TABLE_TITLES_BEGIN_ROW_NUM.getValue();
		int lastRowNum = XlsParser.getLastRowNum(workbook, "TABLE_TITLES",beginRowNum,RowCellPos.TABLE_TILES_END_SYMBOL_COL_NUM.getValue());
		ArrayList<String> dsCodeList = new ArrayList<String>();
		for (int i = beginRowNum; i < lastRowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			String dsCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0))); 
			dsCodeList.add(dsCode);
		}
		
		Integer rowStart = RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue();
		Integer endSymbolColNum = RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue();
		Integer rowEnd = XlsParser.getLastRowNum(workbook, sheetName, rowStart, endSymbolColNum);
		for (int j=rowStart; j<rowEnd; j++) {
			HSSFRow row = workbook.getSheet(sheetName).getRow(j);
			String ds = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1)));
			if (ds != null) {
				if (!dsCodeList.contains(ds)){
		        	content = "Dataset code <" + ds +"> in the sheet TABLE_TITLES" + " not found";
		        	writeLog(bw, content);
		        	result = false;
				}				
			} else {
	        	content = "dataset code in the sheet " + sheetName + "at row " + j + " is null";
	        	writeLog(bw, content);
	        	result = false;
			}
		}	
		
		return result;
	}

	private static boolean sfNameCheckPass (HSSFWorkbook workbook, String sheetName,BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		
		HSSFSheet sheet = workbook.getSheet("SAMPLES");
		int beginRowNum = RowCellPos.SAMPLES_BEGIN_ROW_NUM.getValue();
		int lastRowNum = XlsParser.getLastRowNum(workbook, "SAMPLES",beginRowNum,RowCellPos.SAMPLES_DATA_END_SYMBOL_COL_NUM.getValue());
		ArrayList<String> sfCodeList = new ArrayList<String>();
		for (int i = beginRowNum; i < lastRowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			String sfCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1))); 
			sfCodeList.add(sfCode);
		}
		
		Integer rowStart = RowCellPos.RMI_DATA_BEGIN_ROW_NUM.getValue();
		Integer endSymbolColNum = RowCellPos.RMI_DATA_END_SYMBOL_COL_NUM.getValue();
		Integer rowEnd = XlsParser.getLastRowNum(workbook, sheetName, rowStart, endSymbolColNum);
		for (int j=rowStart; j<rowEnd; j++) {
			HSSFRow row = workbook.getSheet(sheetName).getRow(j);
			String sf = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(2)));
			if (sf != null) {
				if (!sfCodeList.contains(sf)){
		        	content = "Sample name <" + sf +"> in the sheet SAMPLES" + " not found";
		        	writeLog(bw, content);
		        	result = false;
				}				
			} else {
	        	content = "Sample name in the sheet " + sheetName + "at row " + j + " is null";
	        	writeLog(bw, content);
	        	result = false;
			}
		}	
		
		return result;
	}
	
	private static boolean tableTitleCheckPass (HSSFWorkbook workbook, BufferedWriter bw) throws IOException {
		String content;
		boolean result = true;
		
		HSSFSheet sheet = workbook.getSheet("TABLE_TITLES");
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if(row.getRowNum()>= RowCellPos.TABLE_TITLES_BEGIN_ROW_NUM.getValue()) {
				String tableNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));
				String tableTitle = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1)));
				if (tableNum != null) {
					if (tableNum.equals("-1")) {
						result = true;
						break;
					}
						
					if (tableTitle == null) {
						content = "Table title of number " + tableNum + " must be filled";
						writeLog(bw, content);
						result = false;
					}
				} else {
					if (tableTitle == null) {
						content = "Empty line at the row: " + row.getRowNum();
						writeLog(bw, content);
						result = false;
					}
				}
			}
		}
		return result;
	}


	public static void main(String[] args) throws IOException{
		
		
		File[] files = new File("checking/").listFiles();
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		if (files != null) {
			File logFile = new File("log/", ECHECKERLOG);

	    	fw = new FileWriter(logFile);
	    	
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
		        	} else {
		        		if (!tableTitleCheckPass(workbook, bw)) 
		        			result = false;
		        		
		        	}
		        	
		        	//Sheet SAMPLES checking
		        /*	if(!XlsParser.isRowEndingSymbolExist(workbook, "SAMPLES")) {
		        		content = "Row ending Symbol -1 check(SAMPLES):   not found";
		        		writeLog(bw, content);
		        		
		        		result = false;
		        	}	*/	        	
					SamplingFeatures samplingFeatures = SamplingFeatureParser.parseSamplingFeature(workbook, "SAMPLES", moondbNum, null);
					List<SamplingFeature> sfList = samplingFeatures.getSamplingFeatures();
					for(SamplingFeature sf : sfList) {
						String parentSfCode = sf.getParentSamplingFeatureCode();
						if (!UtilityDao.isSamplingFeatureExist(parentSfCode,1)) {
			        		content = "Parent sampling feature " + parentSfCode + "(SAMPLES):   not found";
			        		writeLog(bw, content);			        		
			        		result = false;
						}							
					}
		        	//Sheet METHODS checking
		        /*	if(!XlsParser.isRowEndingSymbolExist(workbook, "METHODS")) {
		        		content = "Row ending Symbol -1 check(METHODS):   not found";
		        		writeLog(bw, content);
		        		
		        		result = false;
		        	}
		        	*/
		        	Methods methods = MethodParser.parseMethod(workbook);
		        	List<Method> methodList = methods.getMethods();
		        	for (Method method : methodList) {
		        		Integer labNum = method.getMethodLabNum();
		        		if (labNum == null) {
			        		content = "LAB " + labNum +" (METHODS):   not found";
			        		writeLog(bw, content);
			        		result = false;
		        		} else {
			        		if (!UtilityDao.isOrgExist(labNum)) {
				        		content = "LAB " + labNum +" (METHODS):   not found";
				        		writeLog(bw, content);
				        		result = false;
			        		}
		        		}
		        	}
		        	
		        	//Sheet ROCKS checking
	        		if (!datasetCheckPass(workbook,"ROCKS",bw))
	        			result = false;
	        		if (!sfNameCheckPass(workbook,"ROCKS",bw))
	        			result = false;
	        		if (!variableCheckPass(workbook,"ROCKS",bw))
	        			result = false;
	        		if(!methodCheckPass(workbook,"ROCKS",bw))
	        			result = false;	   
	        		//Unit code checking
	        		if (!unitCheckPass(workbook,"ROCKS",bw))
	        			result = false;
	        		//Numeric value checking
	        		if(!valueCheckPass(workbook,"ROCKS", bw))
	        			result = false;
	        		/*
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
		        		content = "Row ending Symbol check(ROCKS):   not found";
		        		writeLog(bw, content);
		        		result = false;
		        	}
		        	*/
		        	//Sheet MINERALS checking
	        		//Variable code checking
	        		if (!datasetCheckPass(workbook,"MINERALS",bw))
	        			result = false;
	        		if (!sfNameCheckPass(workbook,"MINERALS",bw))
	        			result = false;
	        		if(!variableCheckPass(workbook,"MINERALS",bw))
	        			result = false;
	        		if(!methodCheckPass(workbook,"MINERALS",bw))
	        			result = false;	   
	        		//Unit code checking
	        		if(!unitCheckPass(workbook,"MINERALS",bw))
	        			result = false;
	        		
	        		//Numeric value checking
	        		if(!valueCheckPass(workbook,"MINERALS", bw))
	        			result = false;
	        		/*
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
		        	*/
	        		
		        	//Sheet INCLUSIONS checking
	        		//Variable code checking
	        		if (!datasetCheckPass(workbook,"INCLUSIONS",bw))
	        			result = false;
	        		if (!sfNameCheckPass(workbook,"INCLUSIONS",bw))
	        			result = false;
	        		if(!variableCheckPass(workbook,"INCLUSIONS",bw))
	        			result = false;
	        		if(!methodCheckPass(workbook,"INCLUSIONS",bw))
	        			result = false;	        		
	        		//Unit code checking
	        		if (!unitCheckPass(workbook,"INCLUSIONS",bw)) 
	        			result = false;
	        		if(!valueCheckPass(workbook,"INCLUSIONS", bw))
	        			result = false;
	        		/*
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
		        	*/
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
