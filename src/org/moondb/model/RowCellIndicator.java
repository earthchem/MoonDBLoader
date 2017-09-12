package org.moondb.model;

//Specific row number and cell number in sheets of data template
public enum RowCellIndicator {
	TABLE_TITLES_ROW_S_NUM(1),             //Starting row number in sheet TABLE_TITLES
	SAMPLES_ROW_S_NUM(2),	               //Starting row number in sheet SAMPLES
	METHODS_ROW_S_NUM(1),	               //Starting row number in sheet METHODS
	VARIABLE_ROW_S_NUM(2),                 //Starting row number of variable in sheet ROCKS, MINERALS and INCLUSIONS
	METHOD_CODE_ROW_S_NUM(3),              //Starting row number of method in sheet ROCKS, MINERALS and INCLUSIONS
	UNIT_ROW_S_NUM(4),                     //Starting row number of unit in sheet ROCKS, MINERALS and INCLUSIONS
	ROCKS_VMUCD_CELL_S_NUM(7),             //Starting cell number of variable, method, unit and chemical data in Sheet ROCKS
	ROCKS_VMUSD_CELL_E_NUM(6),             //Ending cell number of variable, method, unit and sample data in Sheet ROCKS
	MINERALS_VMUCD_CELL_S_NUM(11),         //Starting cell number of variable, method, unit and chemical data in Sheet MINERALS
	MINERALS_VMUSD_CELL_E_NUM(10),         //Ending cell number of variable, method, unit and sample data in Sheet MINERALS
	INCLUSIONS_VMUCD_CELL_S_NUM(15),       //Starting cell number of variable, method, unit and chemical data in Sheet INCLUSIONS
	INCLUSIONS_VMUSD_CELL_E_NUM(14),       //Ending cell number of variable, method, unit and sample data in Sheet INCLUSIONS
	DATA_ROW_S_NUM(8);                     //Starting row number of data in sheet ROCKS, MINERALS and INCLUSIONS

	RowCellIndicator(int value) {
		this.value = value;
	}
	
	private int value;
	
	public int getValue() {
		return value;
	}

}
