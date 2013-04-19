package com.googlecode.chmcreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolBarBuilder {

	private Composite application;
	
	public ToolBarBuilder(Composite application){
		this.application = application;
	}
	
	public CoolBar build(){
	    CoolBar coolBar = new CoolBar(application, SWT.NONE);
	    createItem(coolBar, 3);
	    createItem(coolBar, 2);
	    createItem(coolBar, 3);
	    createItem(coolBar, 4);
	    
	    return coolBar;
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
