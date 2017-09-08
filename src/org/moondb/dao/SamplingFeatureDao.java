package org.moondb.dao;

import java.util.List;

import org.moondb.model.Dataset;
import org.moondb.model.SamplingFeature;
import org.moondb.model.SamplingFeatures;
import org.moondb.util.DatabaseUtil;

public class SamplingFeatureDao {
	private SamplingFeatures samplingFeatures;
	
	public SamplingFeatureDao(SamplingFeatures samplingFeatures) {
		this.samplingFeatures = samplingFeatures;
		
	}
	
	private boolean isSamplingFeatureExist(String samplingFeatureCode) {
		String query = "SELECT COUNT(*) FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	private Integer getSamplingFeatureNum(String samplingFeatureCode) {
		String query = "SELECT sampling_feature_num FROM sampling_feature WHERE sampling_feature_code='" + samplingFeatureCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public void saveDataToDB() {
		List<SamplingFeature> sfs = samplingFeatures.getSamplingFeatures();

		for(SamplingFeature sf: sfs) {
			String sfCode = sf.getSamplingFeatureCode();
			String sfParentCode = sfCode.substring(0, sfCode.indexOf(','))+ ",0";
			String sfComment = sf.getSamplingFeatureComment();
			Integer sfTypeNum = sf.getSamplingFeatureTypeNum();
			
			String query;
			if (!isSamplingFeatureExist(sfCode)) {
				//save to table sampling_feature
				query = "INSERT INTO sampling_feature(sampling_feature_type_num,sampling_feature_code,sampling_feature_description) VALUES('"+sfTypeNum+"','"+sfCode+"','"+sfComment+"')";
				DatabaseUtil.update(query);
				Integer sfNum = getSamplingFeatureNum(sfCode);
				Integer sfParentNum = getSamplingFeatureNum(sfParentCode);
				Integer relationshipTypeNum = 9;  //isSubSampleOf
				//save to table related_feature
				query = "INSERT INTO related_feature(sampling_feature_num,related_sampling_feature_num,relationship_type_num) VALUES('"+sfNum+"',"+sfParentNum+",'"+relationshipTypeNum+"')";
				DatabaseUtil.update(query);
			}			

		}
	}
}
