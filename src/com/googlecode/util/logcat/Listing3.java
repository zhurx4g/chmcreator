package com.googlecode.util.logcat;


import java.io.*;

public class Listing3 {
	static PipedInputStream pipedIS =
		new PipedInputStream();
	static PipedOutputStream pipedOS =
		new PipedOutputStream();

	public static void main(String[] args) {
		try {
			pipedIS.connect(pipedOS);
		}
		catch(IOException e) {
			System.err.println("����ʧ��");
			System.exit(1);
		}

		byte[] inArray = new byte[10];
		int bytesRead = 0;

		// ����д�����߳�
		startWriterThread();

		try {
			bytesRead = pipedIS.read(inArray, 0, 10);
			while(bytesRead != -1) {
				System.out.println("�Ѿ���ȡ" +
					bytesRead + "�ֽ�...");
				bytesRead = pipedIS.read(inArray, 0, 10);
			}
		}
		catch(IOException e) {
			System.err.println("��ȡ�������.");
			System.exit(1);
		}
	} // main()

    // ����һ���������߳�
    // ִ��д��PipedOutputStream�Ĳ���
	private static void startWriterThread() {
		new Thread(new Runnable() {
			public void run() {
				byte[] outArray = new byte[2000];

				while(true) { // ����ֹ������ѭ��
					try {
						// �ڸ��߳�����֮ǰ�������1024�ֽڵ���ݱ�д��
						pipedOS.write(outArray, 0, 2000);
					}
					catch(IOException e) {
						System.err.println("д��������");
						System.exit(1);
					}
					System.out.println("	 �Ѿ�����2000�ֽ�...");
				}
			}
		}).start();
	} // startWriterThread()
} // Listing3
