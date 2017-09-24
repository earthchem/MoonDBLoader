package org.moondb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.moondb.dao.UtilityDao;

import org.moondb.model.Actions;
import org.moondb.model.ChemistryResult;

import org.moondb.model.Datasets;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;
import org.moondb.model.SampleResult;
import org.moondb.model.SampleResults;
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
					UtilityDao.saveSamplingFeature(MineralAnalysisSFS);
					
					int[] variableNums = SampleDataParser.getVariableNums(workbook, "MINERALS");
					int[] unitNums = SampleDataParser.getUnitNums(workbook, "MINERALS");
					int[] methodNums = SampleDataParser.getMethodNums(workbook, "MINERALS",methods);

					int citationNum = UtilityDao.getCitationNum(moondbNum);
					SampleResults sampleResults = SampleDataParser.parseSampleData(workbook, "MINERALS", datasets, moondbNum, variableNums,unitNums,methodNums);
					List<SampleResult> srList = sampleResults.getSampleResults();
					int annotationNum = -1;
					for(SampleResult sr: srList) {
						if (sr.getSpotId() != null) {
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_SPOT_ID.getValue(), sr.getSpotId(), citationNum);
							annotationNum = UtilityDao.getAnnotationNum(MoonDBType.ANNOTATION_TYPE_SPOT_ID.getValue(), sr.getSpotId(), citationNum);
							UtilityDao.saveSamplingFeatureAnnotation(sr.getSamplingFeatureNum(), annotationNum);
						}
						if (sr.getCalcAvge() != null) {
							
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_CALCULATED_AVERAGE.getValue(), sr.getCalcAvge(), citationNum);

						}
						if (sr.getMineral() != null)
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_MINERAL.getValue(),sr.getMineral(), citationNum);
						if (sr.getGrain() != null)
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_GRAIN.getValue(),sr.getGrain(), citationNum);
						if (sr.getRimCore() != null)
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_RIM_OR_CORE.getValue(),sr.getRimCore(), citationNum);
						if (sr.getMineralSize() != null)
							UtilityDao.saveAnnotation(MoonDBType.ANNOTATION_TYPE_MINERAL_SIZE.getValue(),sr.getMineralSize(), citationNum);

							

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
							int actionNum = UtilityDao.getActionNum(sr.getDatasetNum(), cr.getMethodNum(), MoonDBType.ACTION_TYPE_SPECIMEN_ANALYSIS.getValue());
							UtilityDao.saveFeatureAction(sr.getSamplingFeatureNum(), actionNum);
							int featureActionNum = UtilityDao.getFeatureActionNum(sr.getSamplingFeatureNum(), actionNum);
							UtilityDao.saveResult(featureActionNum, cr.getVariableNum());
							int resultNum = UtilityDao.getResultNum(featureActionNum, cr.getVariableNum());
							UtilityDao.saveNumericData(resultNum, cr.getValue(), cr.getUnitNum());
							System.out.println("action: " + actionNum);
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
			

			workbook.close();
		} finally {
		
			inputStream.close();
		}
	}
}
