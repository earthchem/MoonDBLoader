package org.moondb.model;
/*
 * MoonDB(table:method)	Template(sheet:METHODS)	Java(Class:Method)	
 * 							METHOD_CODE					methodCode
 * method_code				TECHNIQUE					methodTechnique
 */
public class Method {
	private String methodCode;
	private String methodTechnique;
	private Integer methodLabNum;
	private String methodComment;

	
	public String getMethodCode() {
		return methodCode;
	}

	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}
	
	public String getMethodTechnique() {
		return methodTechnique;
	}
	
	public void setMethodTechnique(String methodTechnique) {
		this.methodTechnique = methodTechnique;
	}
	
	public Integer getMethodLabNum() {
		return methodLabNum;
	}
	
	public void setMethodLabNum(Integer methodLab) {
		this.methodLabNum = methodLab;
	}
	
	public String getMethodComment() {
		return methodComment;
	}
	
	public void setMethodComment(String methodComment) {
		this.methodComment = methodComment;
	}
}
