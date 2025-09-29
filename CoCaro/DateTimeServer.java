import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateTimeServer {
    public final static int daytimePort = 5000;
    
    public static void main(String[] args) {
        ServerSocket theServer;
        try {
            theServer = new ServerSocket(daytimePort);
            System.out.println("DateTime Server đang chạy trên port " + daytimePort);
            
            while (true) {
                Socket theConnection = theServer.accept();
                System.out.println("Kết nối từ: " + theConnection.getInetAddress());
                
                DataOutputStream dos = new DataOutputStream(
                    theConnection.getOutputStream());
                String time = new Date().toString();
                dos.writeUTF(time);
                theConnection.close();
            }
        } catch (IOException e) {
            System.err.println("Lỗi server: " + e.getMessage());
        }
    }
}
