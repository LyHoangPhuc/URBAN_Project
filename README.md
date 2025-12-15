# Employee Manager Application

Employee Manager là một ứng dụng quản lý nhân sự được xây dựng trên nền tảng **Spring Boot**. Ứng dụng hỗ trợ các chức năng quản lý nhân viên, văn phòng, lịch làm việc và các tính năng liên quan đến xác thực, phân quyền, và quản lý tài nguyên.

---

## 🎯 **Các tính năng chính**
- **Quản lý nhân viên**:
  - Thêm, sửa, xóa, tìm kiếm và phân quyền nhân viên.
  - Upload và quản lý avatar của nhân viên.
  - Thay đổi mật khẩu và quản lý thông tin cá nhân.
  - Thống kê số lượng nhân viên theo chức vụ và văn phòng.

- **Quản lý văn phòng**:
  - Thêm, sửa, xóa và lấy danh sách các văn phòng.

- **Quản lý lịch làm việc**:
  - Tạo, sửa, xóa và xem lịch làm việc cho nhân viên.
  - Kiểm tra xung đột lịch làm việc.
  - Hỗ trợ các loại lịch trình như:
    - Làm việc thông thường
    - Công tác
    - Nghỉ phép
    - Ra ngoài
    - Tăng ca

- **Xác thực và phân quyền**:
  - Hỗ trợ đăng nhập, đăng xuất, và kiểm tra thông tin người dùng hiện tại.
  - Phân quyền theo vai trò: **Admin**, **Manager**, **User**.
  - Chức năng quên mật khẩu và đặt lại mật khẩu.

- **Cấu hình bảo mật**:
  - Sử dụng Spring Security để bảo vệ API.
  - Hỗ trợ CORS cho các nguồn gốc cụ thể.
  - Xử lý session và bảo mật cookie.

- **Upload file**:
  - Upload avatar với các định dạng được hỗ trợ (jpg, jpeg, png, gif).
  - Giới hạn kích thước file tối đa 5MB.

---

## 🛠 **Công nghệ sử dụng**
- **Backend**: Spring Boot 3, Spring Security, Hibernate JPA.
- **Cơ sở dữ liệu**: MySQL.
- **Thư viện hỗ trợ**:
  - Lombok.
  - Thymeleaf (gửi email).
  - Spring Mail (gửi email thông qua SMTP).
- **API Documentation**: Swagger.

---

## 🚀 **Hướng dẫn cài đặt**

### 1. **Yêu cầu hệ thống**
- **Java**: JDK 17 hoặc mới hơn.
- **Maven**: 3.8.0 hoặc mới hơn.
- **MySQL**: 8.0 hoặc mới hơn.

### 2. **Clone repository**
```bash
git clone <repository-url>
cd EmployeeManager
```

### 3. **Cấu hình cơ sở dữ liệu**
- Tạo một cơ sở dữ liệu MySQL mới (ví dụ: `employee_manager`).
- Mở tệp `application.properties` hoặc `application.yml` và cập nhật thông tin kết nối cơ sở dữ liệu:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_manager
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>
```

### 4. **Chạy ứng dụng**
- Sử dụng Maven để build và chạy ứng dụng:
```bash
mvn spring-boot:run
```
- Ứng dụng sẽ chạy tại `http://localhost:8080`.

---

## 📖 **Cách sử dụng**

### 1. **Tài khoản mặc định**
- **Admin**:
  - Username: `admin`
  - Password: `123456`
- **Manager**:
  - Username: `manager`
  - Password: `123456`

### 2. **Swagger API Documentation**
- Truy cập tài liệu API tại: `http://localhost:8080/swagger-ui.html`.

### 3. **Thư mục upload**
- Các avatar được lưu trong thư mục: `uploads/avatars`.

---

## 📂 **Cấu trúc thư mục**
Dưới đây là cấu trúc chính của ứng dụng:

```
EmployeeManager/
├── config/                     # Cấu hình ứng dụng
├── controller/                 # REST API controllers
├── dto/                        # Data Transfer Objects (DTOs)
├── enums/                      # Enum định nghĩa
├── exception/                  # Xử lý và định nghĩa lỗi
├── model/                      # Các model tương ứng với bảng trong database
├── repository/                 # Repository để giao tiếp với cơ sở dữ liệu
├── service/                    # Business logic
├── util/                       # Các tiện ích
└── EmployeeManagerApplication.java # Điểm khởi đầu của ứng dụng
```

---

## 📌 **Chức năng API chính**
| **Endpoint**                | **Phương thức** | **Mô tả**                                                                                 | **Quyền truy cập**        |
|-----------------------------|-----------------|-------------------------------------------------------------------------------------------|---------------------------|
| `/api/auth/login`           | POST            | Đăng nhập và tạo session                                                                  | Công khai                |
| `/api/auth/logout`          | POST            | Đăng xuất và hủy session                                                                  | Đã đăng nhập             |
| `/api/auth/me`              | GET             | Lấy thông tin người dùng hiện tại                                                        | Đã đăng nhập             |
| `/api/employees`            | GET             | Lấy danh sách nhân viên                                                                  | ADMIN, MANAGER           |
| `/api/employees/{id}`       | GET             | Lấy thông tin chi tiết của một nhân viên                                                 | ADMIN, MANAGER           |
| `/api/employees`            | POST            | Tạo nhân viên mới                                                                        | ADMIN, MANAGER           |
| `/api/employees/{id}`       | PUT             | Cập nhật thông tin nhân viên                                                             | ADMIN, MANAGER           |
| `/api/employees/{id}`       | DELETE          | Xóa nhân viên                                                                            | ADMIN, MANAGER           |
| `/api/employees/statistics` | GET             | Lấy thống kê nhân viên theo chức vụ và văn phòng                                         | ADMIN, MANAGER           |
| `/api/schedules`            | GET             | Lấy danh sách lịch làm việc                                                              | ADMIN, MANAGER           |
| `/api/schedules`            | POST            | Tạo lịch làm việc mới                                                                     | ADMIN, MANAGER, USER     |
| `/api/schedules/{id}`       | PUT             | Cập nhật lịch làm việc                                                                   | ADMIN, MANAGER, USER     |
| `/api/schedules/{id}`       | DELETE          | Xóa lịch làm việc                                                                        | ADMIN, MANAGER, USER     |

