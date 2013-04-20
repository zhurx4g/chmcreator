package com.googlecode.chmcreator.editor;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipsian.swt.composer.HyperComposer;
import org.eclipsian.swt.composer.IDocumentStateListener;

import com.googlecode.chmcreator.ResourceLoader;

public class HTMLEditor extends Composite {
	protected Log logger = LogFactory.getLog(getClass());
	
	private CTabFolder folder;
	private StyledText styledText;
	private HyperComposer composer;
	private String path = "";
	private HTMLLineStyler lineStyler = null;
	
	enum State {
		NORMAL,
		TEXT_MODIFY,
		WYSWYG_MODIFY,
		MODIFY,
	}
	
	private volatile State state = State.NORMAL;
	
	final static List<String> KEYWORDS = new ArrayList<String>();
	static {
		KEYWORDS.add("<html>");
		KEYWORDS.add("</html>");
	}
	
	final CTabItem html;
	final CTabItem design;
	@SuppressWarnings("unused")
	public HTMLEditor(Composite parent, int arg1) {
		super(parent, arg1);
		this.setLayout(new FillLayout());
		folder = new CTabFolder(this, SWT.NORMAL|SWT.BOTTOM);
		
		html = new CTabItem(folder, SWT.NORMAL);
		html.setText("HTML");
		styledText = new StyledText(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		lineStyler = new HTMLLineStyler(styledText);
		styledText.addLineStyleListener(lineStyler);

		styledText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent arg0) {
				styledText.redraw();
				updateState(State.TEXT_MODIFY, false);
			}
			
		});
		html.setControl(styledText);
		
		design = new CTabItem(folder, SWT.NORMAL);
		design.setText("Design");
		Composite parentComposite = new Composite(folder, SWT.NORMAL);
		parentComposite.setLayout(new FormLayout());
		
		CoolBar coolBar = new CoolBar(parentComposite, SWT.NORMAL);
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
		ToolItem newItem1 = newItem(toolBar, "editor/bold.gif","New");
		ToolItem newItem2 = newItem(toolBar, "editor/italic.gif","New");
		ToolItem newItem3 = newItem(toolBar, "editor/underline.gif","New");
		ToolItem newItem4 = newItem(toolBar, "editor/fgcolor.gif","New");
		ToolItem newItem5 = newItem(toolBar, "editor/hlcolor.gif","New");//bgcolor
		ToolItem newItem6 = newItem(toolBar, "editor/bgcolor.gif","New");
		
		ToolItem newItem7 = newItem(toolBar, "editor/alignleft.gif","New");
		ToolItem newItem8 = newItem(toolBar, "editor/aligncenter.gif","New");
		ToolItem newItem9 = newItem(toolBar, "editor/alignright.gif","New");
		ToolItem newItem10 = newItem(toolBar, "editor/alignjustify.gif","New");
		
		ToolItem newItem11 = newItem(toolBar, "editor/indent.gif","New");
		ToolItem newItem12 = newItem(toolBar, "editor/outdent.gif","New");
		
		ToolItem newItem13 = newItem(toolBar, "editor/orderlist.gif","New");
		ToolItem newItem14 = newItem(toolBar, "editor/unorderlist.gif","New");
		
		ToolItem newItem15 = newItem(toolBar, "editor/subscript.gif","New");
		ToolItem newItem16 = newItem(toolBar, "editor/superscript.gif","New");
		
		ToolItem newItem17 = newItem(toolBar, "editor/incfont.gif","New");
		ToolItem newItem18 = newItem(toolBar, "editor/decfont.gif","New");
	    toolBar.pack();
	    Point size = toolBar.getSize();
	    CoolItem item = new CoolItem(coolBar, SWT.NONE);
	    item.setControl(toolBar);
	    Point preferred = item.computeSize(size.x, size.y);
	    item.setPreferredSize(preferred);
	    
		composer = new HyperComposer(parentComposite, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		FormData textData = new FormData();
		textData.left = new FormAttachment(0);
		textData.right = new FormAttachment(100);
		textData.top = new FormAttachment(coolBar);
		textData.bottom = new FormAttachment(100);

		composer.setLayoutData(textData);
		composer.setLayout(new FillLayout());
		//composer.setFlavour(HyperComposer.FLAVOUR_CSS);
		design.setControl(parentComposite);
		composer.setCursor(Cursor.TEXT_CURSOR);
		composer.addDocumentStateListener(new IDocumentStateListener(){

			@Override
			public void documentCreated() {
				
			}

			@Override
			public void documentModified() {
				updateState(State.WYSWYG_MODIFY, false);
			}
			
		});
		folder.setSelection(html);
		folder.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				CTabItem item = (CTabItem)event.item;
				if(html.equals(item)&&state.equals(State.WYSWYG_MODIFY)){
					logger.info("get content from composer");
					styledText.setText(composer.getContent());
					updateState(State.MODIFY);
				}else if(design.equals(item)&&state.equals(State.TEXT_MODIFY)){
					logger.info("get content from text");
					composer.setContent(styledText.getText());
					updateState(State.MODIFY);
				}
			}
			
		});
	}

	private void updateState(State state){
		updateState(state, true);
	}
	private void updateState(State state, boolean isInner){
		this.state = state;
		if(isInner)
			return;
		
		Listener[] listeners = getListeners(SWT.Modify);
		for(Listener listener:listeners){
			listener.handleEvent(null);
		}
	}
	
	public String getContent(){
		CTabItem item = folder.getSelection();
		if(html.equals(item)){
			return styledText.getText();
		}else if(design.equals(item)){
			return composer.getContent();
		}
		return StringUtils.EMPTY;
	}
	
	public void setContent(String content){
		styledText.setText(content);
		composer.setContent(content);
		updateState(State.NORMAL);
	}
	ToolItem newItem(ToolBar toolBar, String imageName, String toolTip){
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
        toolItem.setImage(ResourceLoader.getImage(imageName));
        toolItem.setToolTipText(toolTip);
        
        return toolItem;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
