package com.googlecode.chmcreator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class ResourceLoader {
	
	public static Map<String, Image> imagesMap = new ConcurrentHashMap<String, Image>();
	
	public static Image getImage(String fileName){
		Image img = imagesMap.get(fileName);
		if(img!=null)
			return img;
		ImageLoader loader = new ImageLoader();
		img = new Image(Display.getDefault(),loader.load(Application.class.getResourceAsStream("/"+ fileName))[0]);
			imagesMap.put(fileName, img);
		return img;
	}
}
