package org.moondb.model;
/*
 * MoonDB(table:method)	Template(sheet:METHODS)	Java(Class:Method)	
 * 							METHOD_CODE					methodCode
 * method_code				TECHNIQUE					methodTechnique
 */
public class Method {
	private String methodCode;
	private String methodTechnique;

	
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
}
