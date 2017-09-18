package org.moondb.model;

public enum MoonDBType {
	
	
	/*
	 * Type of sampling features, value from table sampling_feature_type of moonDB
	 */
	SAMPLING_FEATURE_TYPE_SPECIMEN(1),
	SAMPLING_FEATURE_TYPE_ROCKANALYSIS(2),
	SAMPLING_FEATURE_TYPE_MINERALANALYSIS(3),
	SAMPLING_FEATURE_TYPE_INCLUSIONANALYSIS(4);
	
	private final int value;
	
	private MoonDBType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
