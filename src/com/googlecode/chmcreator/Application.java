package com.googlecode.chmcreator;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.googlecode.chmcreator.bean.Workspace;
import com.googlecode.chmcreator.builder.MenubarBuilder;
import com.googlecode.chmcreator.builder.ToolBarBuilder;
import com.googlecode.chmcreator.builder.WorkspaceBuilder;
import com.googlecode.chmcreator.editor.HTMLEditor;

public class Application {

	public final Display display = Display.getDefault();
	public final Shell shell = new Shell(display);

	public CTabFolder tabEditor = null;
	
	public Tree workspaceTree;
	public Workspace workspace = null;
	
	public static Properties settings = new Properties();

	private WorkspaceBuilder workspaceBuilder = null;
	public Application(){
		loadSettings();//load settings
		
		createUI();//create UI
	}

	public void createUI(){
		shell.setLayout (new FillLayout());
		shell.setSize(display.getClientArea().width-60, display.getClientArea().height-60);
		setTitle(settings.getProperty("title"));
		
		//menu
		Menu menuBar = new MenubarBuilder(display, shell).build(this);
		shell.setMenuBar (menuBar);
		
		Composite app = new Composite(shell,SWT.NONE);
		app.setLayout(new FormLayout());

		//toolbar
		CoolBar coolBar = new ToolBarBuilder(app).build();

		//workspace
		Composite workspace = createWorkspace(app,coolBar);
		
		//framework
		SashForm framework = new SashForm(workspace,SWT.HORIZONTAL);
		framework.setLayout(new FillLayout());
		
		//navgator
		Composite navgator = new Composite(framework,SWT.NONE);
		navgator.setLayout(new FillLayout());
		addNavgatorTab(getImage("java.gif"),navgator, 1);
		
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
		
		workspacePath = settings.getProperty("workspace.path");
		workspaceBuilder = new WorkspaceBuilder(workspacePath);
		workspace = workspaceBuilder.build(this, workspaceTree);
		
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
	
	public void addNavgatorTab(final Image image, final Composite parent, int count){
		final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		folder.setBorderVisible(true);
		folder.setUnselectedImageVisible(true);
		folder.setUnselectedCloseVisible(true);
		
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Project Explore");
		item.setImage(getImage("workset.gif"));
		workspaceTree = new Tree (folder, SWT.BORDER);
		item.setControl(workspaceTree);
		
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
			item.setImage(getImage("console_view.gif"));
			Text text = new Text(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText("");
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
		File file = new File(fileName);
		if(!file.exists()||file.isDirectory())
			return;
		
		final CTabItem item = new CTabItem(tabEditor, SWT.CLOSE);
		item.setImage(image);
		item.setText(file.getName());
		item.setToolTipText(fileName);
		HTMLEditor htmlEditor = new HTMLEditor(tabEditor, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		htmlEditor.setPath(file.getParentFile().getAbsolutePath());
		try {
			htmlEditor.setContent(FileUtils.readFileToString(file, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		htmlEditor.addListener(SWT.Modify, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				if(item.getText().charAt(0)=='*')
					return;
				item.setText("*"+item.getText());
			}
			
		});
		item.setControl(htmlEditor);
		setTitle(file.getName());
		
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
	public synchronized void setTitle(String title){
		if(StringUtils.isBlank(title)){
			shell.setText("Chmcreator");
		}else{
			shell.setText("Chmcreator - " + title);
		}
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
}
