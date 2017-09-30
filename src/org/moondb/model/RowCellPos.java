package org.moondb.model;

//Specific row number and cell number in sheets of data template
public enum RowCellPos {
	
	/*
	 * indicate the data is ending if the cell holding end sign "-1" or null 
	 * corresponding the column 'SAMPLE NMAE' in the sheet SAMPLES
	 */
	SAMPLES_DATA_END_SYMBOL_COL_NUM(1),
	/*
	 * indicate the data is ending if the cell holding end sign "-1" or null 
	 * corresponding the column 'SAMPLE NMAE' in the sheet ROCKS, MINERALS and INCLUSIONS
	 */
	RMI_DATA_END_SYMBOL_COL_NUM(2),

	/*
	 * Beginning row number in sheet TABLE_TITLES
	 */
	TABLE_TITLES_BEGIN_ROW_NUM(1),    
	/*
	 * indicate the data is ending if the cell holding end sign "-1" or null 
	 * corresponding the column 'Number of TABLE_IN_REF' in the sheet TABLE_TITLES
	 */
	TABLE_TILES_END_SYMBOL_COL_NUM(0),
	
	/*
	 * Beginning row number in sheet SAMPLES
	 */
	SAMPLES_BEGIN_ROW_NUM(2),	        
	/*
	 * Beginning row number in sheet METHODS
	 */
	METHODS_BEGIN_ROW_NUM(1),	 
	/*
	 * Beginning row number of variable in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	/*
	 * indicate the data is ending if the cell holding end sign "-1" or null 
	 * corresponding the column 'METHOD_NUM' in the sheet METHODS
	 */
	METHODS_END_SYMBOL_COL_NUM(0),
	
	VARIABLE_BEGIN_ROW_NUM(2),    
	/*
	 * Beginning row number of method in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	METHOD_CODE_BEGIN_ROW_NUM(3),  
	/*
	 * Beginning row number of unit in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	UNIT_ROW_B(4), 
	/*
	 * Beginning cell number of variable, method, unit and chemical data in Sheet ROCKS
	 */
	ROCKS_VMUCD_CELL_B(7),  
	/*
	 * Ending cell number of variable, method, unit and sample data in Sheet ROCKS
	 */
	ROCKS_VMUSD_CELL_E(6),   
	/*
	 * Beginning cell number of variable, method, unit and chemical data in Sheet MINERALS
	 */
	MINERALS_VMUCD_CELL_B(11), 
	/*
	 * Ending cell number of variable, method, unit and sample data in Sheet MINERALS
	 */
	MINERALS_VMUSD_CELL_E(10), 
	/*
	 * Beginning cell number of variable, method, unit and chemical data in Sheet INCLUSIONS
	 */
	INCLUSIONS_VMUCD_CELL_B(15),
	/*
	 * Ending cell number of variable, method, unit and sample data in Sheet INCLUSIONS
	 */
	INCLUSIONS_VMUSD_CELL_E(14),   
	/*
	 * Beginning row number of data in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	RMI_DATA_BEGIN_ROW_NUM(8),         
	
	/*
	 * Column number of SPOT_ID in sheet MINERALS and INCLUSIONS
	 */
	MINERALS_SPOTID_COL_NUM(4),
	INCLUSIONS_SPOTID_COL_NUM(3),
	
	/*
	 * Column number of CALC_AVGE in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	ROCKS_AVGE_COL_NUM(5),
	MINERALS_AVGE_COL_NUM(6),
	INCLUSIONS_AVGE_COL_NUM(5),
	
	/*
	 * Column number of NUMBER OF REPLICATE in sheet ROCKS, MINERALS and INCLUSIONS
	 */
	ROCKS_REPLICATE_COL_NUM(4),
	MINERALS_REPLICATE_COL_NUM(5),
	INCLUSIONS_REPLICATE_COL_NUM(4);

	private final int value;
	
	private RowCellPos(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
