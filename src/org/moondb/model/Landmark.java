package org.moondb.model;

public class Landmark {
	private String landmarkName;
	private Integer gpnfID;
	private String gpnfURL;
	private Double latitude;
	private Double longitude;
	
	public String getLandmarkName() {
		return landmarkName;
	}
	
	public void setLandmarkName(String landmarkName) {
		this.landmarkName = landmarkName;
	}
	
	public Integer getGpnfID() {
		return gpnfID;
	}
	
	public void setGpnfID(Integer gpnfID) {
		this.gpnfID = gpnfID;
	}
	
	public String getGpnfURL() {
		return gpnfURL;
	}
	
	public void setGpnfURL(String gpnfURL) {
		this.gpnfURL = gpnfURL;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
