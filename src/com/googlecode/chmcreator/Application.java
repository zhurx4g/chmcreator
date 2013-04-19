package com.googlecode.chmcreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Application {

	public final static Display display = Display.getDefault();
	public final static Shell shell = new Shell(display);

	public static CTabFolder tabEditor = null;
	
	public static List<Project> projectList = new ArrayList<Project>();
	
	public static Properties settings = new Properties();

	public Application(){
		//load settings
		loadSettings();
		
		//create UI
		createUI();
	}

	public void createUI(){
		shell.setLayout (new FillLayout());
		shell.setSize(display.getClientArea().width-60, display.getClientArea().height-60);
		setTitle(settings.getProperty("title"));
		
		//menu
		Menu menuBar = createMenu();
		shell.setMenuBar (menuBar);
		
		Composite application = new Composite(shell,SWT.NONE);
		application.setLayout(new FormLayout());

		//toolbar
		CoolBar coolBar = new ToolBarBuilder(application).build();

		//workspace
		Composite workspace = createWorkspace(application,coolBar);
		
		//framework
		SashForm framework = new SashForm(workspace,SWT.HORIZONTAL);
		framework.setLayout(new FillLayout());
		
		//navgator
		Composite navgator = new Composite(framework,SWT.NONE);
		navgator.setLayout(new FillLayout());
		addTab(getImage("java.gif"),navgator, 1);
		
		//editorArea
		SashForm editorArea = new SashForm(framework,SWT.VERTICAL);
		//editor
		Composite editorParent = new Composite(editorArea,SWT.NONE);
		editorParent.setLayout(new FillLayout());
		tabEditor = createTabEditor(editorParent);
		
		//console
		Composite consoleParent = new Composite(editorArea,SWT.NONE);
		consoleParent.setLayout(new FillLayout());
		createConsoleTab(getImage("java.gif"),consoleParent, 1);
		editorArea.setWeights(new int[] {70, 30});

		framework.setWeights(new int[] {20,80});
	}
	/**
	 * @param args
	 */
	public void start(String[] args) {
		String workspacePath = settings.getProperty("workspace.path");
		if(StringUtils.isBlank(workspacePath)){
				switchWorkspace();
		}
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	public static Image getImage(String string) {
		return ResourceLoader.getImage(string);
	}

	public void switchWorkspace(){
		final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("Select a workspace");
		dialog.setLayout (new FillLayout(SWT.VERTICAL));

		Label label = new Label (dialog, SWT.NONE);
		label.setText ("\n    Select a workspace\n");
		label.setFont(new Font(display, label.getFont().getFontData()));
		label.setBackground(new Color(display,255,255,255));

		Label label2 = new Label (dialog, SWT.NONE);
		label2.setText ("    Eclipse stores your projects in a folder called a workspace.\n" +
				"    Choose a workspace folder to use for this session.");
		label2.setBackground(new Color(display,255,255,255));
		
		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		cancel.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				dialog.close ();
				System.exit(0);
			}
		});

		final Text text = new Text (dialog, SWT.BORDER);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println ("User typed: " + text.getText ());
				dialog.close ();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();
		while (!dialog.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
	}
	public Menu createMenu(){
		Menu bar = new Menu (shell, SWT.BAR);
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
					projectList.add(project);
					TreeItem iItem = new TreeItem (tree, 0);
					iItem.setText (file.getName());
					iItem.setImage(getImage("projects.gif"));
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
					addEditor(getImage("java.gif"), fileName);
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
		return bar;
	}
	
	public Composite createWorkspace(final Composite application, final Composite attachment){
	    int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
	    //Text text = new Text(application, style);
	    Composite workspace = new Composite(application,style);
	    workspace.setBackground(new Color(display,0,0,0));
	    FormData textData = new FormData();
	    textData.left = new FormAttachment(0);
	    textData.right = new FormAttachment(100);
	    textData.top = new FormAttachment(attachment);
	    textData.bottom = new FormAttachment(100);
	    workspace.setLayoutData(textData);
	    workspace.setLayout(new FillLayout());
	    
	    return workspace;
	}
	static Tree tree;
	public static void addTab(final Image image, final Composite parent, int count){
		final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		folder.setBorderVisible(true);
		folder.setUnselectedImageVisible(true);
		folder.setUnselectedCloseVisible(true);
		for (int i = 0; i < count; i++) {
			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText("Project Explore");
			item.setImage(getImage("workset.gif"));
			//Text text = new Text(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			//text.setText("ss");
			
			tree = new Tree (folder, SWT.BORDER);
			for (int ix=0; ix<4; ix++) {
				TreeItem iItem = new TreeItem (tree, 0);
				iItem.setText ("工程" + ix);
				iItem.setImage(getImage("projects.gif"));
				for (int j=0; j<4; j++) {
					TreeItem jItem = new TreeItem (iItem, 0);
					jItem.setText ("文件夹" + j);//
					jItem.setImage(getImage("fldr_obj.gif"));
					for (int k=0; k<4; k++) {
						TreeItem kItem = new TreeItem (jItem, 0);
						kItem.setText ("文件夹" + k);
						kItem.setImage(getImage("fldr_obj.gif"));
						for (int l=0; l<4; l++) {
							TreeItem lItem = new TreeItem (kItem, 0);
							lItem.setText ("文件" + l);
							lItem.setImage(getImage("jcu_obj.gif"));
						}
					}
				}
			}
			item.setControl(tree);
		}
		folder.setMinimizeVisible(true);
		folder.setTabHeight(30);
		folder.setMaximizeVisible(true);
		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void minimize(CTabFolderEvent event) {
				folder.setMinimized(true);
				parent.layout(true);
			}
			public void maximize(CTabFolderEvent event) {
				folder.setMaximized(true);
				parent.layout(true);
			}
			public void restore(CTabFolderEvent event) {
				folder.setMinimized(false);
				folder.setMaximized(false);
				parent.layout(true);
			}
		});
		folder.setSelection(0);
	}
	

	public static void createConsoleTab(final Image image, final Composite parent, int count){
		final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		folder.setBorderVisible(true);
		folder.setUnselectedImageVisible(true);
		folder.setUnselectedCloseVisible(true);
		for (int i = 0; i < count; i++) {
			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText("Console");
			//
			item.setImage(getImage("console_view.gif"));
			Text text = new Text(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText("Text for item "+i+"\n\none, two, three\n\nabcdefghijklmnop");
			item.setControl(text);
		}
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);
		folder.setTabHeight(30);
		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void minimize(CTabFolderEvent event) {
				folder.setMinimized(true);
				parent.layout(true);
			}
			public void maximize(CTabFolderEvent event) {
				folder.setMaximized(true);
				parent.layout(true);
			}
			public void restore(CTabFolderEvent event) {
				folder.setMinimized(false);
				folder.setMaximized(false);
				parent.layout(true);
			}
		});
		folder.setSelection(0);
	}
	public void addEditor(final Image image, String fileName){
		CTabItem item = new CTabItem(tabEditor, SWT.CLOSE);
		item.setImage(image);
		File file = new File(fileName);
		item.setText(file.getName());
		item.setToolTipText(fileName);
		Text text = new Text(tabEditor, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		try {
			text.setText(FileUtils.readFileToString(file, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle(file.getName());
		item.setControl(text);
		
		tabEditor.setSelection(item);
	}
	public static CTabFolder createTabEditor(final Composite parent){
		final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		folder.setBorderVisible(true);
		folder.setUnselectedImageVisible(true);
		folder.setUnselectedCloseVisible(true);
		folder.setTabHeight(30);
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);
		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void minimize(CTabFolderEvent event) {
				folder.setMinimized(true);
				parent.layout(true);
			}
			public void maximize(CTabFolderEvent event) {
				folder.setMaximized(true);
				parent.layout(true);
			}
			public void restore(CTabFolderEvent event) {
				folder.setMinimized(false);
				folder.setMaximized(false);
				parent.layout(true);
			}
		});
		return folder;
	}
	
	public static void loadSettings(){
		try {
			settings.load(FileUtils.openInputStream(new File("setting.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		settings.list(System.out);
	}
	public static synchronized void setTitle(String title){
		if(StringUtils.isBlank(title)){
			shell.setText("Chmcreator");
		}else{
			shell.setText("Chmcreator - " + title);
		}
	}
}
