package com.googlecode.chmcreator.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.chmcreator.Application;
import com.googlecode.chmcreator.CHM;
import com.googlecode.chmcreator.ResourceLoader;

public class Workspace implements SelectionListener {

	private List<Project> projectList = new ArrayList<Project>();
	private Tree workspaceTree;
	private Application application;
	private String path;
	public Workspace(Application application, Tree tree){
		this.application = application;
		workspaceTree = tree;
		workspaceTree.addListener(SWT.MouseDoubleClick, new Listener(){

			@Override
			public void handleEvent(Event event) {
				Point point = new Point (event.x, event.y);
			    TreeItem item = workspaceTree.getItem (point);
			    if(item!=null){
			    	FileEntry fileEntry = (FileEntry) item.getData();
			    	if(fileEntry!=null){
			    		Workspace.this.application.addEditor(ResourceLoader.getImage("java.gif"), fileEntry.getPath());
			    	}
			    }
			}
			
		});
	}
	
	public void add(Project project){
		projectList.add(project);
		save();
		if(workspaceTree!=null){
			TreeItem projectItem = newProjectItem(project);
			projectItem.setData(project);
			
			add(project, projectItem);
			workspaceTree.setMenu(getMenu());
		}
	}
	
	public void save(){
		saveTo(path);
	}
	public void saveTo(String path){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			Element rootElement = document.createElement("projects");
			document.appendChild(rootElement);
			
			List<Project> projectList = getProjectList();
			for(Project project:projectList){
				Element projectElement = document.createElement("project");
				projectElement.setAttribute("name", project.getName());
				projectElement.setAttribute("path", project.getPath());
				rootElement.appendChild(projectElement);
			}
			
			TransformerFactory tff = TransformerFactory.newInstance();
			tff.setAttribute("indent-number", 4);
		    Transformer tf = tff.newTransformer();
		    DOMSource source = new DOMSource(document);
		    StreamResult result = new StreamResult(new File(path, CHM.PROJECTS_NAME));
		   
		    //设置输出编码
		    tf.setOutputProperty(OutputKeys.INDENT,"yes");
		    tf.setOutputProperty("encoding", "utf-8");
		    tf.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void add(FileEntry parent, TreeItem parentItem){
		List<FileEntry> entryList = parent.getEntryList();
		for(FileEntry entry : entryList){
			TreeItem entryItem = new TreeItem (parentItem, 0);
			entryItem.setText(entry.getName());
			entryItem.setImage(entry.isParent()?ResourceLoader.getImage("fldr_obj.gif"):ResourceLoader.getImage("jcu_obj.gif"));
			entryItem.setData(entry);
			if(entry.getEntryList().size()>0){
				add(entry, entryItem);
			}
		}
	}
	
	public TreeItem newProjectItem(Project project){
		TreeItem iItem = new TreeItem (workspaceTree, 0);
		iItem.setText (project.getName());
		
		if(project.isOpen()){
			iItem.setImage(ResourceLoader.getImage("projects.gif"));
		}else{
			iItem.setImage(ResourceLoader.getImage("cprj_obj.gif"));
		}
		
		return iItem;
	}
	
	public Menu getMenu(){
    	final Menu menu = new Menu (workspaceTree.getShell(), SWT.POP_UP);
    	//final Tree tree = workspaceTree;
    	menu.addListener (SWT.Show, new Listener () {
    		public void handleEvent (Event event) {
    			MenuItem [] menuItems = menu.getItems ();
    			for (int i=0; i<menuItems.length; i++) {
    				menuItems [i].dispose ();
    			}
    			
    			MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
				menuItem.setText ("New");
				menuItem.addSelectionListener(Workspace.this);
    		}
    	});
    	return menu;
	}
	public void widgetSelected(SelectionEvent e) {
		MenuItem item = (MenuItem)e.widget;
		if (item.getSelection ()) {
			System.out.println (item + " selected");
		} else {
			System.out.println (item + " unselected");
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
