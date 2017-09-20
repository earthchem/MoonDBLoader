package org.moondb.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;
import org.moondb.model.RowCellPos;
import org.moondb.model.SampleResult;
import org.moondb.model.SampleResults;
import org.moondb.model.ChemistryResult;

public class SampleDataParser {
	
	private static int getBeginCellNum (HSSFSheet sheet) {
		Integer beginCellNum = null;
		switch (sheet.getSheetName()){
		case "ROCKS":
			beginCellNum = RowCellPos.ROCKS_VMUCD_CELL_B.getValue();
			break;
		case "MINERALS":
			beginCellNum = RowCellPos.MINERALS_VMUCD_CELL_B.getValue();
			break;
		case "INCLUSIONS":
			beginCellNum = RowCellPos.INCLUSIONS_VMUCD_CELL_B.getValue();
			break;
		}
		
		return beginCellNum;
	}
	
	public static int[] getVariableNums (HSSFWorkbook workbook, String sheetName) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
	
		Integer beginCellNum = getBeginCellNum(sheet);
		
		/*
		 * The ending cell number of data
		 */
		int lastCellNum = XlsParser.getLastCellNum(sheet,RowCellPos.VARIABLE_ROW_B.getValue());
		/*
		 * The size of array for variableNums,methodNums,unitNums and chemicalData
		 */
        int dataEntrySize = lastCellNum-beginCellNum;
        /*
         * Initializing array of variableNums
         */
		int[] variableNums = new int[dataEntrySize];
		
		Row row = sheet.getRow(RowCellPos.VARIABLE_ROW_B.getValue());
		/*
		 * Set variableNums
		 */
		for(int i=beginCellNum; i<lastCellNum; i++) {
			String varCode = XlsParser.getCellValueString(row.getCell(i));
			if(varCode.contains("[")) {
				varCode = varCode.substring(0, varCode.indexOf('[')); //remove type notation like [TE]
			}
			int varTypeNum = MoonDBType.VARIABLE_TYPE_MV.getValue();
			variableNums[i-beginCellNum] = UtilityDao.getVariableNum(varCode, varTypeNum);
		}
		
