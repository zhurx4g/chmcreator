package com.googlecode.chmcreator.builder;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.googlecode.chmcreator.Application;
import com.googlecode.chmcreator.bean.Project;
import com.googlecode.chmcreator.bean.Workspace;

public class WorkspaceBuilder {
	protected Log logger = LogFactory.getLog(getClass());
	
	private String path = null;
	
	public WorkspaceBuilder(String path){
		this.path = path;
	}
	
	public Workspace build(Application application, Tree workspaceTree){
		Workspace workspace = new Workspace(application, workspaceTree);
		workspace.setPath(path);
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
				
				String projectPath = element.getAttribute("path");
				Project project = new ProjectBuilder().build(element.getAttribute("name"),projectPath);

				workspace.add(project);
				logger.info("Load project:" + element.getAttribute("name"));
			}

		} catch (Exception e) {
			System.out.println("exception:" + e.getMessage());
		}
	}
	
	private String getProjectsFile(){
		return path + File.separator + "projects.xml";
	}
}
