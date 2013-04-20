package com.googlecode.util.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import com.googlecode.util.LoopedStreams;

public class ConsoleTextArea extends JTextArea {
	public ConsoleTextArea(InputStream[] inStreams) {
		for(int i = 0; i < inStreams.length; ++i)
			startConsoleReaderThread(inStreams[i]);
	} // ConsoleTextArea()

	public ConsoleTextArea() throws IOException {
		final LoopedStreams ls = new LoopedStreams();

		PrintStream ps = new PrintStream(ls.getOutputStream());
		System.setOut(ps);
		System.setErr(ps);
		startConsoleReaderThread(ls.getInputStream());
	} // ConsoleTextArea()
	private void startConsoleReaderThread(InputStream inStream) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		new Thread(new Runnable() {
			public void run() {
				StringBuffer sb = new StringBuffer();
				try {
					String s;
					Document doc = getDocument();
					while((s = br.readLine()) != null) {
						boolean caretAtEnd = false;
						caretAtEnd = getCaretPosition() == doc.getLength() ?
							true : false;
						sb.setLength(0);
						append(sb.append(s).append('\n').toString());
						if(caretAtEnd)
							setCaretPosition(doc.getLength());
					}
				}
				catch(IOException e) {
					JOptionPane.showMessageDialog(null,
						"" + e);
					System.exit(1);
				}
			}
		}).start();
	}
} // ConsoleTextArea
