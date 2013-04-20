package com.googlecode.chmcreator.builder;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.chmcreator.CHM;
import com.googlecode.chmcreator.bean.FileEntry;
import com.googlecode.chmcreator.bean.Project;

public class ProjectBuilder {

	public ProjectBuilder(){
		
	}
	
	public Project build(String name, String path){
		Project project = new Project(name, path);
		parse(project,path);
		return project;
	}
	
	public Project build(String path){
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		Project project = new Project(file.getName(), path);
		parse(project,file);
		return project;
	}
	
	private void parse(Project project,File dir) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			Element rootElement = document.createElement("project");
			document.appendChild(rootElement);
			Element entryListElement = document.createElement("entryList");
			rootElement.appendChild(entryListElement);
			
			parseDir(project,document, entryListElement, dir);
			
			TransformerFactory tff = TransformerFactory.newInstance();
			tff.setAttribute("indent-number", 4);
		    Transformer tf = tff.newTransformer();
		    DOMSource source = new DOMSource(document);
		    StreamResult result = new StreamResult(new File(dir, "project.xml"));
		   
		    //设置输出编码
		    tf.setOutputProperty(OutputKeys.INDENT,"yes");
		    tf.setOutputProperty("encoding", "utf-8");
		    tf.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		project.setOpen(true);
	}
	
	private void parseDir(FileEntry parentEntry, Document document, Element parent, File dir){
		String[] fileNames = dir.list();
		for(String fileName:fileNames){
			if(CHM.PROJECT_NAME.equals(fileName)&&parentEntry instanceof Project){
				continue;
			}
			Element entry = document.createElement("entry");
			File file = new File(dir, fileName);
			entry.setAttribute("name", file.getName());
			entry.setAttribute("path", file.getAbsolutePath());
			entry.setAttribute("parent", String.valueOf(file.isDirectory()));
			parent.appendChild(entry);
			
			FileEntry fileEntry = new FileEntry(entry.getAttribute("name"), entry.getAttribute("path"),Boolean.parseBoolean(entry.getAttribute("parent")));
			parentEntry.addFileEntry(fileEntry);
			
			if(file.isDirectory()){
				parseDir(fileEntry, document, entry, file);
			}
		}
	}
	private void parse(Project project,String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File projectFile = new File(getProjectFile(path));
			if(!projectFile.exists())
				return;
			project.setOpen(projectFile.exists());
			Document document = builder.parse(projectFile);
			Element rootElement = document.getDocumentElement();

			Element entryList = (Element)rootElement.getElementsByTagName("entryList").item(0);
			parseElement(entryList, project);
			/*
			for(int i=0; i<list.getLength();i++){
				Element element = (Element) list.item(i);
				String projectPath = element.getAttribute("path");
				project.addFileEntry(new FileEntry(element.getAttribute("name"),
						projectPath,Boolean.parseBoolean(element.getAttribute("parent"))));
			}*/

		} catch (Exception e) {
			System.out.println("exception:" + path + e.getMessage());
			project.setOpen(false);
		}
	}
	
	private void parseElement(Element parent, FileEntry parentEntry){
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength();i++){
			Node node = list.item(i);
			System.out.println(node.getNodeName());
			if(!(node instanceof Element &&((Element) node).getNodeName().equals("entry"))){
				continue;
			}
			Element element = (Element) node;
			String projectPath = element.getAttribute("path");
			FileEntry fileEntry = new FileEntry(element.getAttribute("name"),projectPath,Boolean.parseBoolean(element.getAttribute("parent")));
			parentEntry.addFileEntry(fileEntry);
			if(element.getChildNodes().getLength()>0){
				parseElement(element, fileEntry);
			}
		}
	}
	private String getProjectFile(String path){
		return path + File.separator + "project.xml";
	}
}
