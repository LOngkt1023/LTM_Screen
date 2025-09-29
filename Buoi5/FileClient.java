import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhap dia chi IP cua server: ");
            String serverIP = scanner.nextLine().trim();
            if (serverIP.isEmpty()) {
                serverIP = "localhost";
            }
            
            Socket socket = new Socket(serverIP, 6000);
            System.out.println("Ket noi thanh cong den server: " + serverIP);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            System.out.print("Nhap ten file can tai: ");
            String fileName = scanner.nextLine();
            
            dos.writeUTF(fileName);
            
            boolean fileExists = dis.readBoolean();
            
            if (!fileExists) {
                String errorMsg = dis.readUTF();
                System.out.println("Loi: " + errorMsg);
                socket.close();
                return;
            }
            
            long fileSize = dis.readLong();
            String receivedFileName = dis.readUTF();
            
            System.out.println("Bat dau tai file: " + receivedFileName);
            System.out.println("Kich thuoc file: " + fileSize + " bytes");
            
            String downloadPath = "downloads/" + receivedFileName;
            
            java.io.File downloadDir = new java.io.File("downloads");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
            
            FileOutputStream fos = new FileOutputStream(downloadPath);
            byte[] buffer = new byte[8192];
            long totalReceived = 0;
            
            while (totalReceived < fileSize) {
                int bytesToRead = (int) Math.min(buffer.length, fileSize - totalReceived);
                int bytesRead = dis.read(buffer, 0, bytesToRead);
                
                if (bytesRead == -1) break;
                
                fos.write(buffer, 0, bytesRead);
                totalReceived += bytesRead;
                
                if (totalReceived % (1024 * 1024) == 0) {
                    System.out.println("Da tai: " + totalReceived / (1024 * 1024) + " MB");
                }
            }
            
            fos.close();
            socket.close();
            
            System.out.println("Tai file thanh cong!");
            System.out.println("File da duoc luu tai: " + downloadPath);
            System.out.println("Tong so byte da tai: " + totalReceived);
            
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
}