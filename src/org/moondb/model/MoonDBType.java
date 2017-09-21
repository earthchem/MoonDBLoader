package org.moondb.model;

public enum MoonDBType {
	
	/*
	 * Type of annotation, value from table annotation_type of moonDB
	 */
	ANNOTATION_TYPE_NUMBER_OF_REPLICATES(1),
	ANNOTATION_TYPE_CALCULATED_AVERAGE(2),
	ANNOTATION_TYPE_MATERIAL(3),
	ANNOTATION_TYPE_SPOT_ID(4),
	ANNOTATION_TYPE_MINERAL(5),
	ANNOTATION_TYPE_GRAIN(6),
	ANNOTATION_TYPE_RIM_OR_CORE(7),
	ANNOTATION_TYPE_MINERAL_SIZE(8),
	ANNOTATION_TYPE_INCLUSION_TYPE(9),
	ANNOTATION_TYPE_INCLUSION_MINERAL(10),
	ANNOTATION_TYPE_HOST_MINERAL(11),
	ANNOTATION_TYPE_HOST_ROCK(12),
	ANNOTATION_TYPE_INCLUSION_SIZE(13),
	ANNOTATION_TYPE_HEATED(14),
	ANNOTATION_TYPE_TEMPERATURE(15),
	
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
