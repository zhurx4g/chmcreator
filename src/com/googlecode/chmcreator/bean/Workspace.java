package com.googlecode.chmcreator.bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.googlecode.chmcreator.ResourceLoader;

public class Workspace implements SelectionListener {

	private List<Project> projectList = new ArrayList<Project>();
	private Tree workspaceTree;
	public Workspace(Tree tree){
		workspaceTree = tree;
	}
	
	public void add(Project project){
		projectList.add(project);
		if(workspaceTree!=null){
			TreeItem projectItem = newProjectItem(project.getName());
			projectItem.setData(project);
			
			workspaceTree.setMenu(getMenu());
		}
	}
	
	public TreeItem newProjectItem(String name){
		TreeItem iItem = new TreeItem (workspaceTree, 0);
		iItem.setText (name);
		iItem.setImage(ResourceLoader.getImage("projects.gif"));
		
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
