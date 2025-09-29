Hướng dẫn tạo app cờ caro:
Server:
- Chấp nhận kết nối từ nhièu client ( chỉ có 2 client đầu được đánh, còn lại chỉ được xem)
- Nhận tọa độ của ai đó đánh và kiểm tra tính hợp lệ
- Gửi tọa độ mới nhất cho tất cả 
Client:
- Giao diện tương tác nguòi dùng
- Gửi tọa độ muốn đánh
- Nhận tọa độ mà ai đó vừa đánh từ server



CaroOffline


Viết chương trình tải file ( lớn hơn 50mb) với yêu cầu sau:
- Sever:
  + Chấp nhận nhiều kết nối
  + Mỗi kết nối server được yêu cầu 1 file nào đó chứa trong 1 thư mục có sẵn đã được cố định trước
  + Nếu tồn tại file đó thì gửi cho client
- Client:
  + Kết nối tới server
  + Gửi yêu cầu bằng cách gưuir 1 tên file
  + Nhận file đó từ server và lưu vào máy