import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class FileClientAdvanced {
    
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
            
            System.out.println("Chon chuc nang:");
            System.out.println("1. Download file tu server");
            System.out.println("2. Upload file len server");
            System.out.print("Lua chon (1/2): ");
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                downloadFile(dis, dos, scanner);
            } else if (choice.equals("2")) {
                uploadFile(dis, dos, scanner);
            } else {
                System.out.println("Lua chon khong hop le!");
            }
            
            socket.close();
            
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
    
    private static void downloadFile(DataInputStream dis, DataOutputStream dos, Scanner scanner) throws Exception {
        System.out.print("Nhap ten file can tai: ");
        String fileName = scanner.nextLine();
        
        // Gui command DOWNLOAD
        dos.writeUTF("DOWNLOAD");
        dos.writeUTF(fileName);
        
        // Nhan phan hoi
        String response = dis.readUTF();
        
        if (!response.equals("OK")) {
            System.out.println("Loi: " + response);
            return;
        }
        
        long fileSize = dis.readLong();
        
        System.out.println("Bat dau tai file: " + fileName);
        System.out.println("Kich thuoc file: " + fileSize + " bytes");
        
        String downloadPath = "downloads/" + fileName;
        
        File downloadDir = new File("downloads");
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        
        FileOutputStream fos = new FileOutputStream(downloadPath);
        byte[] buffer = new byte[4096];
        long totalReceived = 0;
        
        while (totalReceived < fileSize) {
            int bytesRead = dis.read(buffer);
            
            if (bytesRead == -1) break;
            
            fos.write(buffer, 0, bytesRead);
            totalReceived += bytesRead;
            
            if (totalReceived % (1024 * 1024) == 0) {
                System.out.println("Da tai: " + totalReceived / (1024 * 1024) + " MB");
            }
        }
        
        fos.close();
        
        System.out.println("Tai file thanh cong!");
        System.out.println("File da duoc luu tai: " + downloadPath);
        System.out.println("Tong so byte da tai: " + totalReceived);
    }
    
    private static void uploadFile(DataInputStream dis, DataOutputStream dos, Scanner scanner) throws Exception {
        System.out.print("Nhap duong dan file can upload: ");
        String filePath = scanner.nextLine();
        
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File khong ton tai: " + filePath);
            return;
        }
        
        String fileName = file.getName();
        long fileSize = file.length();
        
        // Gui command UPLOAD
        dos.writeUTF("UPLOAD");
        dos.writeUTF(fileName);
        dos.writeLong(fileSize);
        
        System.out.println("Bat dau upload file: " + fileName);
        System.out.println("Kich thuoc file: " + fileSize + " bytes");
        
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        long totalSent = 0;
        int bytesRead;
        
        while ((bytesRead = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, bytesRead);
            totalSent += bytesRead;
            
            if (totalSent % (1024 * 1024) == 0) {
                System.out.println("Da gui: " + totalSent / (1024 * 1024) + " MB");
            }
        }
        
        fis.close();
        
        // Nhan phan hoi
        String response = dis.readUTF();
        
        if (response.equals("UPLOAD OK")) {
            System.out.println("Upload file thanh cong!");
        } else {
            System.out.println("Upload file that bai: " + response);
        }
        
        System.out.println("Tong so byte da gui: " + totalSent);
    }
}