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

public class LoadData {
	
	private static void saveAnnotation(int annotationTypeNum, String annotationText, int citationNum, int samplingFeatureNum) {
		UtilityDao.saveAnnotation(annotationTypeNum, annotationText, citationNum);
		int annotationNum = UtilityDao.getAnnotationNum(annotationTypeNum, annotationText, citationNum);
		UtilityDao.saveSamplingFeatureAnnotation(samplingFeatureNum, annotationNum);
	}

	private static Integer saveData(HSSFWorkbook workbook, String sheetName, String moondbNum, Methods methods, Datasets datasets, Integer baseNum) {
		Integer recordCounts = 0;
		SamplingFeatures samplingFeatures = SamplingFeatureParser.parseSamplingFeature(workbook, sheetName, moondbNum, baseNum);
		if (samplingFeatures.getCounts() != 0) {
			UtilityDao.saveSamplingFeatures(samplingFeatures);
			recordCounts = samplingFeatures.getCounts();

			int[] variableNums = SampleDataParser.getVariableNums(workbook, sheetName);
			int[] unitNums = SampleDataParser.getUnitNums(workbook, sheetName);
			int[] methodNums = SampleDataParser.getMethodNums(workbook, sheetName,methods);

			int citationNum = UtilityDao.getCitationNum(moondbNum);
			SampleResults sampleResults = SampleDataParser.parseSampleData(workbook, sheetName, datasets, moondbNum, variableNums,unitNums,methodNums,baseNum);
			List<SampleResult> srList = sampleResults.getSampleResults();
					
			for(SampleResult sr: srList) {
					
				//Loading annotation
				
				if (sr.getMaterial() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_MATERIAL.getValue(),sr.getMaterial(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getSpotId() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_SPOT_ID.getValue(),sr.getSpotId(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getCalcAvge() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_CALCULATED_AVERAGE.getValue(),sr.getCalcAvge(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getMineral() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_MINERAL.getValue(),sr.getMineral(), citationNum, sr.getSamplingFeatureNum());

				if (sr.getGrain() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_GRAIN.getValue(),sr.getGrain(), citationNum, sr.getSamplingFeatureNum());

				if (sr.getRimCore() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_RIM_OR_CORE.getValue(),sr.getRimCore(), citationNum, sr.getSamplingFeatureNum());

				if (sr.getMineralSize() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_MINERAL_SIZE.getValue(),sr.getMineralSize(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getInclusionType() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_INCLUSION_TYPE.getValue(),sr.getInclusionType(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getInclusionSize() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_INCLUSION_SIZE.getValue(),sr.getInclusionSize(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getInclusionMineral() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_INCLUSION_MINERAL.getValue(),sr.getInclusionMineral(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getHostMineral() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_HOST_MINERAL.getValue(),sr.getHostMineral(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getHostRock() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_HOST_ROCK.getValue(),sr.getHostRock(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getHeated() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_HEATED.getValue(),sr.getHeated(), citationNum, sr.getSamplingFeatureNum());
				
				if (sr.getTemperature() != null)
					saveAnnotation(MoonDBType.ANNOTATION_TYPE_TEMPERATURE.getValue(),sr.getTemperature(), citationNum, sr.getSamplingFeatureNum());
			
				
				int datasetNum = sr.getDatasetNum();
				//Loading chemical data

				List<ChemistryResult> crList = sr.getChemistryResults();
				for(ChemistryResult cr: crList) {
					int actionNum = UtilityDao.getActionNum(sr.getDatasetNum(), cr.getMethodNum(), MoonDBType.ACTION_TYPE_SPECIMEN_ANALYSIS.getValue());
					UtilityDao.saveFeatureAction(sr.getSamplingFeatureNum(), actionNum);
					int featureActionNum = UtilityDao.getFeatureActionNum(sr.getSamplingFeatureNum(), actionNum);
					UtilityDao.saveResult(featureActionNum, cr.getVariableNum(),MoonDBType.RESULT_TYPE_MEASUREMENT.getValue(),"numeric");
					int resultNum = UtilityDao.getResultNum(featureActionNum, cr.getVariableNum());
					
					UtilityDao.saveDatasetResult(datasetNum, resultNum);
					UtilityDao.saveNumericData(resultNum, cr.getValue(), cr.getUnitNum());
				}
			}	
		}
		
		return recordCounts;
	}
	
	
	
	public static void main(String[] args) throws IOException{
		
		File[] files = new File("data/").listFiles();
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				FileInputStream inputStream = new FileInputStream(file);
		    	String moondbNum = fileName.substring(fileName.indexOf(' ')+1, fileName.indexOf("."));
		    	
		    	UtilityDao.cleanPaperData(moondbNum);

				try {

					//Get the workbook instance for XLS file 	
					HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

					/*
					 * Step one: Loading Datasets from sheet TABLE_TITLES
					 * Save data to table dataset and citation_dataset
					 */
					Datasets datasets = DatasetParser.parseDataset(workbook, moondbNum);
					if (datasets != null) {
						UtilityDao.saveDatasets(datasets);
						System.out.println("Datasets of " + moondbNum + " loaded.");
					}
					
					/*
					 * Step two: Loading Methods from sheet METHODS
					 * Save data to table method
					 */
					Methods methods = MethodParser.parseMethod(workbook);
					if (methods != null) {
						UtilityDao.saveMethods(methods);
						System.out.println("Methods of " + moondbNum + " loaded.");
					}
					
					/*
					 * Step three: Loading samping_feature from sheet SAMPLES
					 * Save data to table sampling_feature and related_feature
					 */
					SamplingFeatures samplingFeatures = SamplingFeatureParser.parseSamplingFeature(workbook, "SAMPLES", moondbNum, null);
					if (samplingFeatures != null) {
						UtilityDao.saveSamplingFeatures(samplingFeatures);
						System.out.println("Sampling features of " + moondbNum + " loaded.");
					}
					
					/*
					 * Step Four: Creating action
					 * Save data to table action
					 */
					
					Actions actions = ActionParser.parseAction(datasets, methods);
					if (actions != null) {
						UtilityDao.saveActions(actions);
						System.out.println("Actions of " + moondbNum + " loaded.");
					}
					
					/*
					 * Step Five: Creating analysis sampling features(sub sampling features)
					 */
					int countOfRocks = saveData(workbook, "ROCKS", moondbNum, methods, datasets, 0);
					int countOfMinerals = saveData(workbook, "MINERALS", moondbNum, methods, datasets,countOfRocks);
					saveData(workbook, "INCLUSIONS", moondbNum, methods, datasets, countOfRocks + countOfMinerals);
					System.out.println("Data of " + moondbNum + " loaded.");
					
					workbook.close();
				} finally {
				
					inputStream.close();
				}
				inputStream.close();
			}
		}	
	}
}
