import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CaroClient extends JFrame implements MouseListener {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    int off = 50;
    int n = 15;
    int s = 30;
    Vector<Point> dadanh = new Vector<Point>();
    Vector<Integer> players = new Vector<Integer>(); // Lưu thông tin player nào đánh (1 hoặc 2)
    
    private int myPlayerNumber = -1; // Số thứ tự của player này (1, 2, hoặc -1 nếu là khán giả)
    private int currentTurn = 1; // Lượt hiện tại
    private boolean gameActive = false;
    private String gameStatus = "Đang kết nối...";
    
    public CaroClient() {
        this.setTitle("Game Caro Online - Client");
        this.setSize(off*2+n*s, off*2+n*s+100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addMouseListener(this);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        // Kết nối đến server
        connectToServer();
        
        this.setVisible(true);
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Tạo thread để nhận tin nhắn từ server
            Thread messageReceiver = new Thread(this::receiveMessages);
            messageReceiver.start();
            
            gameStatus = "Đã kết nối. Đang chờ...";
            repaint();
            
        } catch (IOException e) {
            gameStatus = "Không thể kết nối đến server!";
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến server!");
            e.printStackTrace();
        }
    }
    
    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                processServerMessage(message);
            }
        } catch (IOException e) {
            gameStatus = "Mất kết nối với server!";
            repaint();
        }
    }
    
    private void processServerMessage(String message) {
        if (message.startsWith("PLAYER_NUMBER:")) {
            myPlayerNumber = Integer.parseInt(message.split(":")[1]);
            gameStatus = "Bạn là Player " + myPlayerNumber + ". Đang chờ đối thủ...";
            this.setTitle("Game Caro Online - Player " + myPlayerNumber);
        } 
        else if (message.equals("SPECTATOR")) {
            myPlayerNumber = -1;
            gameStatus = "Bạn là khán giả";
            this.setTitle("Game Caro Online - Khán giả");
        }
        else if (message.equals("GAME_START")) {
            gameActive = true;
            gameStatus = "Game bắt đầu!";
        }
        else if (message.startsWith("TURN:")) {
            currentTurn = Integer.parseInt(message.split(":")[1]);
            if (gameActive) {
                if (currentTurn == myPlayerNumber) {
                    gameStatus = "Đến lượt bạn!";
                } else {
                    gameStatus = "Đến lượt Player " + currentTurn;
                }
            }
        }
        else if (message.startsWith("MOVE:")) {
            String[] parts = message.split(":");
            String[] coords = parts[1].split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int player = Integer.parseInt(coords[2]);
            
            dadanh.add(new Point(x, y));
            players.add(player);
        }
        else if (message.startsWith("GAME_OVER:")) {
            gameActive = false;
            if (message.contains("WINNER:")) {
                int winner = Integer.parseInt(message.split(":")[2]);
                gameStatus = "Game Over! Player " + winner + " thắng!";
            } else if (message.contains("DRAW")) {
                gameStatus = "Game Over! Hòa!";
            }
        }
        else if (message.equals("PLAYER_DISCONNECTED")) {
            gameActive = false;
            gameStatus = "Đối thủ đã rời game";
            dadanh.clear();
            players.clear();
        }
        else if (message.equals("INVALID_MOVE")) {
            gameStatus = "Nước đi không hợp lệ!";
        }
        else if (message.equals("NOT_YOUR_TURN")) {
            gameStatus = "Chưa đến lượt của bạn!";
        }
        
        repaint();
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        
        // Vẽ bàn cờ
        for (int i = 0; i <= n; i++) {
            g.drawLine(off, off + s * i, off + s * n, off + s * i);
            g.drawLine(off + s * i, off, off + s * i, off + s * n);
        }
        
        // Vẽ các quân cờ
        g.setFont(new Font("Arial", Font.BOLD, s - 5));
        for (int i = 0; i < dadanh.size(); i++) {
            int ix = dadanh.get(i).x;
            int iy = dadanh.get(i).y;
            int x = off + ix * s + s / 2 - s / 4;
            int y = off + iy * s + s / 2 + s / 4;
            
            int player = players.get(i);
            String str = (player == 1) ? "O" : "X";
            
            // Đổi màu cho quân cờ
            if (player == 1) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(str, x, y);
        }
        
        // Hiển thị thông tin game
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Trạng thái: " + gameStatus, 10, off + n * s + 25);
        
        if (myPlayerNumber > 0) {
            g.drawString("Bạn là Player " + myPlayerNumber + " (" + 
                        (myPlayerNumber == 1 ? "O - Xanh" : "X - Đỏ") + ")", 10, off + n * s + 45);
        } else if (myPlayerNumber == -1) {
            g.drawString("Bạn đang xem game", 10, off + n * s + 45);
        }
        
        if (gameActive && currentTurn == myPlayerNumber) {
            g.setColor(Color.GREEN);
            g.drawString(">>> Đến lượt bạn! <<<", 10, off + n * s + 65);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Chỉ cho phép đánh khi đến lượt và là player chính thức
        if (!gameActive || currentTurn != myPlayerNumber || myPlayerNumber <= 0) {
            return;
        }
        
        int x = e.getX();
        int y = e.getY();
        
        if (x < off || x >= off + s * n) return;
        if (y < off || y >= off + s * n) return;
        
        int ix = (x - off) / s;
        int iy = (y - off) / s;
        
        // Kiểm tra ô đã được đánh chưa
        for (Point p : dadanh) {
            if (p.x == ix && p.y == iy) return;
        }
        
        // Gửi nước đi đến server
        out.println("MOVE:" + ix + "," + iy);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    public static void main(String[] args) {
        new CaroClient();
    }
}
