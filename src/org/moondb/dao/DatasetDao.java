package org.moondb.dao;

import java.util.List;

import org.moondb.model.Citation;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.util.DatabaseUtil;

public class DatasetDao {
	private Citation citation;
	private Datasets datasets;
	
	public DatasetDao (Citation citation, Datasets datasets) {
		this.citation = citation;
		this.datasets = datasets;
	}
	
	private boolean isDatasetExist(String datasetCode) {
		String query = "SELECT COUNT(*) FROM dataset WHERE dataset_code='" + datasetCode + "'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	private Integer getDatasetNum(String datasetCode) {
		String query = "SELECT dataset_num FROM dataset WHERE dataset_code='" + datasetCode + "'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}
	
	public void saveDataToDB() {
		List<Dataset> dss = datasets.getDatasets();
		String citationCode = citation.getCitationCode();
		for(Dataset ds: dss) {
			String datasetCode = citationCode + ", " + ds.getTableNum();  //create unique dataset_code by combing citation_code and number of TABLE_IN_REF
			String datasetType = ds.getDatasetType();
			String datasetTitle = ds.getDatasetTitle();
			String query;
			if (!isDatasetExist(citationCode)) {
				//save to table dataset
				query = "INSERT INTO dataset(dataset_type,dataset_code,dataset_title) VALUES('"+datasetType+"','"+datasetCode+"','"+datasetTitle+"')";
				DatabaseUtil.update(query);
				Integer datasetNum = getDatasetNum(datasetCode);
				Integer relationshipTypeNum = 3;
				//save to table citation_dataset
				query = "INSERT INTO citation_dataset(citation_num,dataset_num,relationship_type_num) VALUES('"+citation.getCitationNum()+"',"+datasetNum+",'"+relationshipTypeNum+"')";
				DatabaseUtil.update(query);
			}			

		}

	}
}
