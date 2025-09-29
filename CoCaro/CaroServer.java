import java.io.*;
import java.net.*;
import java.util.*;

public class CaroServer {
    private static final int PORT = 5000;
    private static final int BOARD_SIZE = 15;
    
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE]; // 0: trống, 1: player 1, 2: player 2
    private int currentPlayer = 1; // 1 hoặc 2
    private boolean gameActive = false;
    private int playerCount = 0;
    
    public CaroServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Caro Server đã khởi động trên port " + PORT);
            System.out.println("Đang chờ client kết nối...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                clientHandler.start();
                
                System.out.println("Client mới kết nối. Tổng số client: " + clients.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void makeMove(int x, int y, ClientHandler player) {
        // Kiểm tra tính hợp lệ của nước đi
        if (!gameActive || x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE || board[x][y] != 0) {
            player.sendMessage("INVALID_MOVE");
            return;
        }
        
        // Kiểm tra lượt đi
        int playerNumber = getPlayerNumber(player);
        if (playerNumber != currentPlayer || playerNumber > 2) {
            player.sendMessage("NOT_YOUR_TURN");
            return;
        }
        
        // Thực hiện nước đi
        board[x][y] = currentPlayer;
        
        // Kiểm tra thắng thua
        boolean hasWinner = checkWin(x, y, currentPlayer);
        
        // Gửi thông tin nước đi cho tất cả client
        String moveMessage = "MOVE:" + x + "," + y + "," + currentPlayer;
        broadcastMessage(moveMessage);
        
        if (hasWinner) {
            broadcastMessage("GAME_OVER:WINNER:" + currentPlayer);
            gameActive = false;
            System.out.println("Game Over! Player " + currentPlayer + " thắng!");
        } else if (isBoardFull()) {
            broadcastMessage("GAME_OVER:DRAW");
            gameActive = false;
            System.out.println("Game Over! Hòa!");
        } else {
            // Chuyển lượt
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            broadcastMessage("TURN:" + currentPlayer);
        }
    }
    
    public synchronized void addPlayer(ClientHandler client) {
        if (playerCount < 2) {
            playerCount++;
            client.sendMessage("PLAYER_NUMBER:" + playerCount);
            System.out.println("Player " + playerCount + " đã tham gia game");
            
            // Gửi trạng thái bàn cờ hiện tại cho player mới
            sendBoardState(client);
            
            if (playerCount == 2) {
                gameActive = true;
                broadcastMessage("GAME_START");
                broadcastMessage("TURN:" + currentPlayer);
                System.out.println("Game bắt đầu!");
            }
        } else {
            client.sendMessage("SPECTATOR");
            // Gửi trạng thái bàn cờ hiện tại cho khán giả
            sendBoardState(client);
            System.out.println("Client tham gia với tư cách khán giả");
        }
    }
    
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        int playerNumber = getPlayerNumber(client);
        if (playerNumber <= 2) {
            playerCount--;
            gameActive = false;
            broadcastMessage("PLAYER_DISCONNECTED");
            resetGame();
            System.out.println("Player " + playerNumber + " đã rời game");
        }
        System.out.println("Client ngắt kết nối. Số client còn lại: " + clients.size());
    }
    
    private int getPlayerNumber(ClientHandler client) {
        int index = clients.indexOf(client);
        return (index >= 0 && index < 2) ? index + 1 : -1;
    }
    
    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
    
    private boolean checkWin(int x, int y, int player) {
        // Kiểm tra 4 hướng: ngang, dọc, chéo chính, chéo phụ
        int[] dx = {1, 0, 1, 1};
        int[] dy = {0, 1, 1, -1};
        
        for (int dir = 0; dir < 4; dir++) {
            int count = 1;
            
            // Đếm về một phía
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            while (nx >= 0 && nx < BOARD_SIZE && ny >= 0 && ny < BOARD_SIZE && board[nx][ny] == player) {
                count++;
                nx += dx[dir];
                ny += dy[dir];
            }
            
            // Đếm về phía ngược lại
            nx = x - dx[dir];
            ny = y - dy[dir];
            while (nx >= 0 && nx < BOARD_SIZE && ny >= 0 && ny < BOARD_SIZE && board[nx][ny] == player) {
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
    
    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void resetGame() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
        currentPlayer = 1;
        gameActive = false;
    }
    
    // Gửi trạng thái bàn cờ hiện tại cho client
    private void sendBoardState(ClientHandler client) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != 0) {
                    client.sendMessage("MOVE:" + i + "," + j + "," + board[i][j]);
                }
            }
        }
        
        // Gửi thông tin game hiện tại
        if (gameActive) {
            client.sendMessage("GAME_START");
            client.sendMessage("TURN:" + currentPlayer);
        }
    }
    
    public static void main(String[] args) {
        new CaroServer();
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private CaroServer server;
    private PrintWriter out;
    private BufferedReader in;
    
    public ClientHandler(Socket socket, CaroServer server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Thêm player vào game
            server.addPlayer(this);
            
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("MOVE:")) {
                    String[] parts = message.split(":");
                    String[] coords = parts[1].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    server.makeMove(x, y, this);
                }
            }
        } catch (IOException e) {
            System.out.println("Client ngắt kết nối");
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
