package org.moondb.model;
/*
 * MoonDB(table:method)	Template(sheet:METHODS)	Java(Class:Method)	
 * 							METHOD_CODE					methodNum
 * method_code				TECHNIQUE					methodCode
 */
public class Method {
	private String methodCode;
	private Integer methodNum;
	
	public String getMethodCode() {
		return methodCode;
	}

	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}
	
	public Integer getMethodNum() {
		return methodNum;
	}
	
	public void setMethodNum(Integer methodNum) {
		this.methodNum = methodNum;
	}
}
