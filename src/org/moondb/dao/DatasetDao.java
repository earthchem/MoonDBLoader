package org.moondb.dao;

import org.moondb.model.Citation;
import org.moondb.model.Dataset;
import org.moondb.util.DatabaseUtil;

public class DatasetDao {
	private Citation citation;
	private Dataset dataset;
	
	public DatasetDao (Citation citation, Dataset dataset) {
		this.citation = citation;
		this.dataset = dataset;
	}
	
	public boolean isDatasetExist() {
		String query = "SELECT COUNT(*) FROM dataset WHERE dataset_code='" + dataset.getDatasetCode() + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public Integer getDatasetNum( ) {
		String query = "SELECT dataset_num FROM dataset WHERE dataset_code='" + dataset.getDatasetCode() + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public void saveDataToDB() {
		String query;
		if (!isDatasetExist()) {
			query = "INSERT INTO dataset(dataset_type,dataset_code,dataset_title) VALUES('"+dataset.getDatasetType()+"',"+dataset.getDatasetCode()+",'"+dataset.getDatasetTitle()+"')";
			DatabaseUtil.update(query);
			Integer datasetNum = getDatasetNum();
			Integer relationshipTypeNum = 3;
			query = "INSERT INTO citation_dataset(citation_num,dataset_num,relationship_type_num) VALUES('"+citation.getCitationNum()+"',"+datasetNum+",'"+relationshipTypeNum+"')";
			DatabaseUtil.update(query);
		}
	}
}
