package com.googlecode.chmcreator.bean;

import java.util.ArrayList;
import java.util.List;

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

import com.googlecode.chmcreator.Application;
import com.googlecode.chmcreator.ResourceLoader;

public class Workspace implements SelectionListener {

	private List<Project> projectList = new ArrayList<Project>();
	private Tree workspaceTree;
	private Application application;
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
		if(workspaceTree!=null){
			TreeItem projectItem = newProjectItem(project);
			projectItem.setData(project);
			
			add(project, projectItem);
			workspaceTree.setMenu(getMenu());
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
}
