# 📺 LTM_Screen - Screen Sharing Project

## 🎯 Mô tả dự án
Ứng dụng chia sẻ màn hình (Screen Sharing) được phát triển bằng Java với tính năng đo tốc độ đường truyền real-time.

## 📁 Cấu trúc dự án
```
GKi/
├── src/
│   ├── ScreenServer.java    # Server chính với tính năng monitoring
│   └── ScreenClient.java    # Client với đo bandwidth
├── .idea/                   # IntelliJ IDEA config
└── GKi.iml                 # Module config
```

## 🚀 Tính năng chính

### ✅ ScreenServer.java
- **Screen Capture**: Chụp màn hình real-time
- **Multi-client Support**: Hỗ trợ nhiều client cùng lúc
- **Bandwidth Monitoring**: Nhận và hiển thị tốc độ từ client
- **Network Optimization**: Socket optimization với TCP_NODELAY

### ✅ ScreenClient.java  
- **Real-time Display**: Hiển thị màn hình từ server
- **Bandwidth Measurement**: Đo tốc độ đường truyền
- **Dynamic Title**: Hiển thị tốc độ trên title bar
- **Auto Quality Adjustment**: Tự động điều chỉnh dựa trên bandwidth

## 🔧 Cách chạy

### Bước 1: Compile
```bash
cd GKi/src
javac ScreenServer.java
javac ScreenClient.java
```

### Bước 2: Chạy Server
```bash
java ScreenServer
```

### Bước 3: Chạy Client
```bash
java ScreenClient
```

## 📊 Thông số kỹ thuật
- **Port**: 2345
- **Protocol**: TCP
- **Image Format**: PNG (có thể mở rộng sang JPEG)
- **Refresh Rate**: Real-time với tối ưu hóa
- **Buffer Size**: 128KB send/receive buffer

## 🌐 Network Configuration
- **Localhost**: `127.0.0.1:2345`
- **LAN**: Thay đổi IP trong ScreenClient.java line 29
- **Remote**: Cấu hình port forwarding nếu cần

## 📈 Performance Monitoring
Client sẽ hiển thị:
- **Bandwidth real-time** trên title bar
- **Automatic quality adjustment** dựa trên tốc độ mạng
- Server console hiển thị thông tin từ tất cả client

## 🎓 Đây là bài tập giữa kì môn Lập trình mạng
**Sinh viên**: LOngkt1023  
**Repository**: https://github.com/LOngkt1023/LTM_Screen.git