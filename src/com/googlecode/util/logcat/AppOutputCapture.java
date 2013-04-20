package com.googlecode.util.logcat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class AppOutputCapture {
	private static Process process;

	public static void main(String[] args) {
		// if(args.length == 0) {
		// System.exit(0);
		// }

		try {
			process = Runtime.getRuntime().exec("curl http://www.163.com");
		} catch (IOException e) {
			System.exit(1);
		}

		InputStream[] inStreams = new InputStream[] { process.getInputStream(), process.getErrorStream() };
		ConsoleTextArea cta = new ConsoleTextArea(inStreams);
		cta.setFont(java.awt.Font.decode("monospaced"));

		JFrame frame = new JFrame("xxx");

		frame.getContentPane().add(new JScrollPane(cta), BorderLayout.CENTER);
		frame.setBounds(50, 50, 400, 400);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				process.destroy();
				try {
					process.waitFor(); // ��Win98�¿��ܱ�����
				} catch (InterruptedException e) {
				}
				System.exit(0);
			}
		});
	} // main()
} // AppOutputCapture
