package com.hnu.entity;

public class UavPathInfo {
	private Integer pathId;
	private Integer uavId;
	private Integer startX;
	private Integer startY;
	private Integer endX;
	private Integer endY;
	private String path;

	public Integer getPathId() {
		return pathId;
	}

	public void setPathId(Integer pathId) {
		this.pathId = pathId;
	}

	public Integer getUavId() {
		return uavId;
	}

	public void setUavId(Integer uavId) {
		this.uavId = uavId;
	}

	public Integer getStartX() {
		return startX;
	}

	public void setStartX(Integer startX) {
		this.startX = startX;
	}

	public Integer getStartY() {
		return startY;
	}

	public void setStartY(Integer startY) {
		this.startY = startY;
	}

	public Integer getEndX() {
		return endX;
	}

	public void setEndX(Integer endX) {
		this.endX = endX;
	}

	public Integer getEndY() {
		return endY;
	}

	public void setEndY(Integer endY) {
		this.endY = endY;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public UavPathInfo() {
	}

	@Override
	public String toString() {
		return "UavPathInfo{" +
				"pathId=" + pathId +
				", uavId=" + uavId +
				", startX=" + startX +
				", startY=" + startY +
				", endX=" + endX +
				", endY=" + endY +
				", path='" + path + '\'' +
				'}';
	}
}
