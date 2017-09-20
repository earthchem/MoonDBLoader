package org.moondb.model;

import java.util.List;

public class SampleResult {
	
	private int datasetNum;
	private int samplingFeatureNum;
	private List<ChemistryResult> chemistryResults;
	private String analysisComment;
	private String calcAvge;
	private String replicatesCount;
	private String material;
	private String mineral;
	private String grain;
	private String rimCore;
	private String mineralSize;
	private String spotId;
	private String inclusionType;
	private String inclusionMineral;
	private String hostMineral;
	private String hostRock;
	private String inclusionSize;
	private String heated;
	private String temperature;
	
	public int getDatasetNum() {
		return datasetNum;
	}
	
	public void setDatasetNum(int datasetNum) {
		this.datasetNum = datasetNum;
	}
	
	public int getSamplingFeatureNum() {
		return samplingFeatureNum;
	}
	
	public void setSamplingFeatureNum(int samplingFeatureNum) {
		this.samplingFeatureNum = samplingFeatureNum;
	}
	
	public List<ChemistryResult> getChemistryResults() {
		return chemistryResults;
	}

	public void setChemistryResults(List<ChemistryResult> chemistryResults) {
		this.chemistryResults = chemistryResults;
	}
	
	public String getAnalysisComment() {
		return analysisComment;
	}
	
	public void setAnalysisComment(String analysisComment) {
		this.analysisComment = analysisComment;
	}
	
	public String getCalcAvge() {
		return calcAvge;
	}
	
	public void setCalcAvge(String calcAvge) {
		this.calcAvge = calcAvge;
	}
	
	public String getReplicatesCount() {
		return replicatesCount;
	}
	
	public void setReplicatesCount(String replicatesCount) {
		this.replicatesCount = replicatesCount;
	}
	
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String material) {
		this.material = material;
	}
	
	public String getMineral() {
		return mineral;
	}
	
	public void setMineral(String mineral) {
		this.mineral = mineral;
	}
	
	public String getGrain() {
		return grain;
	}
	
	public void setGrain(String grain) {
		this.grain = grain;
	}
	
	public String getRimCore() {
		return rimCore;
	}
	
	public void setRimCore(String rimCore) {
		this.rimCore = rimCore;
	}
	
	public String getMineralSize() {
		return mineralSize;
	}
	
	public void setMineralSize(String mineralSize) {
		this.mineralSize = mineralSize;
	}
	
	public String getSpotId() {
		return spotId;
	}
	
	public void setSpotId(String spotId) {
		this.spotId = spotId;
	}
	
	public String getInclusionType() {
		return inclusionType;
	}
	
	public void setInclusionType(String inclusionType) {
		this.inclusionType = inclusionType;
	}
	
	public String getInclusionMineral() {
		return inclusionMineral;
	}
	
	public void setInclusionMineral(String inclusionMineral) {
		this.inclusionMineral = inclusionMineral;
	}
	
	public String getHostMineral() {
		return hostMineral;
	}
	
	public void setHostMineral(String hostMineral) {
		this.hostMineral = hostMineral;
	}
	
	public String getHostRock() {
		return hostRock;
	}
	
	public void setHostRock(String hostRock) {
		this.hostRock = hostRock;
	}
	
	public String getInclusionSize() {
		return inclusionSize;
	}
	
	public void setInclusionSize(String inclusionSize) {
		this.inclusionSize = inclusionSize;
	}
	
	public String getHeated() {
		return heated;
	}
	
	public void setHeated(String heated) {
		this.heated = heated;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
}
