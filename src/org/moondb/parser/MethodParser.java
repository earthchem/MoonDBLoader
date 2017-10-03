package org.moondb.parser;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;


public class MethodParser {
	public static Methods parseMethod(HSSFWorkbook workbook) {
		
		Methods methods = new Methods();
		HSSFSheet sheet = workbook.getSheet("METHODS");
		
		int beginRowNum = RowCellPos.METHODS_BEGIN_ROW_NUM.getValue();
		int lastRowNum = XlsParser.getLastRowNum(workbook, "METHODS",beginRowNum,RowCellPos.METHODS_END_SYMBOL_COL_NUM.getValue());
		
		ArrayList<Method> methodList = new ArrayList<Method>();
		for (int i = beginRowNum; i < lastRowNum; i++) {
			Method method = new Method();			
			HSSFRow row = sheet.getRow(i);
			
			String methodCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(1))); //corresponding to METHOD_CODE in sheet METHODS
			String methodTech = XlsParser.getCellValueString(row.getCell(2)); //corresponding to TECHNIQUE in sheet METHODS
			
			Integer methodLabNum = null;
			try {
				methodLabNum = Integer.parseInt(XlsParser.formatString(XlsParser.getCellValueString(row.getCell(3))));  //corresponding to LAB in sheet METHODS

			} catch (NumberFormatException e) {
				methodLabNum = null;
			}
			String methodComment = XlsParser.getCellValueString(row.getCell(4));   //corresponding to METHOD_COMMENT in sheet METHODS
			Integer methodTypeNum = MoonDBType.METHOD_TYPE_LABANALYSIS.getValue();
			
			method.setMethodCode(methodCode);
			method.setMethodTechnique(methodTech);
			method.setMethodName(methodTech);
			method.setMethodLabNum(methodLabNum);
			method.setMethodComment(methodComment);
			method.setMethodTypeNum(methodTypeNum);
			methodList.add(method);	
		}

		methods.setMethods(methodList);
		return methods;
	}
}
