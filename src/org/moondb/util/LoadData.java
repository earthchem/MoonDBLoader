package org.moondb.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
public class LoadData {

	public static void main(String[] args) throws IOException{
		FileInputStream file = new FileInputStream("data\\MoonDB 0623.xls");
		try {

			//Get the workbook instance for XLS file 
			
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheet("SAMPLES");
			//Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				//For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					switch(cell.getCellTypeEnum()) {		
						case BOOLEAN:
							System.out.print(cell.getBooleanCellValue() + "\t\t");
							break;
						case NUMERIC:
							System.out.print(cell.getNumericCellValue() + "\t\t");
							break;
						case STRING:
							System.out.print(cell.getStringCellValue() + "\t\t");
							break;
					default:
						break;
					}
				}
				System.out.println("");
			}
			String test = "abc2005a";
			if(Character.isDigit(test.charAt(test.length()-1))) {
				System.out.println("test");
			}else {
				System.out.println(test.substring(0, test.length()-1));
			}
			for (int i=98;i<122;i++) {
				System.out.print((char)i);
			}
			workbook.close();
		} finally {
		
			file.close();
		}
	}
}
