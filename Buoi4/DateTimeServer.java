package Buoi4;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateTimeServer {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(5000);
			while (true) {
				try {
					Socket soc = server.accept();
					xuly t = new xuly(soc);
					t.start();
				} catch (Exception e1) {
				}
			}
		} catch (Exception e) {
		}
	}
}

class xuly extends Thread{
	Socket soc;
	public xuly(Socket soc) {
		this.soc = soc;
	}
	public void run() {
		try {
			System.out.println("Connect from:" + soc.getInetAddress().getHostAddress());
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF("Gio cua thay Tuan:" + new Date().toString());
			soc.close();
		} catch (Exception e1) {
		}
	}
}
