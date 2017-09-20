package org.moondb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.CitationDao;
import org.moondb.dao.DatasetDao;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Action;
import org.moondb.model.Actions;
import org.moondb.model.ChemistryResult;
import org.moondb.model.Citation;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.RowCellPos;
import org.moondb.model.SampleResult;
import org.moondb.model.SampleResults;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.parser.ActionParser;
import org.moondb.parser.DatasetParser;
import org.moondb.parser.MethodParser;
import org.moondb.parser.SampleDataParser;
import org.moondb.parser.SamplingFeatureParser;
import org.moondb.parser.XlsParser;
public class LoadData {

	
	
	
	public static void main(String[] args) throws IOException{
		File file = new File("data\\MoonDB 00016.xls");
		String fileName = file.getName();
		FileInputStream inputStream = new FileInputStream(file);
    	String moondbNum = fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf("."));
    	
		//Citation citation = new Citation();
		//citation.setRefNum(fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf(".")));
		//CitationDao citationDao = new CitationDao(citation);
		//citation.setCitationCode(citationDao.getCitationCode());
		//citation.setCitationNum(citationDao.getCitationNum());
		//System.out.println(citation.getRefNum());
		try {

			//Get the workbook instance for XLS file 	
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			//ExcelParser excelParser = new ExcelParser();
			//excelParser.setLastColumnNum(workbook, "TABLE_TITLES");
			//System.out.println("last Column is:"+ excelParser.getLastColumnNum());
			//excelParser.setLastRowNum(workbook, "TABLE_TITLES");
			//System.out.println("last Row is:"+ excelParser.getLastRowNum());

			//Get first sheet from the workbook
			/*
			 * Step one: Loading Datasets from sheet TABLE_TITLES
			 * Save data to table dataset and citation_dataset
			 */
			Datasets datasets = DatasetParser.parseDataset(workbook, moondbNum);
			if (datasets != null) {
				UtilityDao.saveDatasets(datasets);
			}
			
			/*
			 * Step two: Loading Methods from sheet METHODS
			 * Save data to table method
			 */
			Methods methods = MethodParser.parseMethod(workbook);
			if (methods != null) {
				UtilityDao.saveMethods(methods);
				System.out.println("Step two finished");
			}
			
			/*
			 * Step three: Loading samping_feature from sheet SAMPLES
			 * Save data to table sampling_feature and related_feature
			 */
			SamplingFeatures samplingFeatures = SamplingFeatureParser.parseSamplingFeature(workbook, "SAMPLES", moondbNum);
			if (samplingFeatures != null) {
				UtilityDao.saveSamplingFeature(samplingFeatures);
				System.out.println("Step three finished");
			}
			
			/*
			 * Step Four: Creating action
			 * Save data to table action
			 */
			
			Actions actions = ActionParser.parseAction(datasets, methods);
			if (actions != null) {
				UtilityDao.saveActions(actions);
				System.out.println("Step four finished");
			}
			
			/*
			 * Step Five: Creating analysis sampling features(sub sampling features)
			 */
			
			if (XlsParser.isDataExist(workbook, "ROCKS")) {
				
				 // loading RockAnalysis sampling features
				 
				SamplingFeatures RockAnalysisSFS = SamplingFeatureParser.parseSamplingFeature(workbook, "ROCKS", moondbNum);
				if (RockAnalysisSFS != null) {
					UtilityDao.saveSamplingFeature(RockAnalysisSFS);
				}
				
			} else if (XlsParser.isDataExist(workbook, "MINERALS")) {
				System.out.println("Step five begin");

				// loading MineralAnalysis sampling features
				SamplingFeatures MineralAnalysisSFS = SamplingFeatureParser.parseSamplingFeature(workbook, "MINERALS", moondbNum);
				if (MineralAnalysisSFS != null) {
					//UtilityDao.saveSamplingFeature(MineralAnalysisSFS);
					
					int[] variableNums = SampleDataParser.getVariableNums(workbook, "MINERALS");
					int[] unitNums = SampleDataParser.getUnitNums(workbook, "MINERALS");
					int[] methodNums = SampleDataParser.getMethodNums(workbook, "MINERALS",methods);

					SampleResults sampleResults = SampleDataParser.parseSampleData(workbook, "MINERALS", datasets, moondbNum, variableNums,unitNums,methodNums);
					List<SampleResult> srList = sampleResults.getSampleResults();
					for(SampleResult sr: srList) {
						System.out.println("dataset: " + sr.getDatasetNum());
						System.out.println("sf: " + sr.getSamplingFeatureNum());
						System.out.println("spot: " + sr.getSpotId());

						System.out.println("analysis comment: " + sr.getAnalysisComment());
						System.out.println("avage: " + sr.getCalcAvge());
						System.out.println("mineral: " + sr.getMineral());
						System.out.println("grain: " + sr.getGrain());
						System.out.println("rim/core: " + sr.getRimCore());
						System.out.println("size: " + sr.getMineralSize());
						List<ChemistryResult> crList = sr.getChemistryResults();
						for(ChemistryResult cr: crList) {
							System.out.println("method: " + cr.getMethodNum());
							System.out.println("var: " + cr.getVariableNum());
							System.out.println("unit: " + cr.getUnitNum());
							System.out.println("value: " + cr.getValue());
						}


					}
			
					System.out.println("Step five finished");
				}
			} else if (XlsParser.isDataExist(workbook, "INCLUSIONS")) {
				
				 // loading InclusionAnalysis sampling features
				 
				SamplingFeatures InclusionAnalysisSFS = SamplingFeatureParser.parseSamplingFeature(workbook, "INCLUSIONS", moondbNum);
				if (InclusionAnalysisSFS != null) {
					UtilityDao.saveSamplingFeature(InclusionAnalysisSFS);
				}
			}
			
			List<Method> methodList = methods.getMethods();
			List<Dataset> dss = DatasetParser.parseDataset(workbook, moondbNum).getDatasets();
			
			//SamplingFeatures sfs = SamplingFeatureParser.parseSamplingFeature(workbook);

			//DatasetDao dsDao = new DatasetDao(citation,dss);
			//dsDao.saveDataToDB();
			System.out.println("Status of Rock Data: "+ XlsParser.isDataExist(workbook, "ROCKS"));
			System.out.println("Status of Mineral Data: "+ XlsParser.isDataExist(workbook, "Minerals"));
			System.out.println("Status of Inclusion Data: "+ XlsParser.isDataExist(workbook, "Inclusions"));

			HSSFSheet sheet = workbook.getSheet("ROCKS");
			Integer lastCellNum = XlsParser.getLastCellNum(sheet, RowCellPos.VARIABLE_ROW_B.getValue());
			System.out.println("last cell num is: "+ lastCellNum);
			//Integer variableCode = UtilityDao.getMethodNum("Unknown");
			//System.out.println("variable num is:" + variableCode);
			//Get iterator to all the rows in current sheet
			/*Iterator<Row> rowIterator = sheet.iterator();
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
*/
			workbook.close();
		} finally {
		
			inputStream.close();
		}
	}
}
