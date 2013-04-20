package com.googlecode.chmcreator.editor;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipsian.swt.composer.HyperComposer;

import com.googlecode.chmcreator.ResourceLoader;

public class HTMLEditor extends Composite {
	private CTabFolder folder;
	private StyledText styledText;
	private HyperComposer composer;
	private String path = "";
	private HTMLLineStyler lineStyler = new HTMLLineStyler();
	final static List<String> KEYWORDS = new ArrayList<String>();
	static {
		KEYWORDS.add("<html>");
		KEYWORDS.add("</html>");
	}
	public HTMLEditor(Composite parent, int arg1) {
		super(parent, arg1);
		this.setLayout(new FillLayout());
		folder = new CTabFolder(this, SWT.NORMAL|SWT.BOTTOM);
		
		CTabItem html = new CTabItem(folder, SWT.NORMAL);
		html.setText("HTML");
		styledText = new StyledText(folder, SWT.BORDER|SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		styledText.addLineStyleListener(lineStyler);
//		styledText.addLineStyleListener(new LineStyleListener()
//		{
//		    public void lineGetStyle(LineStyleEvent e)
//		    {
//		    	int lineNumber = styledText.getLineAtOffset(e.lineOffset);
//		        //Set the line number
//		        e.bulletIndex = lineNumber;
//		        //Set the style, 12 pixles wide for each digit
//		        StyleRange style = new StyleRange();
//		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(styledText.getLineCount()+1).length()*12);
//		        //Create and set the bullet
//		        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
//		    }
//		});
		html.setControl(styledText);
		
		CTabItem design = new CTabItem(folder, SWT.NORMAL);
		design.setText("Design");
		Composite parentComposite = new Composite(folder, SWT.NORMAL);
		parentComposite.setLayout(new FormLayout());
		
		CoolBar coolBar = new CoolBar(parentComposite, SWT.NORMAL);
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
		ToolItem newItem1 = newToolItem(toolBar, "editor/bold.gif","New");
		ToolItem newItem2 = newToolItem(toolBar, "editor/italic.gif","New");
		ToolItem newItem3 = newToolItem(toolBar, "editor/underline.gif","New");
		ToolItem newItem4 = newToolItem(toolBar, "editor/fgcolor.gif","New");
		ToolItem newItem5 = newToolItem(toolBar, "editor/hlcolor.gif","New");//bgcolor
		ToolItem newItem6 = newToolItem(toolBar, "editor/bgcolor.gif","New");
		
		ToolItem newItem7 = newToolItem(toolBar, "editor/alignleft.gif","New");
		ToolItem newItem8 = newToolItem(toolBar, "editor/aligncenter.gif","New");
		ToolItem newItem9 = newToolItem(toolBar, "editor/alignright.gif","New");
		ToolItem newItem10 = newToolItem(toolBar, "editor/alignjustify.gif","New");
		
		ToolItem newItem11 = newToolItem(toolBar, "editor/indent.gif","New");
		ToolItem newItem12 = newToolItem(toolBar, "editor/outdent.gif","New");
		
		ToolItem newItem13 = newToolItem(toolBar, "editor/orderlist.gif","New");
		ToolItem newItem14 = newToolItem(toolBar, "editor/unorderlist.gif","New");
		
		ToolItem newItem15 = newToolItem(toolBar, "editor/subscript.gif","New");
		ToolItem newItem16 = newToolItem(toolBar, "editor/superscript.gif","New");
		
		ToolItem newItem17 = newToolItem(toolBar, "editor/incfont.gif","New");
		ToolItem newItem18 = newToolItem(toolBar, "editor/decfont.gif","New");
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
//		composer.setURIResolver(new IURIResolver(){
//			@Override
//			public URI resolve(URI arg0) {
//				URI uri = URI.create(HTMLEditor.this.getPath()+"/"+arg0.getPath());
//				System.out.println("===============" + uri);
//				return uri;
//			}
//			
//		});
		composer.setLayoutData(textData);
		composer.setLayout(new FillLayout());
		
		design.setControl(parentComposite);
		composer.setCursor(Cursor.TEXT_CURSOR);
		
		folder.setSelection(html);
	}

	public void setContent(String content){
		//lineStyler.parseBlockComments(content);
		styledText.setText(content);
		composer.setContent(content);
	}
	ToolItem newToolItem(ToolBar toolBar, String imageName, String toolTip){
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
