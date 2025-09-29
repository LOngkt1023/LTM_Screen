# Hướng dẫn chạy Game Caro Online

## Các tính năng đã được triển khai:

### Server (CaroServer.java):
- ✅ Chấp nhận kết nối từ nhiều client (chỉ có 2 client đầu được đánh, còn lại chỉ được xem)
- ✅ Nhận tọa độ của ai đó đánh và kiểm tra tính hợp lệ
- ✅ Gửi tọa độ mới nhất cho tất cả client
- ✅ Kiểm tra thắng thua (5 quân liên tiếp)
- ✅ Quản lý lượt chơi
- ✅ Xử lý ngắt kết nối

### Client (CaroClient.java):
- ✅ Giao diện tương tác người dùng
- ✅ Gửi tọa độ muốn đánh
- ✅ Nhận tọa độ mà ai đó vừa đánh từ server
- ✅ Hiển thị trạng thái game
- ✅ Phân biệt player và khán giả

## Cách chạy:

### Cách 1: Sử dụng file batch (Dễ nhất)
1. Chạy file `run_caro_online.bat`
2. Chọn "3" để biên dịch tất cả
3. Chọn "1" để chạy Server
4. Mở terminal mới, chạy lại `run_caro_online.bat` và chọn "2" để chạy Client
5. Lặp lại bước 4 để tạo thêm client

### Cách 2: Chạy thủ công
```bash
# Biên dịch
javac -encoding UTF-8 *.java

# Chạy server (terminal 1)
java CaroServer

# Chạy client (terminal 2)
java CaroClient

# Chạy client thứ 2 (terminal 3)
java CaroClient
```

## Cách chơi:

1. **Khởi động Server**: Chạy CaroServer trước
2. **Kết nối Client**: 
   - Client đầu tiên sẽ là Player 1 (O - màu xanh)
   - Client thứ hai sẽ là Player 2 (X - màu đỏ)
   - Client từ thứ 3 trở đi sẽ là khán giả
3. **Chơi game**:
   - Player 1 (O) đi trước
   - Click vào ô trống để đánh
   - Ai đạt được 5 quân liên tiếp sẽ thắng
4. **Kết thúc game**: Game sẽ tự động reset khi có player ngắt kết nối

## Giao thức giao tiếp:

### Server gửi cho Client:
- `PLAYER_NUMBER:1` hoặc `PLAYER_NUMBER:2` - Thông báo số thứ tự player
- `SPECTATOR` - Thông báo là khán giả
- `GAME_START` - Game bắt đầu
- `TURN:1` hoặc `TURN:2` - Thông báo lượt chơi
- `MOVE:x,y,player` - Thông báo nước đi mới
- `GAME_OVER:WINNER:1` - Thông báo kết thúc game
- `PLAYER_DISCONNECTED` - Thông báo player ngắt kết nối

### Client gửi cho Server:
- `MOVE:x,y` - Gửi nước đi

## Lưu ý:
- Server chạy trên port 12345
- Client kết nối đến localhost:12345
- Có thể thay đổi SERVER_ADDRESS trong CaroClient.java để kết nối đến server khác
- Game sẽ reset khi có player chính thức ngắt kết nối
