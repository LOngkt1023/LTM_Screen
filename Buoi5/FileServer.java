import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    static String FILE_DIRECTORY = "files/";
    
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(6000);
        System.out.println("Port: 6000");
        System.out.println("Files directory: " + FILE_DIRECTORY);
        
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            System.out.println("Server IP: " + localHost.getHostAddress());
        } catch (Exception e) {
            System.out.println("Khong the lay IP cua server");
        }
        
        System.out.println("Dang cho client ket noi...");
        System.out.println("==============================");
        
        while (true) {
            Socket client = server.accept();
            System.out.println("Client ket noi: " + client.getInetAddress());
            ClientHandler handler = new ClientHandler(client);
            handler.start();
        }
    }
}

class ClientHandler extends Thread {
    Socket client;
    
    public ClientHandler(Socket client) {
        this.client = client;
    }
    
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            
            String fileName = dis.readUTF();
            System.out.println("Client yeu cau file: " + fileName);
            
            File file = new File(FileServer.FILE_DIRECTORY + fileName);
            
            if (!file.exists() || !file.isFile()) {
                dos.writeBoolean(false);
                dos.writeUTF("File khong ton tai!");
                System.out.println("File khong ton tai: " + fileName);
                client.close();
                return;
            }
            
            dos.writeBoolean(true);
            dos.writeLong(file.length());
            dos.writeUTF(fileName);
            
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalSent = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
                totalSent += bytesRead;
            }
            
            fis.close();
            dos.flush();
            System.out.println("Da gui file " + fileName + " (" + totalSent + " bytes)");
            
        } catch (Exception e) {
            System.out.println("Loi xu ly client: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }
}