# Employee Manager Application

Employee Manager là một ứng dụng quản lý nhân sự được xây dựng trên nền tảng **Spring Boot**. Ứng dụng hỗ trợ các chức năng quản lý nhân viên, văn phòng, lịch làm việc và các tính năng liên quan đến xác thực, phân quyền, và quản lý tài nguyên.

---
## Vai trò trong nhóm : front-end làm trang đăng nhập đăng nhập/ đăng ký và trang lịch của nhân viên.
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

Ảnh Demo: 

<img width="596" height="641" alt="image" src="https://github.com/user-attachments/assets/abac4e15-6e6f-4962-8736-673334763990" />

Hình 1 Giao diện đăng nhập của trang web 
Tại đây Admin và User nhân viên sẽ đăng nhập bằng tên tài khoản và mật khẩu 
để vào trang chính của trang web. Có thể bấm “Quên mật khẩu” nếu muốn lấy lại mật 
khẩu của mình.

<img width="637" height="341" alt="image" src="https://github.com/user-attachments/assets/3f4f86c9-0894-42a1-b8ce-aab4dea630e8" />

Hình 2 Giao diện chính của trang web với quyền Admin
Tại đây Admin có thể xem được thống kê số lượng nhân viên trong công ty, sắp 
xếp theo chức vụ và theo văn phòng, ngoài ra Admin còn có thể xem danh sách nhân 
viên và một số thông tin của các nhân viên hiển thị. 

<img width="408" height="557" alt="image" src="https://github.com/user-attachments/assets/5df7b48f-817a-4045-aaeb-66196f01b9c3" />

Hình 3 Giao diện đổi mật khẩu
Tại đây Admin và User nhân viên có thể đổi và xác nhận lại mật khẩu của mình 
khi chọn chức năng “Quên mật khẩu” ở giao diện “Đăng nhập”.

<img width="747" height="399" alt="image" src="https://github.com/user-attachments/assets/e0d9f814-e2f0-4f65-be3d-2cc92606504d" />

Hình 4 Giao diện xem danh sách nhân viên 
Tại đây Admin có thể xem thông tin các nhân viên có trong công ty và có thể 
nhấp vào từng nhân viên để xem thông tin chi tiết. Admin cũng có thể sửa thông tin 
nhân viên hoặc xóa nếu muốn.

<img width="746" height="408" alt="image" src="https://github.com/user-attachments/assets/f5ee7236-94c7-4cc5-98a1-8d0d832aa3b3" />

Hình 5 Giao diện xem lịch của mọi nhân viên trong công ty
Tại đây Admin có thể xem lịch làm việc của mọi nhân viên, có thể xem theo tuần, 
ngày, tháng. Admin có thể nhấp vào một lịch bất kì để xem chi tiết thông tin của lịch 
đó.

<img width="755" height="211" alt="image" src="https://github.com/user-attachments/assets/d49535f9-e176-4f3c-9c27-5ce87b9ab6f3" />


Hình 6 Giao diện xem các văn phòng có trong công ty 
Tại đây Admin có thể xem các văn phòng của công ty. Admin có thể nhấp vào 
từng văn phòng để xem thông tin chi tiết và cũng có thể thêm hoặc sửa thông tin văn 
phòng.

<img width="752" height="409" alt="image" src="https://github.com/user-attachments/assets/66667f71-7bec-4881-8d5f-8809b71b0104" />

Hình 7 Giao diện chính của trang web với quyền User nhân viên
Sau khi đăng nhập thành công vào trang web, thì giao diện chính của User nhân 
viên sẽ hiện ra, tại đây User nhân viên có thể xem được thứ, ngày, tháng, năm, giờ và 
lịch làm việc của mình.

<img width="632" height="411" alt="image" src="https://github.com/user-attachments/assets/728f2884-0211-43df-97ef-6115ac4f686d" />

Hình 8 Giao diện xem thông tin cá nhân của User nhân viên
Tại đây User nhân viên có thể xem thông tin cá nhân của mình. 
