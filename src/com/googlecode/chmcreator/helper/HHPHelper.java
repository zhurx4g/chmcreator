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
	private static Map<String, String> HHC_KEYWORS = new HashMap<String, String>();
	static {
		HHC_KEYWORS.put("extensens.projectName", "projectName");
		
		HHC_KEYWORS.put("options.binary.index", "Binary Index");
		HHC_KEYWORS.put("options.binary.toc", "Binary TOC");
		HHC_KEYWORS.put("options.compatibility", "Compatibility");
		HHC_KEYWORS.put("options.compiled.file", "Compiled file");
		HHC_KEYWORS.put("options.contents.file", "Contents file");
		HHC_KEYWORS.put("options.default.font", "Default Font");
		HHC_KEYWORS.put("options.default.topic", "Default topic");
		HHC_KEYWORS.put("options.display.compile.progress", "Display compile progress");
		HHC_KEYWORS.put("options.enhanced.decompilation", "Enhanced decompilation");
		HHC_KEYWORS.put("options.error.log.file", "Error log file");
		HHC_KEYWORS.put("options.flat", "Flat");
		
		HHC_KEYWORS.put("options.full-text.search", "Full-text search");
		HHC_KEYWORS.put("options.index.file", "Index file");
		HHC_KEYWORS.put("options.language", "Language");
		HHC_KEYWORS.put("options.title", "Title");
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
		String val = HHC_KEYWORS.get(key);
		return val==null?(String)key:val;
	}
}
