package com.googlecode.chmcreator.bean;

import java.util.Properties;


public class Project extends FileEntry {

	private boolean open;
	private Properties props = new Properties();
	
	public Project(String name, String path){
		super(name, path, true);
	}
	
	public Properties getProperties() {
		return props;
	}

	public void setProperties(Properties props) {
		this.props = props;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
