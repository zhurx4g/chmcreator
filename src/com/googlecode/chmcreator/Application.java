package com.googlecode.chmcreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Application {

	final Display display = new Display ();
	Shell shell = new Shell(display);

	public Application(){
		shell.setLayout (new FillLayout());
		shell.setSize(display.getClientArea().width-60, display.getClientArea().height-60);
		
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&File");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu);
		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
		
		Composite application = new Composite(shell,SWT.NONE);
		FormLayout layout = new FormLayout();
		application.setLayout(layout);

		//toolbar
		CoolBar coolBar = createBar(application);

		//workspace
		Composite workspace = createWorkspace(application,coolBar);
	    
		//framework
		SashForm framework = new SashForm(workspace,SWT.HORIZONTAL);
		framework.setLayout(new FillLayout());
		
		//navgator
		Composite navgator = new Composite(framework,SWT.NONE);
		navgator.setLayout(new FillLayout());
		addTab(getImage(display),navgator, 1);
		
		//editorArea
		SashForm editorArea = new SashForm(framework,SWT.VERTICAL);
		//editor
		Composite editorParent = new Composite(editorArea,SWT.NONE);
		editorParent.setLayout(new FillLayout());
		addTab(getImage(display),editorParent, 3);
		
		//console
		Composite consoleParent = new Composite(editorArea,SWT.NONE);
		consoleParent.setLayout(new FillLayout());
		addTab(getImage(display),consoleParent, 2);
		editorArea.setWeights(new int[] {70, 30});

		framework.setWeights(new int[] {20,80});
	}

	/**
	 * @param args
	 */
	public void start(String[] args) {
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	public static Image getImage(final Display display){
		Image image = new Image(display, 28, 28);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, 0, 28, 28);
		gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(3, 3, 22, 22);
		gc.dispose();
		return image;
	}
	
	static int itemCount;
	CoolItem createItem(CoolBar coolBar, int count) {
	    ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
	    for (int i = 0; i < count; i++) {
	        ToolItem item = new ToolItem(toolBar, SWT.PUSH);
	        item.setImage(getImage(display));
	        item.setToolTipText(itemCount++ +"");
	    }
	    toolBar.pack();
	    Point size = toolBar.getSize();
	    CoolItem item = new CoolItem(coolBar, SWT.NONE);
	    item.setControl(toolBar);
	    Point preferred = item.computeSize(size.x, size.y);
	    item.setPreferredSize(preferred);
	    return item;
	}
	public CoolBar createBar(final Composite application){
	    CoolBar coolBar = new CoolBar(application, SWT.NONE);
	    createItem(coolBar, 3);
	    createItem(coolBar, 2);
	    createItem(coolBar, 3);
	    createItem(coolBar, 4);
	    
	    return coolBar;
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
	public static void addTab(final Image image, final Composite parent, int count){
		final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		folder.setBorderVisible(true);
		folder.setUnselectedImageVisible(true);
		folder.setUnselectedCloseVisible(true);
		for (int i = 0; i < count; i++) {
			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText("Item "+i);
			item.setImage(image);
			Text text = new Text(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText("Text for item "+i+"\n\none, two, three\n\nabcdefghijklmnop");
			item.setControl(text);
		}
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
		folder.setSelection(0);
	}
}
