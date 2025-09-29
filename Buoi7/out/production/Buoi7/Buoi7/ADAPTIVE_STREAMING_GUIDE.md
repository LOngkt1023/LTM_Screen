# Hệ Thống Adaptive Screen Sharing với Bandwidth Monitoring

## 🎯 Tổng quan

Đây là hệ thống chia sẻ màn hình thông minh có khả năng **đo tốc độ đường truyền của mỗi client** và **tự động điều chỉnh chất lượng streaming** cho phù hợp với băng thông của từng người dùng.

## ✨ Tính năng chính

### 🔍 Bandwidth Monitoring
- **Đo tốc độ download/upload real-time** (KB/s)
- **Ping measurement** để đánh giá độ trễ
- **Average speed calculation** trong 10 giây gần nhất
- **Automatic quality recommendation** dựa trên băng thông

### 🎚️ Adaptive Quality Control
- **4 mức chất lượng tự động:**
  - `VERY_LOW`: < 50 KB/s, ping > 200ms → 100px width, 5% JPEG quality, 2 FPS
  - `LOW`: 50-200 KB/s → 200px width, 20% JPEG quality, 5 FPS  
  - `MEDIUM`: 200-500 KB/s → 400px width, 50% JPEG quality, 10 FPS
  - `HIGH`: > 500 KB/s, ping < 200ms → 800px width, 80% JPEG quality, 30 FPS

### 📊 Real-time Monitoring Dashboard
- **Monitor tất cả clients** đang kết nối
- **Thống kê bandwidth tổng quan**
- **Quality distribution** của các clients
- **Connection status và duration**

## 🗂️ Cấu trúc Files

```
Buoi7/
├── BandwidthMonitor.java         # Class đo và quản lý băng thông
├── ScreenServerAdaptive.java     # Server với adaptive streaming
├── ScreenClientAdaptive.java     # Client với bandwidth monitoring  
├── AdaptiveStreamingMonitor.java # Dashboard quản lý server
├── run_adaptive_streaming.bat    # Script chạy hệ thống
└── ScreenClient.java            # Client gốc (tham khảo)
```

## 🚀 Cách sử dụng

### Bước 1: Biên dịch
```bash
# Chạy file bat và chọn option 5
run_adaptive_streaming.bat
# Hoặc chạy trực tiếp:
javac -encoding UTF-8 Buoi7/*.java
```

### Bước 2: Khởi động Server + Monitor Dashboard
```bash
# Option 1: Server + Monitor (Recommended)
run_adaptive_streaming.bat → chọn 1
```

### Bước 3: Kết nối Clients
```bash
# Mở terminals mới và chạy clients
run_adaptive_streaming.bat → chọn 3
```

## 🔧 Cách hoạt động

### Server Side (`ScreenServerAdaptive.java`)
1. **Capture màn hình** với Robot API (30 FPS max)
2. **Quản lý nhiều clients** với ConcurrentHashMap
3. **Ping/Pong protocol** để đo latency mỗi 5 giây
4. **Adaptive image processing:**
   - Resize ảnh theo maxWidth của quality level
   - JPEG compression với quality tương ứng
   - Frame rate limiting theo maxFrameInterval
5. **Real-time quality adjustment** dựa trên bandwidth feedback

### Client Side (`ScreenClientAdaptive.java`)
1. **Nhận frames** từ server và track bandwidth
2. **Ping/Pong response** để server đo latency
3. **Real-time stats display:**
   - Download speed (KB/s)
   - Average speed và ping
   - Recommended quality level
   - Connection duration
4. **Visual quality indicators** với màu nền tương ứng

### Monitoring Dashboard (`AdaptiveStreamingMonitor.java`)
1. **Table view** hiển thị tất cả clients
2. **Color-coded status:**
   - 🟢 Xanh: High quality connection
   - 🟡 Vàng: Medium quality  
   - 🔴 Đỏ: Poor connection/disconnected
3. **Summary statistics** ở header
4. **Auto-refresh** mỗi 2 giây

## 📈 Quality Level Logic

```java
// Trong BandwidthMonitor.getRecommendedQuality()
if (ping > 200ms) return VERY_LOW;
if (avgSpeed > 500 KB/s) return HIGH;
if (avgSpeed > 200 KB/s) return MEDIUM;  
if (avgSpeed > 50 KB/s) return LOW;
else return VERY_LOW;
```

## 🌐 Test qua mạng

### Để test giữa 2 máy tính:
1. Sửa `SERVER_ADDRESS` trong `ScreenClientAdaptive.java`:
   ```java
   private static final String SERVER_ADDRESS = "192.168.1.100"; // IP máy server
   ```
2. Chạy Server trên máy 1
3. Chạy Client trên máy 2
4. Xem kết quả bandwidth test trong giao diện

## 📊 Monitoring Information

### Client Table Columns:
- **Client ID**: Unique identifier
- **IP Address**: Remote socket address  
- **Download Speed**: Current KB/s
- **Ping**: Latency in milliseconds
- **Quality**: Current quality level
- **Frames Sent**: Total frames processed
- **Status**: Active/Disconnected
- **Duration**: Connection time

### Summary Statistics:
- Total active clients
- Combined bandwidth usage
- Quality distribution (High/Medium/Low counts)

## 🔧 Customization Options

### Thay đổi Quality Thresholds:
```java
// Trong BandwidthMonitor.java
if (avgSpeed > 1000) return ULTRA_HIGH; // Custom level
```

### Thay đổi Frame Rate:
```java
// Trong QualityLevel enum
HIGH(0.9f, 800, 80, 16); // 16ms = ~60 FPS
```

### Thay đổi Ping Frequency:
```java
// Trong AdaptiveScreenProcessor.handlePingPong()
Thread.sleep(3000); // Ping mỗi 3 giây thay vì 5 giây
```

## 🐛 Troubleshooting

### Client không kết nối được:
- Kiểm tra server đã chạy chưa
- Kiểm tra firewall/antivirus
- Xác nhận port 2345 không bị block

### Bandwidth readings không chính xác:
- Đợi ít nhất 10 giây để average stabilize
- Kiểm tra mạng có stable không
- Monitor dashboard để xem overview

### Performance issues:
- Giảm frame rate trong ScreenCapture (tăng sleep time)
- Adjust quality thresholds cho phù hợp
- Check CPU usage với multiple clients

## 📋 TODO và Extensions

- [ ] Export bandwidth statistics to CSV
- [ ] Manual quality override trong dashboard  
- [ ] Audio streaming support
- [ ] Encryption cho secure transmission
- [ ] Multi-monitor support
- [ ] Recording functionality
- [ ] Client authentication

---

**🎉 Hoàn thành!** Hệ thống đã tích hợp đầy đủ bandwidth monitoring và adaptive quality control cho optimal user experience!