package org.moondb.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.moondb.dao.UtilityDao;
import org.moondb.model.Author;
import org.moondb.model.Citation;
import org.moondb.model.Landmark;
import org.moondb.model.SamplingFeature;

public class ExportToJSON {

	
	@SuppressWarnings("unchecked")
	public static void main(String argv[]) {
		try {
			List<Integer> sfNums =  UtilityDao.getSFNums();
			for(Integer sfNum : sfNums) {
				JSONObject objSpecimen = new JSONObject();
				SamplingFeature samplingFeature = UtilityDao.getSamplingFeature(sfNum);
				objSpecimen.put("specimenId", sfNum);
				objSpecimen.put("specimenCode", samplingFeature.getSamplingFeatureCode());
				objSpecimen.put("specimenName", samplingFeature.getSamplingFeatureName());
				objSpecimen.put("weight", samplingFeature.getWeight());
				objSpecimen.put("pristinity", samplingFeature.getPristinity());
				objSpecimen.put("pristinityDate", samplingFeature.getPristinityDate());


				
				JSONArray childSpecimenList = new JSONArray();
				List<String> childSfCodes = samplingFeature.getChildSfCodes();
				if(childSfCodes.size()>0) {
					for(String childSfCode: childSfCodes) {
						childSpecimenList.add(childSfCode);
					}
				} 
				objSpecimen.put("childSpecimens", childSpecimenList);

				objSpecimen.put("parentSpecimen", samplingFeature.getParentSamplingFeatureCode());
				objSpecimen.put("mission", samplingFeature.getMissionName());
					
				JSONObject objLandmark = new JSONObject();
				Landmark landmark = samplingFeature.getLandMark();
				objLandmark.put("landmarkName", landmark.getLandmarkName());
				objLandmark.put("GPNFID", landmark.getGpnfID());
				objLandmark.put("GPNFURL", landmark.getGpnfURL());
				objLandmark.put("latitude", landmark.getLatitude());
				objLandmark.put("longitude", landmark.getLongitude());
					
				objSpecimen.put("landmark", objLandmark);
				objSpecimen.put("lunarStation", samplingFeature.getLunarStation());
				objSpecimen.put("returnContainer", samplingFeature.getReturnContainer());
				objSpecimen.put("samplingTechnique", samplingFeature.getSamplingTechniqueName());
				//objSpecimen.put("materialCode", samplingFeature.getMaterialCode());
				//objSpecimen.put("materialName", samplingFeature.getMaterialName());
				//objSpecimen.put("taxonName", samplingFeature.getTaxonName());
				//objSpecimen.put("parentTaxonName", samplingFeature.getParentTaxonName());
				String specimenType = null;
				if(samplingFeature.getMaterialCode() != null) {
					if(samplingFeature.getTaxonName() != null) {
						if(samplingFeature.getParentTaxonName() != null) {
							specimenType = samplingFeature.getMaterialCode() + "/" + samplingFeature.getParentTaxonName() + "/" + samplingFeature.getTaxonName();
						} else {
							specimenType = samplingFeature.getMaterialCode() + "/"  + samplingFeature.getTaxonName();
						}
					} else {
						specimenType = samplingFeature.getMaterialCode();
					}
				} else {
					specimenType = "Unknown";
				}
				
				objSpecimen.put("specimenType", specimenType);
				objSpecimen.put("specimenDescription", samplingFeature.getSamplingFeatureComment());

				JSONArray resultsArr = new JSONArray();
				List<Integer> relatedSfns = UtilityDao.getRelatedSfNums(sfNum);
				for(Integer relatedSfn: relatedSfns) {
					List<Object[]> results = UtilityDao.getAnalysisResuts(relatedSfn);

					if(results.size()>0) {
						for(Object[] result: results) {
							JSONObject objResult = new JSONObject();						
							objResult.put("analysisCode", result[1].toString());
							objResult.put("analysisName", result[2].toString());
							List<Object[]> antResults = UtilityDao.getAnalysisAnnotation((int) result[0]);
							if (!antResults.isEmpty()) {
								Integer antTypeNum = (Integer) antResults.get(0)[0];
								switch (antTypeNum) {
								case 3:
									objResult.put("analyzedMaterialCode", antResults.get(0)[2]);	
									objResult.put("analyzedMaterialName", antResults.get(0)[1]);
									//objResult.put("analysisTaxon", null);						
									break;
								case 5:
									objResult.put("analyzedMaterialCode", "MINERAL/"+antResults.get(0)[2]);	
									objResult.put("analyzedMaterialName", "MINERAL/"+antResults.get(0)[1]);	
									//objResult.put("analysisTaxon", antResults.get(0)[1]);	
									break;
								case 9:
									objResult.put("analyzedMaterialCode", "INCLUSION/"+antResults.get(0)[2]);	
									objResult.put("analyzedMaterialName", "INCLUSION/"+antResults.get(0)[1] + "INCLUSION");	

									//objResult.put("analysisTaxon", antResults.get(0)[1]);	
									break;
								}								
							} else {
								objResult.put("analyzedMaterialCode", "NOT PROVIDED");	
								objResult.put("analyzedMaterialName", "NOT PROVIDED");	


								//objResult.put("analysisMaterialType", samplingFeature.getMaterialCode());	
								//objResult.put("analysisTaxon", samplingFeature.getTaxonName());	
							}

							
							objResult.put("analysisComment", result[3]);
							objResult.put("datasetCode", result[4].toString());
							objResult.put("datasetTitle", result[5].toString());
							Citation citation = UtilityDao.getCitation((String) result[6]);
							JSONObject objCitation = new JSONObject();
							objCitation.put("citationCode", citation.getCitationCode());
							objCitation.put("title", citation.getRefTitle());
							objCitation.put("year", citation.getRefYear());
							objCitation.put("journal", citation.getRefJournal());
							objCitation.put("issue", citation.getRefIssue());
							objCitation.put("volume", citation.getRefVolume());
							objCitation.put("pages", citation.getRefPages());
							objCitation.put("type", citation.getRefType());
							objCitation.put("DOI", citation.getRefDOI());
								
							JSONArray authorsArr = new JSONArray();
							List<Author> authors = citation.getAuthors();
							for(Author author: authors) {
								JSONObject objAuthor = new JSONObject();
								objAuthor.put("firstName", author.getFirstName());
								objAuthor.put("lastName", author.getLastName());
								objAuthor.put("fullName", author.getFullName());
								objAuthor.put("authorOrder", author.getAuthorOrder());
								authorsArr.add(objAuthor);
							}
							objCitation.put("authors", authorsArr);
							objResult.put("citation", objCitation);
								
							JSONArray dataArr = new JSONArray();
							List<Object[]> dataElements = UtilityDao.getAnalysisData((int) result[0]);
							for(Object[] dataElement: dataElements) {
								JSONObject objDataResult = new JSONObject();
								objDataResult.put("variable", dataElement[0].toString());
								objDataResult.put("value", dataElement[1].toString());
								objDataResult.put("unit", dataElement[2].toString());
								objDataResult.put("methodCode", dataElement[3].toString());
								objDataResult.put("methodName", dataElement[4].toString());
								objDataResult.put("laboratory", dataElement[5]);
								objDataResult.put("methodComment", dataElement[6]);

								dataArr.add(objDataResult);
								//objDataResults.put("dataResults", dataArr);
							
							}
							objResult.put("dataResults", dataArr);

							resultsArr.add(objResult);
							//objResults.put("analysisResult", objResult);

						}

					}
	
				}
				objSpecimen.put("analysisResults", resultsArr);



				String fileName = "json/" + sfNum.toString() + ".json";
				FileWriter file = new FileWriter(fileName);
				file.write(objSpecimen.toJSONString());
				System.out.println("Successfully Copied JSON Object "+ sfNum.toString()  +" to File...");
				//System.out.println("\nJSON Object: " + objSpecimen);
				file.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
