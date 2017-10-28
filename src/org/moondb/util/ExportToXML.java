package org.moondb.util;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.moondb.dao.UtilityDao;
import org.moondb.model.Author;
import org.moondb.model.Citation;
import org.moondb.model.Landmark;
import org.moondb.model.SamplingFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportToXML {
	
	private static String formatString(String str) {
		if (str == null)
			str = "";
		return str;
	}
	
	public static void main(String argv[]) {

		  try {


			
			List<Integer> sfNums =  UtilityDao.getSFNums();
			for(Integer sfNum : sfNums) {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				// root element sampling_feature
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("specimen");
				doc.appendChild(rootElement);
				
				// sampling_feature_ID element
				Element sfId = doc.createElement("specimenId");
				sfId.appendChild(doc.createTextNode(sfNum.toString()));
				rootElement.appendChild(sfId);
				
				SamplingFeature samplingFeature = UtilityDao.getSamplingFeature(sfNum);
			
				// mission elements
				Element mission = doc.createElement("mission");
				mission.appendChild(doc.createTextNode(formatString(samplingFeature.getMissionName())));
				rootElement.appendChild(mission);
				
				// landmark elements
				Element landMark = doc.createElement("landMark");
				Landmark landmark = samplingFeature.getLandMark();
				landMark.appendChild(doc.createTextNode(formatString(landmark.getLandmarkName())));
				rootElement.appendChild(landMark);
				
				// station elements
				Element station = doc.createElement("station");
				station.appendChild(doc.createTextNode(formatString(samplingFeature.getLunarStation())));
				rootElement.appendChild(station);
				
				// container elements
				Element returnContainer = doc.createElement("returnContainer");
				returnContainer.appendChild(doc.createTextNode(formatString(samplingFeature.getReturnContainer())));
				rootElement.appendChild(returnContainer);
				
				// samplingTechnique elements
				Element sfTechnique = doc.createElement("samplingTechnique");
				sfTechnique.appendChild(doc.createTextNode(formatString(samplingFeature.getSamplingTechniqueName())));
				rootElement.appendChild(sfTechnique);
				
				//sampling_feature_type element
				//Element sfType = doc.createElement("samplingFeatureType");
				//sfType.appendChild(doc.createTextNode(samplingFeature.getSamplingFeatureTypeName()));
			//	rootElement.appendChild(sfType);

				// sampling_feature_code element
				Element sfCode = doc.createElement("specimenCode");
				sfCode.appendChild(doc.createTextNode(samplingFeature.getSamplingFeatureCode()));
				rootElement.appendChild(sfCode);

				// sampling_feature_name element
				Element sfName = doc.createElement("specimenName");
				sfName.appendChild(doc.createTextNode(samplingFeature.getSamplingFeatureName()));
				rootElement.appendChild(sfName);
				
				// parent sampling_feature_code element
				Element parentSfCode = doc.createElement("parentSpecimenCode");
				parentSfCode.appendChild(doc.createTextNode(formatString(samplingFeature.getParentSamplingFeatureCode())));
				rootElement.appendChild(parentSfCode);
				//child sampling_feature_codes element
				Element childSamplingFeatures = doc.createElement("childSpecimens");
				List<String> childSfCodes = samplingFeature.getChildSfCodes();
				if(childSfCodes.size()>0) {
					for(String childSfCode: childSfCodes) {
						Element childSfCodeElement = doc.createElement("childSpecimenCode");
						childSfCodeElement.appendChild(doc.createTextNode(formatString(childSfCode)));
						childSamplingFeatures.appendChild(childSfCodeElement);
					}
				}
				rootElement.appendChild(childSamplingFeatures);
				
				// material_code element
				Element materialCode = doc.createElement("materialCode");
				materialCode.appendChild(doc.createTextNode(formatString(samplingFeature.getMaterialCode())));
				rootElement.appendChild(materialCode);
				
				// material_name element
				Element materialName = doc.createElement("materialName");
				materialName.appendChild(doc.createTextNode(formatString(samplingFeature.getMaterialName())));
				rootElement.appendChild(materialName);	
				
				// taxon_name element
				Element taxonName = doc.createElement("taxonName");
				taxonName.appendChild(doc.createTextNode(formatString(samplingFeature.getTaxonName())));
				rootElement.appendChild(taxonName);
				
				// parent_taxon_name element
				Element parentTaxonName = doc.createElement("parentTaxonName");
				parentTaxonName.appendChild(doc.createTextNode(formatString(samplingFeature.getParentTaxonName())));
				rootElement.appendChild(parentTaxonName);
				
				// sampling_feature_description element
				Element sfDescription = doc.createElement("specimenDescription");								
				sfDescription.appendChild(doc.createTextNode(formatString(samplingFeature.getSamplingFeatureComment())));
				rootElement.appendChild(sfDescription);
				
				//analysis results element
				Element analysisResults = doc.createElement("analysisResults");
				List<Object[]> results = UtilityDao.getAnalysisResuts(sfNum);
				if(results.size()>0) {
					for(Object[] result: results) {
						Element analysisResult = doc.createElement("analysisResult");
						Element sampleName = doc.createElement("sampleName");
						sampleName.appendChild(doc.createTextNode(result[1].toString()));
						analysisResult.appendChild(sampleName);
						Element analysisComment = doc.createElement("analysisComment");
						if(result[2] != null)
							analysisComment.appendChild(doc.createTextNode(formatString(result[2].toString())));
						else
							analysisComment.appendChild(doc.createTextNode(""));
						analysisResult.appendChild(analysisComment);
						Element datasetCode = doc.createElement("datasetCode");
						datasetCode.appendChild(doc.createTextNode(result[3].toString()));
						analysisResult.appendChild(datasetCode);
						Element datasetTitle = doc.createElement("datasetTitle");
						datasetTitle.appendChild(doc.createTextNode(result[4].toString()));
						analysisResult.appendChild(datasetTitle);
						
						Citation citation = UtilityDao.getCitation((String) result[5]);
						Element citationElement = doc.createElement("citation");
						Element citationCode = doc.createElement("citationCode");
						citationCode.appendChild(doc.createTextNode(citation.getCitationCode()));
						citationElement.appendChild(citationCode);
						Element citationTitle = doc.createElement("title");
						citationTitle.appendChild(doc.createTextNode(citation.getRefTitle()));
						citationElement.appendChild(citationTitle);
						Element citationYear = doc.createElement("year");
						citationYear.appendChild(doc.createTextNode(formatString(citation.getRefYear().toString())));
						citationElement.appendChild(citationYear);
						Element citationjournal = doc.createElement("journal");
						citationjournal.appendChild(doc.createTextNode(formatString(citation.getRefJournal())));
						citationElement.appendChild(citationjournal);
						Element citationIssue = doc.createElement("issue");
						citationIssue.appendChild(doc.createTextNode(formatString(citation.getRefIssue())));
						citationElement.appendChild(citationIssue);
						
						Element citationVolume = doc.createElement("volume");
						citationVolume.appendChild(doc.createTextNode(formatString(citation.getRefVolume())));
						citationElement.appendChild(citationVolume);
						
						Element citationPages = doc.createElement("pages");
						citationPages.appendChild(doc.createTextNode(formatString(citation.getRefPages())));
						citationElement.appendChild(citationPages);
						
						Element citationType = doc.createElement("type");
						citationType.appendChild(doc.createTextNode(formatString(citation.getRefType())));
						citationElement.appendChild(citationType);
						
						Element citationDoi = doc.createElement("DOI");
						citationDoi.appendChild(doc.createTextNode(formatString(citation.getRefDOI())));
						citationElement.appendChild(citationDoi);
						
						Element citationAuthors = doc.createElement("authors");
						List<Author> authors = citation.getAuthors();
						for(Author author: authors) {
							Element citationAuthor = doc.createElement("author");
							Element firstName = doc.createElement("firstName");
							firstName.appendChild(doc.createTextNode(formatString(author.getFirstName())));
							citationAuthor.appendChild(firstName);
							Element lastName = doc.createElement("lastName");
							lastName.appendChild(doc.createTextNode(formatString(author.getLastName())));
							citationAuthor.appendChild(lastName);
							Element fullName = doc.createElement("fullName");
							fullName.appendChild(doc.createTextNode(formatString(author.getFullName())));
							citationAuthor.appendChild(fullName);
							citationAuthors.appendChild(citationAuthor);
							
						}
						citationElement.appendChild(citationAuthors);
						analysisResult.appendChild(citationElement);
						
						
						List<Object[]> dataElements = UtilityDao.getAnalysisData((int) result[0]);
						for(Object[] dataElement: dataElements) {
							Element data = doc.createElement("data");
							Element varCode = doc.createElement("variable");
							varCode.appendChild(doc.createTextNode(dataElement[0].toString()));
							data.appendChild(varCode);
							Element value = doc.createElement("value");
							value.appendChild(doc.createTextNode(dataElement[1].toString()));
							data.appendChild(value);
							Element unit = doc.createElement("unit");
							unit.appendChild(doc.createTextNode(dataElement[2].toString()));
							data.appendChild(unit);
							
							Element method = doc.createElement("method");
							method.appendChild(doc.createTextNode(dataElement[3].toString()));
							data.appendChild(method);
							Element lab = doc.createElement("laboratory");
							lab.appendChild(doc.createTextNode(dataElement[4].toString()));
							data.appendChild(lab);
							Element methodComment = doc.createElement("methodComment");
							if(dataElement[5] != null)
								methodComment.appendChild(doc.createTextNode(formatString(dataElement[5].toString())));
							else
								methodComment.appendChild(doc.createTextNode(""));

							data.appendChild(methodComment);
							analysisResult.appendChild(data);
						}


						analysisResults.appendChild(analysisResult);
					}
				}
				rootElement.appendChild(analysisResults);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				String fileName = "xml/" + sfNum.toString() + ".xml";
				StreamResult result = new StreamResult(new File(fileName));

				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);

				transformer.transform(source, result);

				System.out.println("File saved!");
			}







		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
}
