package com.googlecode.chmcreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class X {

    public static void main(String[] args) {
    	Display display = new Display ();
    	Shell shell = new Shell (display);
    	final Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI);
    	final Menu menu = new Menu (shell, SWT.POP_UP);
    	tree.setMenu (menu);
    	for (int i=0; i<12; i++) {
    		TreeItem treeItem = new TreeItem (tree, SWT.NONE);
    		treeItem.setText ("Item " + i);
    		MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
    		menuItem.setText (treeItem.getText ());
    	}
    	menu.addListener (SWT.Show, new Listener () {
    		public void handleEvent (Event event) {
    			MenuItem [] menuItems = menu.getItems ();
    			TreeItem [] treeItems = tree.getSelection ();
    			for (int i=0; i<menuItems.length; i++) {
    				String text = menuItems [i].getText ();
    				int index = 0;
    				while (index<treeItems.length) {
    					if (treeItems [index].getText ().equals (text)) break;
    					index++;
    				}
    				menuItems [i].setEnabled (index != treeItems.length);
    			}
    		}
    	});
    	Rectangle clientArea = shell.getClientArea ();
    	tree.setBounds (clientArea.x, clientArea.y, 200, 200);
    	shell.setSize (300, 300);
    	shell.open ();
    	while (!shell.isDisposed ()) {
    		if (!display.readAndDispatch ()) display.sleep ();
    	}
    	display.dispose ();
    }
}
