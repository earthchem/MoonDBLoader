package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.model.Method;
import org.moondb.model.Methods;


public class MethodParser {
	public static Methods parseMethod(HSSFWorkbook workbook) {
		
		Methods methods = new Methods();
		HSSFSheet sheet = workbook.getSheet("METHODS");
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<Method> methodList = new ArrayList<Method>();
		while (rowIterator.hasNext()) {
			Method method = new Method();
			Row row = rowIterator.next();
			//data starting from row 2
			if(row.getRowNum()>0) {
				String methodNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));  //corresponding to METHOD_NUM in sheet METHODS
				if (methodNum.equals("-1")) {    //data ending at the row
					break;
				}
				String methodCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1))); //corresponding to METHOD_CODE in sheet METHODS
				String methodTech = XlsParser.getCellValueString(row.getCell(2)); //corresponding to TECHNIQUE in sheet METHODS
				Integer methodLabNum = Integer.parseInt(XlsParser.formatString(XlsParser.getCellValueString(row.getCell(3))));  //corresponding to LAB in sheet METHODS
				String methodComment = XlsParser.getCellValueString(row.getCell(4));   //corresponding to METHOD_COMMENT in sheet METHODS
				
				method.setMethodCode(methodCode);
				method.setMethodTechnique(methodTech);
				method.setMethodLabNum(methodLabNum);
				method.setMethodComment(methodComment);
				methodList.add(method);
			}	
		}
		methods.setMethods(methodList);
		return methods;
	}
}
