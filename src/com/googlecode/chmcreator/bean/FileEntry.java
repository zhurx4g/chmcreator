package com.googlecode.chmcreator.bean;

import java.util.ArrayList;
import java.util.List;

public class FileEntry {

	private String name;
	private String path;
	private boolean parent;
	
	private List<FileEntry> entryList = new ArrayList<FileEntry>();
	
	public boolean isParent() {
		return parent;
	}
	public void setParent(boolean parent) {
		this.parent = parent;
	}
	
	public FileEntry(String name, String path, boolean parent){
		this.name = name;
		this.path = path;
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FileEntry> getEntryList() {
		return entryList;
	}
	public void setEntryList(List<FileEntry> entryList) {
		this.entryList = entryList;
	}
	
	public void addFileEntry(FileEntry entry){
		entryList.add(entry);
	}
	
	public void removeFileEntry(FileEntry entry){
		entryList.remove(entry);
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
