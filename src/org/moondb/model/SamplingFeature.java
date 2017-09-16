package org.moondb.model;

/*
 * MoonDB(table:sampling_feature)	Template(sheet:SAMPLES)		Java(Class:Dataset)	
 * sampling_feature_code			SAMPLE NAME					samplingFeatureCode
 * sampling_feature_description		SAMPLE_COMMENT				samplingFeatureComment
 * sampling_feature_type_num									samplingFeatureTypeNum
 * 									STATION_NAME				parentSamplingFeatureCode
 */

public class SamplingFeature {
	private String samplingFeatureCode;
	private String samplingFeatureComment;
	private Integer samplingFeatureTypeNum;
	private String parentSamplingFeatureCode;
	
	
	public String getSamplingFeatureCode() {
		return samplingFeatureCode;
	}
	
	public void setSamplingFeatureCode(String samplingFeatureCode) {
		this.samplingFeatureCode = samplingFeatureCode;
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
