import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class DateTimeClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Đã kết nối đến DateTime Server");
            
            DataInputStream din = new DataInputStream(socket.getInputStream());
            String time = din.readUTF();
            System.out.println("Thời gian từ server: " + time);
            
            socket.close();
        } catch (IOException e) {
            System.err.println("Lỗi client: " + e.getMessage());
        }
    }
}
