package com.googlecode.chmcreator.bean;


public class Project extends FileEntry {

	private boolean open;
	public Project(String name, String path){
		super(name, path, true);
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
