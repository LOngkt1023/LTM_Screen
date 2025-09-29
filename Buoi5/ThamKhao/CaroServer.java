package Buoi5;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class CaroServer {
	static Vector<xuly> clients = new Vector<xuly>();
	static Vector<Point> dadanh = new Vector<Point>();
	static int n = 15;
	public static void main(String args[]) throws Exception{
		ServerSocket server = new ServerSocket(5000);
		while (true) {
			Socket soc = server.accept();
			xuly t = new xuly(soc);
			clients.add(t);
			t.start();
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
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			for (Point p : CaroServer.dadanh) {
				dos.writeUTF(p.x+"");
				dos.writeUTF(p.y+"");
			}
		}catch(Exception e) {
			
		}
		while(true) {
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				
				//Kiem tra tinh hop le
				// Co phai 2 nguoi ket noi dau khong
				if (this!=CaroServer.clients.get(0) && this!=CaroServer.clients.get(1)) continue;
				
				// co phai luot danh cua ban khong
				if (this==CaroServer.clients.get(0) && CaroServer.dadanh.size()%2!=0) continue;
				if (this==CaroServer.clients.get(1) && CaroServer.dadanh.size()%2!=1) continue;
		
				// Check trung o
				boolean trung = false;
				for (Point p:CaroServer.dadanh) {
					if (p.x==ix && p.y==iy) {
						trung = true;
					}
				}
				if (trung) continue;
				CaroServer.dadanh.add(new Point(ix,iy));
				for (xuly x:CaroServer.clients) {
					try {
						DataOutputStream dos1 = new DataOutputStream(x.soc.getOutputStream());
						dos1.writeUTF(""+ix);
						dos1.writeUTF(""+iy);
					}catch(Exception e) {
						
					}
				}
				
			}catch(Exception e) {
				
			}
		}
	}
}
