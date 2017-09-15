package org.moondb.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.parser.MethodParser;
import org.moondb.parser.XlsParser;

public class Echecker {
	private static final String ECHECKERLOG = "log\\echecker.txt";
	
	private static void writeLog(BufferedWriter bw, String content) throws IOException {
		bw.write(content);
		bw.newLine();
	}

	public static void main(String[] args) throws IOException{
		File[] files = new File("data\\").listFiles();
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		if (files != null) {
	    	fw = new FileWriter(ECHECKERLOG);
	    	
	    	bw = new BufferedWriter(fw);
		    for (File file : files) {
		    	String content;
		    	bw.write("*****************************");
		    	bw.newLine();
		    	FileInputStream inputStream = new FileInputStream(file);
		    	String fileName = file.getName();
		    	String moondbNum = fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf("."));
	    		writeLog(bw,moondbNum);

		    	
		    	//Citation checking
		    	if(UtilityDao.isCitationExist(moondbNum)) {
		    		content = "Citation check:   Passed";
		    		writeLog(bw,content);

		    	} else {
		    		content = "Citation check:   Failed";
		    		writeLog(bw,content);
		    	}

		    	System.out.println("File: " + file.getName());
		        try {
		        	HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		        	
		        	//Sheet TABLE_TITLES checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "TABLE_TITLES")) {
		        		content = "Row ending Symbol check(TABLE_TITLES):   Passed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(TABLE_TITLES):   Not required";
		        		writeLog(bw, content);
		        	} else {
		        		content = "Row ending Symbol check(TABLE_TITLES):   Failed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(TABLE_TITLES):   Not required";
		        		writeLog(bw, content);
		        	}
		        	
		        	//Sheet SAMPLES checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "SAMPLES")) {
		        		content = "Row ending Symbol check(SAMPLES):   Passed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(SAMPLES):   Not required";
		        		writeLog(bw, content);
		        	} else {
		        		content = "Row ending Symbol check(SAMPLES):   Failed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(SAMPLES):   Not required";
		        		writeLog(bw, content);
		        	}		        	
		        	
		        	//Sheet METHODS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "METHODS")) {
		        		content = "Row ending Symbol check(METHODS):   Passed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(METHODS):   Not required";
		        		writeLog(bw, content);
		        		
		        		//Method code checking
		        		List<Method> methodList  = MethodParser.parseMethod(workbook).getMethods();
		        		for(Method method:methodList) {
		        			String methodCode = method.getMethodTechnique();
		        			if(UtilityDao.isMethodExist(methodCode)) {
				        		content = "Method <" + methodCode +"> check:	Passed";
				        		writeLog(bw, content);
		        			} else {
				        		content = "Method <" + methodCode +"> check:	Failed";
				        		writeLog(bw, content);		        				
		        			}
		        		}
		        	} else {
		        		content = "Row ending Symbol check(METHODS):   Failed";
		        		writeLog(bw, content);
		        		content = "Col ending Symbol check(METHODS):   Not required";
		        		writeLog(bw, content);
		        	}
		        	
		        	//Sheet ROCKS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "ROCKS")) {
		        		content = "Row ending Symbol check(ROCKS):   Passed";
		        		writeLog(bw, content);
		        		if(XlsParser.isDataExist(workbook, "ROCKS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "ROCKS")) {
				        		content = "Col ending Symbol check(ROCKS):   Passed";
				        		writeLog(bw, content);	
				        		
				        		//Variable code checking
				        		String[] variables = XlsParser.getCVCodes(workbook, "ROCKS", "Variable");
				        		for(int j=0; j<variables.length; j++) {
				        			if(UtilityDao.isVariableExist(variables[j])) {
						        		content = "Variable <" + variables[j] +"> check:	Passed";
						        		writeLog(bw, content);
				        			} else {
						        		content = "Variable <" + variables[j] +"> check:	Failed";
						        		writeLog(bw, content);				        				
				        			}
				        		}
				        		
				        		//Unit code checking
				        		String[] units = XlsParser.getCVCodes(workbook, "ROCKS", "Unit");
				        		for(int j=0; j<units.length; j++) {
				        			if(UtilityDao.isUnitExist(units[j])) {
						        		content = "Unit <" + units[j] +"> check:	Passed";
						        		writeLog(bw, content);
				        			} else {
						        		content = "Unit <" + units[j] +"> check:	Failed";
						        		writeLog(bw, content);				        				
				        			}
				        		}
		        			} else {
				        		content = "Col ending Symbol check(ROCKS):   Failed";
				        		writeLog(bw, content);		        				
		        			}
		        		} else {
			        		content = "Col ending Symbol check(ROCKS):   Not required (No data)";
			        		writeLog(bw, content);
		        		}
		        	} else {
		        		content = "Row ending Symbol check(ROCKS):   Failed";
		        		writeLog(bw, content);
		        	}
		        	
		        	//Sheet MINERALS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "MINERALS")) {
		        		content = "Row ending Symbol check(MINERALS):   Passed";
		        		writeLog(bw, content);
		        		if(XlsParser.isDataExist(workbook, "MINERALS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "MINERALS")) {
				        		content = "Col ending Symbol check(MINERALS):   Passed";
				        		writeLog(bw, content);		        				
		        			} else {
				        		content = "Col ending Symbol check(MINERALS):   Failed";
				        		writeLog(bw, content);		        				
		        			}
		        		} else {
			        		content = "Col ending Symbol check(MINERALS):   Not required (No data)";
			        		writeLog(bw, content);
		        		}		        		
		        	} else {
		        		content = "Row ending Symbol check(MINERALS):   Failed";
		        		writeLog(bw, content);
		        	}
		        	
		        	//Sheet INCLUSIONS checking
		        	if(XlsParser.isRowEndingSymbolExist(workbook, "INCLUSIONS")) {
		        		content = "Row ending Symbol check(INCLUSIONS):   Passed";
		        		writeLog(bw, content);
		        		if(XlsParser.isDataExist(workbook, "INCLUSIONS")) {
		        			if(XlsParser.isColEndingSymbolExist(workbook, "INCLUSIONS")) {
				        		content = "Col ending Symbol check(INCLUSIONS):   Passed";
				        		writeLog(bw, content);		        				
		        			} else {
				        		content = "Col ending Symbol check(INCLUSIONS):   Failed";
				        		writeLog(bw, content);		        				
		        			}
		        		} else {
			        		content = "Col ending Symbol check(INCLUSIONS):   Not required (No data)";
			        		writeLog(bw, content);
		        		}			        		
		        	} else {
		        		content = "Row ending Symbol check(INCLUSIONS):   Failed";
		        		writeLog(bw, content);
		        	}
		        	
		        	workbook.close();
		        	 
		        }finally {
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
