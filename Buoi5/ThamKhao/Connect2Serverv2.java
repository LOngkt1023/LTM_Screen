package Buoi4;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;



public class Connect2Serverv2 {

	public static void main(String[] args) {
		try {
			//wifi: WebTuan
			//pass: machinelearning0705
			Socket soc = new Socket("192.168.10.62", 3000);
			
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			System.out.println(dis.readUTF());
			
		} catch (Exception e) {
			System.out.println("Error!!");
		}
	}
}
