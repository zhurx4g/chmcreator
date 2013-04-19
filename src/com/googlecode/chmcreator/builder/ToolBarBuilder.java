package com.googlecode.chmcreator.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.googlecode.chmcreator.ResourceLoader;

public class ToolBarBuilder {

	private Composite application;
	
	public ToolBarBuilder(Composite application){
		this.application = application;
	}
	
	public CoolBar build(){
	    CoolBar coolBar = new CoolBar(application, SWT.NONE);
	    createFileBar(coolBar);
	    createBuildBar(coolBar);
	    //createItem(coolBar, 3);
	    //createItem(coolBar, 4);
	    
	    return coolBar;
	}
	CoolItem createFileBar(CoolBar coolBar) {
	    ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
	    
	    ToolItem newItem = newToolItem(toolBar, "new_wiz.gif","New");
        ToolItem saveItem = newToolItem(toolBar, "save_edit.gif","Save(Ctrl+S)");
        ToolItem saveAllItem = newToolItem(toolBar, "saveall_edit.gif","Save All(Shift+Ctrl+S)");//print_edit.gif
        ToolItem printItem = newToolItem(toolBar, "print_edit.gif","Print(Ctrl+P)");
        
	    toolBar.pack();
	    Point size = toolBar.getSize();
	    CoolItem item = new CoolItem(coolBar, SWT.NONE);
	    item.setControl(toolBar);
	    Point preferred = item.computeSize(size.x, size.y);
	    item.setPreferredSize(preferred);
	    return item;
	}
	CoolItem createBuildBar(CoolBar coolBar) {
	    ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
	    
        ToolItem buildItem = newToolItem(toolBar, "builder.gif","Build All(Ctrl+B)");
        ToolItem runItem = newToolItem(toolBar, "lrun_obj.gif","Run");
        //ToolItem printItem = newToolItem(toolBar, "print_edit.gif","Print(Ctrl+P)");
        
	    toolBar.pack();
	    Point size = toolBar.getSize();
	    CoolItem item = new CoolItem(coolBar, SWT.NONE);
	    item.setControl(toolBar);
	    Point preferred = item.computeSize(size.x, size.y);
	    item.setPreferredSize(preferred);
	    return item;
	}
	ToolItem newToolItem(ToolBar toolBar, String imageName, String toolTip){
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
        toolItem.setImage(ResourceLoader.getImage(imageName));
        toolItem.setToolTipText(toolTip);
        
        return toolItem;
	}
	
	CoolItem createItem(CoolBar coolBar, int count) {
	    ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
	    for (int i = 0; i < count; i++) {
	        ToolItem item = new ToolItem(toolBar, SWT.PUSH);
	        item.setImage(ResourceLoader.getImage("java.gif"));
	        item.setToolTipText("ToolBar TooTip!");
	    }
	    toolBar.pack();
	    Point size = toolBar.getSize();
	    CoolItem item = new CoolItem(coolBar, SWT.NONE);
	    item.setControl(toolBar);
	    Point preferred = item.computeSize(size.x, size.y);
	    item.setPreferredSize(preferred);
	    return item;
	}
}
