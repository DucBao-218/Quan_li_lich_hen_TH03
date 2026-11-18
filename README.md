# Ứng dụng Quản lý lịch hẹn

Bài thực hành Android dùng **Java + XML**, **Intent** và **SQLiteOpenHelper**, gồm bốn màn hình theo đề.

## Chạy ứng dụng

Mở thư mục dự án bằng Android Studio, chờ Gradle Sync rồi nhấn Run trên máy ảo hoặc điện thoại Android 7.0 trở lên. Đăng nhập bằng `admin` / `123456`.

## Đối chiếu yêu cầu

- `LoginActivity`: kiểm tra trường trống, truy vấn tài khoản trong bảng `users`, báo sai tài khoản, mở danh sách và `finish()` khi đăng nhập đúng.
- `MainActivity`: ListView hiển thị tên, địa điểm, ngày giờ và nhãn nhắc nhở. Truy vấn `ORDER BY date ASC, time ASC, id ASC`. Đọc lại danh sách và hai thống kê trong `onResume()`, có trạng thái danh sách rỗng.
- `AddAppointmentActivity`: nhập tên và địa điểm, DatePicker, TimePicker dạng spinner 24 giờ, Switch nhắc nhở. Chỉ lưu khi đủ dữ liệu và thời điểm hẹn ở tương lai.
- `AppointmentDetailActivity`: nhận `appointment_id` qua Intent rồi truy vấn lại SQLite; đổi trạng thái nhắc nhở, quay lại hoặc xác nhận xóa.
- `DatabaseHelper`: cơ sở dữ liệu `appointments.db`, phiên bản 1, hai bảng đúng đề. Chèn tài khoản và ba lịch mẫu duy nhất trong `onCreate()`. Có đủ phương thức đăng nhập, thêm, truy vấn, cập nhật nhắc nhở, xóa, đếm tổng và đếm nhắc nhở.
- `Appointment`: lớp Java biểu diễn lịch hẹn, ngày lưu `yyyy-MM-dd`, giờ lưu `HH:mm`, hiển thị `dd/MM/yyyy · HH:mm`.
- Truy vấn giá trị đầu vào có tham số; Cursor được đóng bằng try-with-resources. ArrayList chỉ chứa kết quả truy vấn để hiển thị, dữ liệu chính lưu trong SQLite.

Ba lịch mẫu giữ nguyên ngày 25–27/10/2026 theo đề dù đã ở quá khứ. Quy tắc thời gian tương lai áp dụng khi thêm mới. Switch chỉ lưu trạng thái; đề không bắt buộc thông báo thật.

## Kiểm tra

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest lintDebug
# Cần có máy ảo hoặc điện thoại đang kết nối:
.\gradlew.bat connectedDebugAndroidTest
```

`AppointmentTest` kiểm tra định dạng ngày giờ. `DatabaseHelperTest` dùng database riêng trong cache, kiểm tra tài khoản mẫu, đầu vào SQL injection, dữ liệu mẫu, thứ tự ngày giờ, thêm/đổi nhắc nhở/xóa, thống kê, danh sách rỗng và giữ dữ liệu sau khi đóng/mở database.

`AppointmentFlowTest` kiểm tra giao diện đăng nhập trống/sai/đúng, thêm lịch, xem chi tiết, tắt nhắc nhở và xóa lịch vừa tạo. Máy ảo cần được mở khóa và không có hộp thoại hệ thống che ứng dụng khi chạy Espresso.

Kiểm tra thủ công: đăng nhập trống/sai/đúng; thêm lịch trống và lịch quá khứ; thêm lịch tương lai; đổi nhắc nhở; xóa; quay lại để xem thống kê; buộc dừng rồi mở lại để kiểm tra dữ liệu giữ nguyên. Xóa dữ liệu ứng dụng hoặc gỡ cài đặt sẽ tạo lại dữ liệu mẫu khi mở lần tiếp theo.
