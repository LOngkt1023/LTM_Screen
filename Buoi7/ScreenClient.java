package Buoi7;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ScreenClient extends JFrame {
	Socket soc;
	DataOutputStream dos;
	
	// Biến đo tốc độ đường truyền
	private long startTime = 0;
	private long totalBytes = 0;
	private int frameCount = 0;
	private double currentBandwidth = 0; // KB/s
	
	public static void main(String[] args) {
		new ScreenClient();
	}
	int off = 50;
	public ScreenClient() {
		this.setTitle("Share Screen - Speed: 0 KB/s");
		this.setSize(500, 400);
		this.setDefaultCloseOperation(3);
		try {
			soc = new Socket("localhost",2345);
			dos = new DataOutputStream(soc.getOutputStream());
			startTime = System.currentTimeMillis();
		} catch(Exception e) {
			System.exit(1);
		}

		this.setVisible(true);
	}

	public void paint(Graphics g) {
		try {
			long receiveStartTime = System.currentTimeMillis();
			DataInputStream bis = new DataInputStream(soc.getInputStream());
			int n = bis.readInt();
			byte tmp[] = bis.readNBytes(n);
			
			// Đo tốc độ đường truyền
			long receiveEndTime = System.currentTimeMillis();
			totalBytes += tmp.length;
			frameCount++;
			
			// Tính bandwidth trung bình mỗi 10 frame
			if (frameCount % 10 == 0 && receiveEndTime > startTime) {
				long timeDiff = receiveEndTime - startTime;
				currentBandwidth = (totalBytes / 1024.0) / (timeDiff / 1000.0); // KB/s
				
				// Gửi tốc độ về server
				dos.writeDouble(currentBandwidth);
				dos.flush();
				
				// Cập nhật title với thông tin tốc độ
				this.setTitle("Share Screen - Speed: " + (int)currentBandwidth + " KB/s");
				
				// Reset để đo chu kỳ mới
				startTime = receiveEndTime;
				totalBytes = 0;
			}
			
			ByteArrayInputStream bis1 = new ByteArrayInputStream(tmp);
			BufferedImage img1 = ImageIO.read(bis1);
			int w = this.getWidth()-2*off;
			int h = this.getHeight()-2*off;
			Image img2 = img1.getScaledInstance(w,h, Image.SCALE_SMOOTH);
			
			g.drawImage(img2, off, off, this.getWidth()-off, this.getHeight()-off, 
					0, 0, w,h, null);
			
			this.repaint();
		} catch (Exception e) {
		}
	}

}

