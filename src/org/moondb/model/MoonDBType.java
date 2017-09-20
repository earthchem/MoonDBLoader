package org.moondb.model;

public enum MoonDBType {
	/*
	 * Type of processing_level, value from table processing_level of moonDB
	 */
	
	PROCESSING_LEVEL_RAW_DATA(1),
	/*
	 * Type of result, value from table result_type of moonDB
	 */
	RESULT_TYPE_MEASUREMENT(1),
	
	/*
	 * Type of method, value from table method_type of moonDB
	 */
	METHOD_TYPE_LABANALYSIS(3),
	/*
	 * Type of action, value from table action_type of moonDB
	 */
	ACTION_TYPE_SPECIMEN_ANALYSIS(20),

	/*
	 * Type of variable, value from table variable_type of moonDB
	 */
	VARIABLE_TYPE_MV(18),
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
