package com.googlecode.chmcreator.builder;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.googlecode.chmcreator.bean.Project;
import com.googlecode.chmcreator.bean.Workspace;

public class WorkspaceBuilder {
	
	private String path = null;
	
	public WorkspaceBuilder(String path){
		this.path = path;
	}
	
	public Workspace build(Tree workspaceTree){
		Workspace workspace = new Workspace(workspaceTree);
		parse(workspace);
		return workspace;
	}
	
	private void parse(Workspace workspace) {
		String projectsFile = getProjectsFile();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(projectsFile));
			Element rootElement = document.getDocumentElement();

			NodeList list = rootElement.getElementsByTagName("project");
			for(int i=0; i<list.getLength();i++){
				Element element = (Element) list.item(i);
				workspace.add(new Project(element.getAttribute("name"),element.getAttribute("path")));
				System.out.println(element.getAttribute("name"));				
			}

		} catch (Exception e) {
			System.out.println("exception:" + e.getMessage());
		}
	}

	private String getProjectsFile(){
		return path + File.separator + "projects.xml";
	}
}