		return variableNums;
	}
	
	public static int[] getUnitNums (HSSFWorkbook workbook, String sheetName) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		
		Integer beginCellNum = getBeginCellNum(sheet);
		
		/*
		 * The ending cell number of data
		 */
		int lastCellNum = XlsParser.getLastCellNum(sheet,RowCellPos.VARIABLE_ROW_B.getValue());
		/*
		 * The size of array for variableNums,methodNums,unitNums and chemicalData
		 */
        int dataEntrySize = lastCellNum-beginCellNum;
        /*
         * Initializing array of unitNums
         */
		int[] unitNums = new int[dataEntrySize];
		
		Row row = sheet.getRow(RowCellPos.UNIT_ROW_B.getValue());
		/*
		 * Set variableNums
		 */
		for(int i=beginCellNum; i<lastCellNum; i++) {
			String unitCode = XlsParser.getCellValueString(row.getCell(i));
			unitNums[i-beginCellNum] = UtilityDao.getUnitNum(unitCode);
		}
		
		return unitNums;
	}
	
	public static int[] getMethodNums (HSSFWorkbook workbook, String sheetName, Methods methods) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		
		Integer beginCellNum = getBeginCellNum(sheet);
		
		/*
		 * The ending cell number of data
		 */
		int lastCellNum = XlsParser.getLastCellNum(sheet,RowCellPos.VARIABLE_ROW_B.getValue());
		/*
		 * The size of array for variableNums,methodNums,unitNums and chemicalData
		 */
        int dataEntrySize = lastCellNum-beginCellNum;
        /*
         * Initializing array of methodNums
         */
		int[] methodNums = new int[dataEntrySize];
		
		Row row = sheet.getRow(RowCellPos.METHOD_CODE_ROW_B.getValue());
		
		List<Method> methodList = methods.getMethods();
		for(int i=beginCellNum; i<lastCellNum; i++) {
			String methodCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(i)));
			
			for(Method method: methodList) {

				if (methodCode.equals(method.getMethodCode())) {
					methodNums[i-beginCellNum] = UtilityDao.getMethodNum(method.getMethodTechnique(),method.getMethodLabNum(),method.getMethodComment());
				}
			}					
		}
		
		return methodNums;
	}
	
	public static SampleResults parseSampleData (HSSFWorkbook workbook, String sheetName, Datasets datasets, String moonDBNum, int[] variableNums, int[] unitNums, int[] methodNums) {
		SampleResults sampleResults = new SampleResults();
		

		HSSFSheet sheet = workbook.getSheet(sheetName);
		
		Integer beginCellNum = getBeginCellNum(sheet);
		Integer spotIDCellNum = null;
		Integer replicateCellNum = null;
		Integer avgeCellNum = null;
		Integer sfTypeNum = null;
		String sfTypeSign = null;
		
		switch (sheetName){
		case "ROCKS":
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_ROCKANALYSIS.getValue();  //RockAnalysis sampling feature
			sfTypeSign = "R";
			replicateCellNum = RowCellPos.ROCKS_REPLICATE_COL_NUM.getValue();
			avgeCellNum = RowCellPos.ROCKS_AVGE_COL_NUM.getValue();
			break;
		case "MINERALS":
			spotIDCellNum = RowCellPos.MINERALS_SPOTID_COL_NUM.getValue();
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_MINERALANALYSIS.getValue();  //MineralAnalysis sampling feature
			sfTypeSign = "M";
			replicateCellNum = RowCellPos.MINERALS_REPLICATE_COL_NUM.getValue();
			avgeCellNum = RowCellPos.MINERALS_AVGE_COL_NUM.getValue();
			break;
		case "INCLUSIONS":
			spotIDCellNum = RowCellPos.INCLUSIONS_SPOTID_COL_NUM.getValue();
			sfTypeSign = "I";
			sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_INCLUSIONANALYSIS.getValue();  //InclusionAnalysis sampling feature
			replicateCellNum = RowCellPos.INCLUSIONS_REPLICATE_COL_NUM.getValue();
			avgeCellNum = RowCellPos.INCLUSIONS_AVGE_COL_NUM.getValue();
			break;
	}
		/*
		 * The ending cell number of data
		 */
		int lastCellNum = XlsParser.getLastCellNum(sheet,RowCellPos.VARIABLE_ROW_B.getValue());
		/*
		 * The size of array for variableNums,methodNums,unitNums and chemicalData
		 */
        int dataEntrySize = lastCellNum-beginCellNum;
        
        
        

        /*
         * Initializing array of chemicalData
         */
		double[] chemicalData = new double[dataEntrySize];
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		ArrayList<SampleResult> sampleResultList = new ArrayList<SampleResult>();
	
		while (rowIterator.hasNext()) {
			
			Row row = rowIterator.next();
		    if (row.getRowNum() >= RowCellPos.DATA_ROW_B.getValue()) {  //reading analysis results
		    	SampleResult sampleResult = new SampleResult();
				String endCellNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(RowCellPos.RMI_DATA_END_CELL_NUM.getValue())));   //corresponding to SAMPLE_ID in sheet SAMPLES, ANALYSIS NO. in sheet ROCKS,MINERIALS and INCLUSIONS 
				if (endCellNum.equals("-1")) {    //data ending at the row
					break;
				}

				/*
				 * Set datasetNum
				 */
				String tabInRef = XlsParser.getCellValueString(row.getCell(1));    //TAB_IN_REF in sheet ROCKS, MINERALS and INCLUSIONS
				Integer datasetNum = null;
				List<Dataset> dsList = datasets.getDatasets();
				for(Dataset ds : dsList) {

					if(tabInRef.equals(ds.getTableNum())) {
						datasetNum = UtilityDao.getDatasetNum(ds.getDatasetCode());
						
						break;
					}
				}
				sampleResult.setDatasetNum(datasetNum);
				/*
				 * Set samplingFeatureNum
				 */
				String resultNum = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));   //corresponding to SAMPLE_ID in sheet SAMPLES, ANALYSIS NO. in sheet ROCKS,MINERIALS and INCLUSIONS 
				String sampleName = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(2)));  // SAMPLE NAME in sheet ROCKS
				String sfCode = sampleName + "#" + sfTypeSign + resultNum + "#" + moonDBNum;
				
				Integer samplingFeatureNum = UtilityDao.getSamplingFeatureNum(sfCode,sfTypeNum);;

				sampleResult.setSamplingFeatureNum(samplingFeatureNum);
				/*
				 * Set analysisComment
				 */
				String analysisComment = null;
				if(sheetName != "INCLUSIONS") {
					analysisComment = XlsParser.getCellValueString(row.getCell(3)); //ANALYSIS COMMENT in sheet ROCKS
				}
				sampleResult.setAnalysisComment(analysisComment);
				
				/*
				 * Set numberOfReplicates
				 */
				String numberOfReplicates = XlsParser.getCellValueString(row.getCell(replicateCellNum)); 
				sampleResult.setReplicatesCount(numberOfReplicates);
				
				/*
				 * set calcAvge
				 */
				String calcAvge = XlsParser.getCellValueString(row.getCell(avgeCellNum)); 
				sampleResult.setCalcAvge(calcAvge);
				
				if (sheetName == "ROCKS") {
					String material = XlsParser.getCellValueString(row.getCell(6)); //MATERIAL in sheet ROCKS
					sampleResult.setMaterial(material);
				}
				
				if (sheetName == "MINERALS") {
					String mineral = XlsParser.getCellValueString(row.getCell(7));
					sampleResult.setMineral(mineral);
					
					String grain = XlsParser.getCellValueString(row.getCell(8));
					sampleResult.setGrain(grain);
					
					String rimCore = XlsParser.getCellValueString(row.getCell(9));
					sampleResult.setRimCore(rimCore);
					
					String minerialSize = XlsParser.getCellValueString(row.getCell(10));
					sampleResult.setMineralSize(minerialSize);					
				}
				
				if (sheetName == "INCLUSIONS") {
					String inclusionType = XlsParser.getCellValueString(row.getCell(6));
					sampleResult.setInclusionType(inclusionType);
					
					String inclusionMineral = XlsParser.getCellValueString(row.getCell(7));
					sampleResult.setInclusionMineral(inclusionMineral);
					
					String hostMineral = XlsParser.getCellValueString(row.getCell(8));
					sampleResult.setHostMineral(hostMineral);
					
					String hostRock = XlsParser.getCellValueString(row.getCell(9));
					sampleResult.setHostRock(hostRock);
					
					String inclusionSize = XlsParser.getCellValueString(row.getCell(10));
					sampleResult.setInclusionSize(inclusionSize);
					
					String rimCore = XlsParser.getCellValueString(row.getCell(11));
					sampleResult.setRimCore(rimCore);
					
					String heated = XlsParser.getCellValueString(row.getCell(12));
					sampleResult.setHeated(heated);
					
					String temperature = XlsParser.getCellValueString(row.getCell(13));
					sampleResult.setTemperature(temperature);
				}
				/*
				 * Reading chemical data
				 */

				
				ArrayList<ChemistryResult> chemistryResultList = new ArrayList<ChemistryResult>();
				for(int i=beginCellNum; i<lastCellNum; i++) {
					ChemistryResult chemistryResult = new ChemistryResult();
					String value = XlsParser.getCellValueString(row.getCell(i));
					//System.out.println("value is:" + value);
					if(value != null) {     //skip null value
						if(value.contains(",")) {
							value = value.substring(0, value.indexOf(',')).trim();							
						}
						if(value.contains(".")) {
							if (value.indexOf('.') == 0) {
								value = "0" + value;
							}
						}
						chemistryResult.setMethodNum(methodNums[i-beginCellNum]);
						chemistryResult.setUnitNum(unitNums[i-beginCellNum]);
						chemistryResult.setVariableNum(variableNums[i-beginCellNum]);
						chemistryResult.setVaule(Double.parseDouble(value));
						chemistryResultList.add(chemistryResult);
					}
				}
				sampleResult.setChemistryResults(chemistryResultList);
		    	sampleResultList.add(sampleResult);
			}

		}
		sampleResults.setSampleResults(sampleResultList);
		return sampleResults;				
	}
}
