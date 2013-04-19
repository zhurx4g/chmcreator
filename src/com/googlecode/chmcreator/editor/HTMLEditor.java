package com.googlecode.chmcreator.editor;

import java.awt.Cursor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipsian.swt.composer.HyperComposer;

public class HTMLEditor extends Composite {
	private CTabFolder folder;
	private Text text;
	private HyperComposer composer;
	public HTMLEditor(Composite parent, int arg1) {
		super(parent, arg1);
		this.setLayout(new FillLayout());
		folder = new CTabFolder(this, SWT.NORMAL|SWT.BOTTOM);
		
		CTabItem html = new CTabItem(folder, SWT.NORMAL);
		html.setText("HTML");
		text = new Text(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		html.setControl(text);
		
		CTabItem design = new CTabItem(folder, SWT.NORMAL);
		design.setText("Design");
		composer = new HyperComposer(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		design.setControl(composer);
		composer.setCursor(Cursor.TEXT_CURSOR);
		
		folder.setSelection(html);
	}

	public void setContent(String content){
		text.setText(content);
		composer.setContent(content);
	}
}
