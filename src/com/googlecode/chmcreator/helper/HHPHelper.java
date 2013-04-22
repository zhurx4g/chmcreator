package com.googlecode.chmcreator.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.googlecode.chmcreator.bean.Project;

public class HHPHelper {
	private static Map<String, String> keywords = new HashMap<String, String>();
	static {
		keywords.put("extensens.projectName", "projectName");
		
		keywords.put("options.binary.index", "Binary Index");
		keywords.put("options.binary.toc", "Binary TOC");
		keywords.put("options.compatibility", "Compatibility");
		keywords.put("options.compiled.file", "Compiled file");
		keywords.put("options.contents.file", "Contents file");
		keywords.put("options.default.font", "Default Font");
		keywords.put("options.default.topic", "Default topic");
		keywords.put("options.display.compile.progress", "Display compile progress");
		keywords.put("options.enhanced.decompilation", "Enhanced decompilation");
		keywords.put("options.error.log.file", "Error log file");
		keywords.put("options.flat", "Flat");
		
		keywords.put("options.full-text.search", "Full-text search");
		keywords.put("options.index.file", "Index file");
		keywords.put("options.language", "Language");
		keywords.put("options.title", "Title");
	}
	
	public static String getPath(Project project){
		return project.getPath() + "/" + project.getName() +".hhp";
	}
	public static boolean save(Project project){
		String path = getPath(project);
		return save(project, path);
	}

	public static boolean save(Project project, String path){
		Properties props = project.getProperties();
		StringBuffer buffer = new StringBuffer();
		
		List<Entry<Object, Object>> extensensList = new ArrayList<Entry<Object, Object>>();
		List<Entry<Object, Object>> optionsList = new ArrayList<Entry<Object, Object>>();
		for(Entry<Object, Object> entry: props.entrySet()){
			String key = (String)entry.getKey();
			if(key.startsWith("options.")){
				optionsList.add(entry);
			}else if(key.startsWith("extensens.")){
				extensensList.add(entry);
			}else{
				optionsList.add(entry);
			}
		}
		
		buffer.append("[EXTENSENS]\n");
		for(Entry<Object, Object> ext:extensensList){
			buffer.append(getKey(ext.getKey())+"=" + ext.getValue()+"\n");
		}
		buffer.append("[OPTIONS]\n");
		for(Entry<Object, Object> opt:optionsList){
			buffer.append(getKey(opt.getKey())+"=" + opt.getValue()+"\n");
		}
		
		try {
			FileUtils.writeStringToFile(new File(path), buffer.toString(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private static String getKey(Object key){
		String val = keywords.get(key);
		return val==null?(String)key:val;
	}
}
