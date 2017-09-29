package org.moondb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Action;
import org.moondb.model.MoonDBType;
import org.moondb.model.SamplingFeature;
import org.moondb.parser.XlsParser;

public class LoadRootSample {
	public static void main(String[] args) throws IOException{
		
		int beginRowNum = 1;
		int endColNum = 13;
		System.out.println("clean time:" + LocalDateTime.now());
		UtilityDao.cleanMoonDB();
		System.out.println("starting time:" + LocalDateTime.now());
		File file = new File("refdata\\rootspecimen.xls");
		String fileName = file.getName();
		FileInputStream inputStream = new FileInputStream(file);
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheet("SPECIMENS");
			
			Iterator<Row> rowIterator = sheet.iterator();


			while (rowIterator.hasNext()) {

				SamplingFeature samplingFeature = new SamplingFeature();
				Action action = new Action();
				Row row = rowIterator.next();
				if(row.getRowNum()>= beginRowNum) {
					String sfCode = XlsParser.formatString(XlsParser.getCellValueString(row.getCell(0)));
					if (sfCode == null)
						break;
					
					String sfName = sfCode;
					String sfDescription = XlsParser.getCellValueString(row.getCell(1));
					int sfTypeNum = MoonDBType.SAMPLING_FEATURE_TYPE_SPECIMEN.getValue();
					
					samplingFeature.setSamplingFeatureCode(sfCode);
					samplingFeature.setSamplingFeatureName(sfName);
					samplingFeature.setSamplingFeatureComment(sfDescription);
					samplingFeature.setSamplingFeatureTypeNum(sfTypeNum);
					UtilityDao.saveSamplingFeature(samplingFeature);
					int sfNum = UtilityDao.getSamplingFeatureNum(sfCode, 1);
					
					String missionCode = XlsParser.getCellValueString(row.getCell(2));
					Integer methodNum = UtilityDao.getMethodNum(missionCode, 9);
					action.setActionName(missionCode);
					action.setActionTypeNum(11);  //expedition
					action.setMethodNum(methodNum);
					UtilityDao.saveAction(action);
					int actionNum = UtilityDao.getActionNum(missionCode, methodNum, 11);

					UtilityDao.saveFeatureAction(sfNum, actionNum);
					
					//action = null;
					//methodNum = null;
					
					Integer foitNum = null;
					String landMark = XlsParser.getCellValueString(row.getCell(3));
					if (landMark != null) {
						foitNum = UtilityDao.getFeatureOfInterestNum(landMark, 1);
						UtilityDao.saveFeatureOfInterest(sfNum, foitNum);
						
					}
					String lunarStation = XlsParser.getCellValueString(row.getCell(4));
					if (lunarStation != null) {
						foitNum = UtilityDao.getFeatureOfInterestNum(lunarStation, 2);
						UtilityDao.saveFeatureOfInterest(sfNum, foitNum);
					}
					String returnContaioner = XlsParser.getCellValueString(row.getCell(5));
					if (returnContaioner != null) {
						foitNum = UtilityDao.getFeatureOfInterestNum(returnContaioner, 3);
						UtilityDao.saveFeatureOfInterest(sfNum, foitNum);
					}
					
					
					
					String samplingTechnique = XlsParser.getCellValueString(row.getCell(6));
					if (samplingTechnique != null) {
						methodNum = UtilityDao.getMethodNum(samplingTechnique, 4);
						String actionName = sfCode + ": collection: " + samplingTechnique;
						action.setActionName(actionName);
						action.setActionTypeNum(21);  //specimen collection
						action.setMethodNum(methodNum);	
						UtilityDao.saveAction(action);
						actionNum = UtilityDao.getActionNum(actionName, methodNum, 21);

						UtilityDao.saveFeatureAction(sfNum, actionNum);
					}
					
					
					String materialCode = XlsParser.getCellValueString(row.getCell(7));
					if (materialCode != null) {
						Integer materialNum = UtilityDao.getMaterialNum(materialCode);
						UtilityDao.saveSamplingFeatureMaterial(sfNum, materialNum);
					}
					
					String tcName = XlsParser.getCellValueString(row.getCell(8));
					if (tcName != null) {
						Integer tcNum = UtilityDao.getTaxonomicClassifierNum(tcName);
						UtilityDao.saveSamplingFeatureTaxonomicClassifier(sfNum, tcNum);
					}
					
					String weight = XlsParser.getCellValueString(row.getCell(9));
					if(weight != null) {
						double value = Double.parseDouble(weight);
						String actionName = sfCode + ": weight: Unknown";
						action.setActionName(actionName);
						action.setActionTypeNum(20);  //Specimen analysis
						action.setMethodNum(19);	
						UtilityDao.saveAction(action);
						actionNum = UtilityDao.getActionNum(actionName, 19, 20);

						UtilityDao.saveFeatureAction(sfNum, actionNum);
						
						int faNum = UtilityDao.getFeatureActionNum(sfNum, actionNum);
						UtilityDao.saveResult(faNum, 556, 1, "numeric");
						int resultNum = UtilityDao.getResultNum(faNum, 556);
						UtilityDao.saveNumericData(resultNum, value, 56);
					}
					
					String pristinity = XlsParser.getCellValueString(row.getCell(10));
					if(pristinity != null) {
						double value = Double.parseDouble(pristinity);
						String actionName = sfCode + ": pristinity: Unknown";
						action.setActionName(actionName);
						action.setActionTypeNum(17);  //Observation
						action.setMethodNum(19);	
						UtilityDao.saveAction(action);
						actionNum = UtilityDao.getActionNum(actionName, 19, 17);

						UtilityDao.saveFeatureAction(sfNum, actionNum);
						
						int faNum = UtilityDao.getFeatureActionNum(sfNum, actionNum);
						UtilityDao.saveResult(faNum, 557, 4, "numeric");
						int resultNum = UtilityDao.getResultNum(faNum, 557);
						UtilityDao.saveNumericData(resultNum, value, 1);
					}
					
					String prinstinyDate = XlsParser.getCellValueString(row.getCell(11));
					if(prinstinyDate != null) {
						String actionName = sfCode + ": pristinity_date: Unknown";
						action.setActionName(actionName);
						action.setActionTypeNum(13);  //Generic non-observation
						action.setMethodNum(19);	
						UtilityDao.saveAction(action);
						actionNum = UtilityDao.getActionNum(actionName, 19, 13);

						UtilityDao.saveFeatureAction(sfNum, actionNum);
						
						int faNum = UtilityDao.getFeatureActionNum(sfNum, actionNum);
						UtilityDao.saveResult(faNum, 558, 4, "text");
						int resultNum = UtilityDao.getResultNum(faNum, 558);
						UtilityDao.saveTextData(resultNum, prinstinyDate, "Value of pristinity_date");
					}
					
					
				}
			}
			System.out.println("ending time:" + LocalDateTime.now());
			workbook.close();
		} finally {
		
			inputStream.close();
		}
	}
}
