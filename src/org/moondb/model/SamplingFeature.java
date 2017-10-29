package org.moondb.model;

import java.util.Date;
import java.util.List;

/*
 * MoonDB(table:sampling_feature)	Template(sheet:SAMPLES)		Java(Class:Dataset)	
 * sampling_feature_code			SAMPLE NAME					samplingFeatureCode
 * sampling_feature_description		SAMPLE_COMMENT				samplingFeatureComment
 * sampling_feature_type_num									samplingFeatureTypeNum
 * 									STATION_NAME				parentSamplingFeatureCode
 */

public class SamplingFeature {
	private String samplingFeatureCode;
	private String samplingFeatureName;
	private String samplingFeatureComment;
	private Integer samplingFeatureTypeNum;
	private String parentSamplingFeatureCode;

	private String samplingFeatureTypeName;
	private String materialCode;
	private String materialName;
	private String taxonName;
	private String parentTaxonName;
	private String missionName;
	private String samplingTechniqueName;
	private String lunarStation;
	private String returnContainer;
	private Landmark landMark;
    private List<String> childSfCodes;
    private String weight;
    private String pristinity;
    private String pristinityDate;
    
	public String getWeight() {
		return weight;
	}
    
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getPristinity() {
		return pristinity;
	}
    
	public void setPristinity(String pristinity) {
		this.pristinity = pristinity;
	}
	
	public String getPristinityDate() {
		return pristinityDate;
	}
    
	public void setPristinityDate(String pristinityDate) {
		this.pristinityDate = pristinityDate;
	}
	
	public List<String> getChildSfCodes() {
		return childSfCodes;
	}
	
	public void setChildSfCodes(List<String> childSfCodes) {
		this.childSfCodes = childSfCodes;
	}
	
	public Landmark getLandMark() {
		return landMark;
	}
	
	public void setLandMark(Landmark landMark) {
		this.landMark = landMark;
	}
	
	public String getReturnContainer() {
		return returnContainer;
	}
	
	public void setReturnContainer(String returnContainer) {
		this.returnContainer = returnContainer;
	}
	
	public String getLunarStation() {
		return lunarStation;
	}
	
	public void setLunarStation(String lunarStation) {
		this.lunarStation = lunarStation;
	}
	
	public String getSamplingTechniqueName() {
		return samplingTechniqueName;
	}
	
	public void setSamplingTechniqueName(String samplingTechniqueName) {
		this.samplingTechniqueName = samplingTechniqueName;
	}
	
	public String getMissionName() {
		return missionName;
	}
	
	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}
	
	public String getTaxonName() {
		return taxonName;
	}
	
	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}
	
	public String getParentTaxonName() {
		return parentTaxonName;
	}
	
	public void setParentTaxonName(String parentTaxonName) {
		this.parentTaxonName = parentTaxonName;
	}
	
	
	public String getMaterialCode() {
		return materialCode;
	}
	
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	
	public String getSamplingFeatureTypeName() {
		return samplingFeatureTypeName;
	}
	
	public void setSamplingFeatureTypeName(String samplingFeatureTypeName) {
		this.samplingFeatureTypeName = samplingFeatureTypeName;
	}
	
	public String getSamplingFeatureCode() {
		return samplingFeatureCode;
	}
	
	public void setSamplingFeatureCode(String samplingFeatureCode) {
		this.samplingFeatureCode = samplingFeatureCode;
	}
	
	public String getSamplingFeatureName() {
		return samplingFeatureName;
	}
	
	public void setSamplingFeatureName(String samplingFeatureName) {
		this.samplingFeatureName = samplingFeatureName;
	}
	
	public String getSamplingFeatureComment() {
		return samplingFeatureComment;
	}
	
	public void setSamplingFeatureComment(String samplingFeatureComment) {
		this.samplingFeatureComment = samplingFeatureComment;
	}
	
	public Integer getSamplingFeatureTypeNum() {
		return samplingFeatureTypeNum;
	}
	
	public void setSamplingFeatureTypeNum(Integer samplingFeatureTypeNum) {
		this.samplingFeatureTypeNum = samplingFeatureTypeNum;
	}
	
	public String getParentSamplingFeatureCode() {
		return parentSamplingFeatureCode;
	}
	
	public void setParentSamplingFeatrureCode(String parentSamplingFeatureCode) {
		this.parentSamplingFeatureCode = parentSamplingFeatureCode;
	}
}
