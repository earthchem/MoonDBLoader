package org.moondb.model;

public class Action {
	private String actionName;
	private int actionTypeNum;
	private String actionDescription;
	private int methodNum;
	private int datasetNum;
	
	public String getActionName() {
		return actionName;
	}
	
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public int getActionTypeNum() {
		return actionTypeNum;
	}
	
	public void setActionTypeNum(int actionTypeNum) {
		this.actionTypeNum = actionTypeNum;
	}
	
	public String getActionDesctription() {
		return actionDescription;
	}
	
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
	
	public int getMethodNum() {
		return methodNum;
	}
	
	public void setMethodNum(int methodNum) {
		this.methodNum = methodNum;
	}
	
	public int getDatasetNum() {
		return datasetNum;
	}
	
	public void setDatasetNum(int datasetNum) {
		this.datasetNum = datasetNum;
	}
}
