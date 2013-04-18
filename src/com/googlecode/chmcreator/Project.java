package com.googlecode.chmcreator;

public class Project {

	private String name;
	private String path;

	public Project(String path, String name){
		this.path = path;
		this.name = name;
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
