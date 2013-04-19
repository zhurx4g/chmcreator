package com.googlecode.chmcreator.bean;

public class Project {

	private String name;
	private String path;

	public Project(String name, String path){
		this.name = name;
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
