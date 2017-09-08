package org.moondb.model;

/*
 * MoonDB(table:sampling_feature)	Template(sheet:SAMPLES)		Java(Class:Dataset)	
 * sampling_feature_code			SAMPLE NAME					samplingFeatureCode
 * sampling_feature_description		SAMPLE_COMMENT				samplingFeatureComment
 */

public class SamplingFeature {
	private String samplingFeatureCode;
	private String samplingFeatureComment;
	
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

}
