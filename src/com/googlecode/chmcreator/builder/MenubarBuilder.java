package com.googlecode.chmcreator.builder;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.googlecode.chmcreator.Application;
import com.googlecode.chmcreator.ResourceLoader;
import com.googlecode.chmcreator.bean.Project;

public class MenubarBuilder {
	private Display display;
	private Shell shell;
	public MenubarBuilder(final Display display, final Shell shell){
		this.display = display;
		this.shell = shell;
	}

	public Menu build(Application application){
		Menu bar = new Menu (shell, SWT.BAR|SWT.EMBEDDED);
		bar.setData(application);
		fileMenu(bar);
		editorMenu(bar);
		searchMenu(bar);
		toolMenu(bar);
		helpMenu(bar);
		return bar;
	}
	
	private void fileMenu(final Menu bar){
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&File");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		
		MenuItem newProject = new MenuItem (submenu, SWT.PUSH);
		newProject.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					File file = new File(fileName);
					Project project = new Project(fileName,file.getName());
//					workspace.add(project);
//					TreeItem iItem = new TreeItem (workspaceTree, 0);
//					iItem.setText (file.getName());
//					iItem.setImage(getImage("projects.gif"));
				}
			}

		});
		newProject.setText ("&New Project\tCtrl+N");
		newProject.setAccelerator (SWT.MOD1 + 'N');
		
		MenuItem open = new MenuItem (submenu, SWT.PUSH);
		open.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				FileDialog dialog = new FileDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					Application application = (Application)bar.getData();
					
					if(application!=null)
						application.addEditor(ResourceLoader.getImage("java.gif"), fileName);
				}
			}
		});
		open.setText ("&Open\tCtrl+O");
		open.setAccelerator (SWT.MOD1 + 'O');
		
		MenuItem importDir = new MenuItem (submenu, SWT.PUSH);
		importDir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					Project project = new ProjectBuilder().build(fileName);
					Application applicaton = (Application)bar.getData();
					applicaton.getWorkspace().add(project);
				}
			}
		});
		importDir.setText ("&Import\tCtrl+I");
		importDir.setAccelerator (SWT.MOD1 + 'I');
		
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
	}
	private void editorMenu(Menu bar){
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&Edit");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		
		MenuItem newProject = new MenuItem (submenu, SWT.PUSH);
		newProject.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					File file = new File(fileName);
					Project project = new Project(fileName,file.getName());
//					workspace.add(project);
//					TreeItem iItem = new TreeItem (workspaceTree, 0);
//					iItem.setText (file.getName());
//					iItem.setImage(getImage("projects.gif"));
				}
			}

		});
		newProject.setText ("&New Project\tCtrl+N");
		newProject.setAccelerator (SWT.MOD1 + 'N');
		
		MenuItem open = new MenuItem (submenu, SWT.PUSH);
		open.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				FileDialog dialog = new FileDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					//addEditor(ResourceLoader.getImage("java.gif"), fileName);
				}
			}
		});
		open.setText ("&Open\tCtrl+O");
		open.setAccelerator (SWT.MOD1 + 'O');
		
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
	}
	
	private void searchMenu(Menu bar) {
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&Search");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		
		MenuItem newProject = new MenuItem (submenu, SWT.PUSH);
		newProject.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					File file = new File(fileName);
					Project project = new Project(fileName,file.getName());
//					workspace.add(project);
//					TreeItem iItem = new TreeItem (workspaceTree, 0);
//					iItem.setText (file.getName());
//					iItem.setImage(getImage("projects.gif"));
				}
			}

		});
		newProject.setText ("&New Project\tCtrl+N");
		newProject.setAccelerator (SWT.MOD1 + 'N');
		
		MenuItem open = new MenuItem (submenu, SWT.PUSH);
		open.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				FileDialog dialog = new FileDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					//addEditor(ResourceLoader.getImage("java.gif"), fileName);
				}
			}
		});
		open.setText ("&Open\tCtrl+O");
		open.setAccelerator (SWT.MOD1 + 'O');
		
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
	}
	private void toolMenu(Menu bar) {
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&Tool");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		
		MenuItem newProject = new MenuItem (submenu, SWT.PUSH);
		newProject.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					File file = new File(fileName);
					Project project = new Project(fileName,file.getName());
//					workspace.add(project);
//					TreeItem iItem = new TreeItem (workspaceTree, 0);
//					iItem.setText (file.getName());
//					iItem.setImage(getImage("projects.gif"));
				}
			}

		});
		newProject.setText ("&New Project\tCtrl+N");
		newProject.setAccelerator (SWT.MOD1 + 'N');
		
		MenuItem open = new MenuItem (submenu, SWT.PUSH);
		open.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				FileDialog dialog = new FileDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					//addEditor(ResourceLoader.getImage("java.gif"), fileName);
				}
			}
		});
		open.setText ("&Open\tCtrl+O");
		open.setAccelerator (SWT.MOD1 + 'O');
		
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
	}

	private void helpMenu(Menu bar) {
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&Help");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		
		MenuItem newProject = new MenuItem (submenu, SWT.PUSH);
		newProject.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					File file = new File(fileName);
					Project project = new Project(fileName,file.getName());
//					workspace.add(project);
//					TreeItem iItem = new TreeItem (workspaceTree, 0);
//					iItem.setText (file.getName());
//					iItem.setImage(getImage("projects.gif"));
				}
			}

		});
		newProject.setText ("&New Project\tCtrl+N");
		newProject.setAccelerator (SWT.MOD1 + 'N');
		
		MenuItem open = new MenuItem (submenu, SWT.PUSH);
		open.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				FileDialog dialog = new FileDialog (shell);
				String platform = SWT.getPlatform();
				dialog.setFilterPath (platform.equals("win32") || platform.equals("wpf") ? "c:\\" : "/");
				String fileName = dialog.open();
				if(StringUtils.isNotBlank(fileName)){
					//addEditor(ResourceLoader.getImage("java.gif"), fileName);
				}
			}
		});
		open.setText ("&Open\tCtrl+O");
		open.setAccelerator (SWT.MOD1 + 'O');
		
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
	}
}
