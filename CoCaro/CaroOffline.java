import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JFrame;

public class CaroOffline extends JFrame implements MouseListener{

	public static void main(String[] args) {
		new CaroOffline();
	}
	int off = 50;
	int n = 15;
	int s = 30;
	Vector<Point> dadanh = new Vector<Point>();
	boolean gameOver = false;
	String winner = "";
	int currentPlayer = 0; // 0 = O, 1 = X
	public CaroOffline() {
		this.setTitle("Game Caro - Người chơi O đi trước");
		this.setSize(off*2+n*s, off*2+n*s+50);
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setVisible(true);
	}
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		
		// Vẽ bàn cờ
		for (int i = 0; i<=n;i++) {
			g.drawLine(off, off+s*i, off+s*n, off+s*i);
			g.drawLine(off+s*i, off, off+s*i, off+s*n);
		}
		
		// Vẽ các quân cờ
		g.setFont(new Font("Arial",Font.BOLD,s-5));
		for (int i = 0; i < dadanh.size();i++) {
			int ix = dadanh.get(i).x;
			int iy = dadanh.get(i).y;
			int x = off+ix*s + s/2 - s/4;
			int y = off+iy*s + s/2 + s/4;
			String str = (i%2==0) ? "O" : "X";
			
			// Đổi màu cho quân cờ
			if (i%2==0) {
				g.setColor(Color.BLUE);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(str, x, y);
		}
		
		// Hiển thị thông tin game
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,16));
		if (gameOver) {
			g.drawString("Game Over! " + winner + " thắng!", 10, off + n*s + 25);
			g.drawString("Click để chơi lại", 10, off + n*s + 45);
		} else {
			String currentPlayerStr = (currentPlayer == 0) ? "O" : "X";
			g.drawString("Lượt của: " + currentPlayerStr, 10, off + n*s + 25);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// Nếu game đã kết thúc, reset game
		if (gameOver) {
			resetGame();
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		
		if (x<off || x>=off+s*n) return;
		if (y<off || y>=off+s*n) return;
		
		int ix = (x-off)/s;
		int iy = (y-off)/s;
		
		// Kiểm tra ô đã được đánh chưa
		for (Point p :dadanh) {
			if (p.x==ix && p.y ==iy) return;
		}
		
		dadanh.add(new Point(ix,iy));
		
		// Kiểm tra thắng thua
		if (checkWin(ix, iy)) {
			gameOver = true;
			winner = (currentPlayer == 0) ? "Người chơi O" : "Người chơi X";
		} else if (dadanh.size() == n*n) {
			gameOver = true;
			winner = "Hòa";
		}
		
		// Chuyển lượt
		currentPlayer = 1 - currentPlayer;
		
		// Cập nhật title
		if (!gameOver) {
			String nextPlayer = (currentPlayer == 0) ? "O" : "X";
			this.setTitle("Game Caro - Lượt của " + nextPlayer);
		}
		
		this.repaint();
	}
	
	// Phương thức kiểm tra thắng thua
	private boolean checkWin(int x, int y) {
		int player = dadanh.size() - 1; // Người chơi vừa đi
		char playerChar = (player % 2 == 0) ? 'O' : 'X';
		
		// Kiểm tra 4 hướng: ngang, dọc, chéo chính, chéo phụ
		int[] dx = {1, 0, 1, 1};
		int[] dy = {0, 1, 1, -1};
		
		for (int dir = 0; dir < 4; dir++) {
			int count = 1; // Đếm quân hiện tại
			
			// Đếm về một phía
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			while (nx >= 0 && nx < n && ny >= 0 && ny < n && hasPlayerAt(nx, ny, playerChar)) {
				count++;
				nx += dx[dir];
				ny += dy[dir];
			}
			
			// Đếm về phía ngược lại
			nx = x - dx[dir];
			ny = y - dy[dir];
			while (nx >= 0 && nx < n && ny >= 0 && ny < n && hasPlayerAt(nx, ny, playerChar)) {
				count++;
				nx -= dx[dir];
				ny -= dy[dir];
			}
			
			if (count >= 5) {
				return true;
			}
		}
		
		return false;
	}
	
	// Kiểm tra có quân của người chơi tại vị trí (x, y) không
	private boolean hasPlayerAt(int x, int y, char player) {
		for (int i = 0; i < dadanh.size(); i++) {
			Point p = dadanh.get(i);
			if (p.x == x && p.y == y) {
				char playerAtPos = (i % 2 == 0) ? 'O' : 'X';
				return playerAtPos == player;
			}
		}
		return false;
	}
	
	// Reset game
	private void resetGame() {
		dadanh.clear();
		gameOver = false;
		winner = "";
		currentPlayer = 0;
		this.setTitle("Game Caro - Người chơi O đi trước");
	}
	
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
