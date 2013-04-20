package com.googlecode.chmcreator.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Text;

public class ConsoleTextHelper {
	protected Log logger = LogFactory.getLog(getClass());
	private Text text;
	public ConsoleTextHelper(Text text) {
		this.text = text;
	}

	public synchronized void start(InputStream[] inStreams){
		for(int i = 0; i < inStreams.length; ++i)
			startConsoleReaderThread(inStreams[i]);
	}
	
	private synchronized void startConsoleReaderThread(InputStream inStream) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		new Thread(new Runnable() {
			public void run() {
				StringBuffer sb = new StringBuffer();
				try {
					String s;
					while((s = br.readLine()) != null) {
						sb.setLength(0);
						text.append(sb.append(s).append('\n').toString());
					}
				}
				catch(IOException e) {
					System.exit(1);
				}
				logger.info("Read Thread exit!");
			}
		}).start();
	}
}
