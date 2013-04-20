package com.googlecode.util.logcat;


import java.io.*;
public class Listing2 {
	static PipedInputStream pipedIS = new PipedInputStream();
	static PipedOutputStream pipedOS = 
		new PipedOutputStream();

	public static void main(String[] a){
		try {
			pipedIS.connect(pipedOS);
		}
		catch(IOException e) {
			System.err.println("����ʧ��");
				System.exit(1);
			}

		byte[] inArray	= new byte[10];
		byte[] outArray = new byte[20];
		int bytesRead = 0;

		try {
            // ��pipedOS����20�ֽ����
			pipedOS.write(outArray, 0, 20);
			System.out.println("	 �ѷ���20�ֽ�...");

           // ��ÿһ��ѭ������У�����10�ֽ�
           // ����20�ֽ�
			bytesRead = pipedIS.read(inArray, 0, 10);
            int i=0;
			while(bytesRead != -1) {
				pipedOS.write(outArray, 0, 20);
				System.out.println("	 �ѷ���20�ֽ�..."+i);
				i++;
				bytesRead = pipedIS.read(inArray, 0, 10);
			}
		}
		catch(IOException e) {
				System.err.println("��ȡpipedISʱ���ִ���: " + e);
				System.exit(1);
		}
	} // main()
}
