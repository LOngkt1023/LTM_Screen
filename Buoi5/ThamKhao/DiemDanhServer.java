package Buoi4;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DiemDanhServer extends JFrame implements Runnable {
	JTextArea ta;
	Map<String, String> m = new HashMap<String, String>();

	public static void main(String[] args) {
		new DiemDanhServer();
	}

	public DiemDanhServer() {
		this.setTitle("DiemDanh");
		this.setSize(400, 500);
		this.setDefaultCloseOperation(3);
		this.setLayout(new BorderLayout());
		ta = new JTextArea();
		this.add(ta, BorderLayout.CENTER);

		this.setVisible(true);
		Thread tt = new Thread(this);
		tt.start();
		try {
			ServerSocket server = new ServerSocket(3000);
			while (true) {
				Socket soc = server.accept();
				ClientProcessing t = new ClientProcessing(soc, this);
				t.start();
			}
		} catch (Exception e) {

		}
	}

	class ClientProcessing extends Thread {
		Socket soc;
		DiemDanhServer server;

		public ClientProcessing(Socket soc, DiemDanhServer server) {
			this.soc = soc;
			this.server = server;
		}

		public void run() {
			try {
				soc.setSoTimeout(200);
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				String msg = "Hay gui theo cu phap:\n";
				msg += "Diem danh!! MSSV\n";
				msg += "Voi MSSV la ma so sinh vien cua ban!!";
				dos.writeUTF(msg);
				String receive = dis.readUTF();
				if (receive.substring(0, "Diem danh!! ".length()).equals("Diem danh!! "))
					server.m.put(soc.getInetAddress().getHostAddress(), receive.substring("Diem danh!! ".length()));
				soc.close();
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void run() {
		while (true) {
			String ans = "";
			for (String s : m.keySet()) {
				ans += m.get(s) + ";" + s + "\n";
			}
			ta.setText(ans);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
