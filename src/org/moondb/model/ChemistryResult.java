package org.moondb.model;

public class ChemistryResult {
	private int methodNum;
	private int variableNum;
	private int unitNum;
	private double value;
	
	public int getMethodNum() {
		return methodNum;
	}
	
	public void setMethodNum(int methodNum) {
		this.methodNum = methodNum;
	}
	
	public int getVariableNum() {
		return variableNum;
	}
	
	public void setVariableNum(int variableNum) {
		this.variableNum = variableNum;
	}
	
	public int getUnitNum() {
		return unitNum;
	}

	public void setUnitNum(int unitNum) {
		this.unitNum = unitNum;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setVaule(double value) {
		this.value = value;
	}
}
