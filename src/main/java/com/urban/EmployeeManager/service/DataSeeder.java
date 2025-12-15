// DataSeeder.java
package com.urban.EmployeeManager.service;

import com.urban.EmployeeManager.config.DataSeedingConfig;
import com.urban.EmployeeManager.enums.Gender;
import com.urban.EmployeeManager.enums.Position;
import com.urban.EmployeeManager.enums.Role;
import com.urban.EmployeeManager.enums.WorkType;
import com.urban.EmployeeManager.model.Employee;
import com.urban.EmployeeManager.model.Office;
import com.urban.EmployeeManager.model.Schedule;
import com.urban.EmployeeManager.repository.EmployeeRepository;
import com.urban.EmployeeManager.repository.OfficeRepository;
import com.urban.EmployeeManager.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test") // Không chạy khi test
public class DataSeeder implements CommandLineRunner {

    private final OfficeRepository officeRepository;
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataSeedingConfig dataSeedingConfig;

    @Override
    public void run(String... args) throws Exception {
        if (!dataSeedingConfig.isSeedEnabled()) {
            log.info("Data seeding bị vô hiệu hóa trong cấu hình.");
            return;
        }

        if (shouldSeedData()) {
            log.info("Bắt đầu seed dữ liệu...");
            seedOffices();
            seedEmployees();
            seedSchedules();
            log.info("Hoàn thành seed dữ liệu!");
        } else {
            log.info("Dữ liệu đã tồn tại, bỏ qua seed.");
        }
    }

    private boolean shouldSeedData() {
        return officeRepository.count() == 0 && employeeRepository.count() == 0;
    }

    private void seedOffices() {
        if (officeRepository.count() > 0) {
            log.info("Offices đã tồn tại, bỏ qua seed offices.");
            return;
        }

        List<Office> offices = Arrays.asList(
                createOffice("Văn phòng Hà Nội", "Tầng 10, Tòa nhà Keangnam, Phạm Hùng, Nam Từ Liêm, Hà Nội"),
                createOffice("Văn phòng TP.HCM", "Tầng 15, Tòa nhà Bitexco, Quận 1, TP.HCM"),
                createOffice("Văn phòng Đà Nẵng", "Tầng 5, Tòa nhà FPT, Quận Ngũ Hành Sơn, Đà Nẵng"),
                createOffice("Văn phòng Tokyo", "Shibuya Sky Building, Tokyo, Japan"),
                createOffice("Văn phòng Osaka", "Umeda Business Center, Osaka, Japan")
        );

        officeRepository.saveAll(offices);
        log.info("Đã seed {} văn phòng", offices.size());
    }

    private Office createOffice(String name, String address) {
        Office office = new Office();
        office.setName(name);
        office.setAddress(address);
        return office;
    }

    private void seedEmployees() {
        if (employeeRepository.count() > 0) {
            log.info("Employees đã tồn tại, bỏ qua seed employees.");
            return;
        }

        List<Office> offices = officeRepository.findAll();
        if (offices.isEmpty()) {
            log.warn("Không có office nào để seed employees");
            return;
        }

        List<Employee> employees = Arrays.asList(
                // Admin users
                createEmployee("Nguyễn Văn Admin", "admin", "admin@urban.vn",
                        "123456", "0901234567", Gender.MALE,
                        "123 Đường ABC, Hà Nội", Position.MANAGER,
                        offices.get(0), Role.ADMIN),

                createEmployee("Trần Thị Quản lý", "manager", "manager@urban.vn",
                        "123456", "0901234568", Gender.FEMALE,
                        "456 Đường XYZ, TP.HCM", Position.MANAGER,
                        offices.get(1), Role.MANAGER),

                // Hà Nội Office
                createEmployee("Lê Văn Hùng", "hung.le", "hung.le@urban.vn",
                        "123456", "0901234569", Gender.MALE,
                        "789 Đường DEF, Hà Nội", Position.LEADER,
                        offices.get(0), Role.USER),

                createEmployee("Phạm Thị Lan", "lan.pham", "lan.pham@urban.vn",
                        "123456", "0901234570", Gender.FEMALE,
                        "101 Đường GHI, Hà Nội", Position.STAFF,
                        offices.get(0), Role.USER),

                createEmployee("Hoàng Minh Tuấn", "tuan.hoang", "tuan.hoang@urban.vn",
                        "123456", "0901234571", Gender.MALE,
                        "202 Đường JKL, Hà Nội", Position.STAFF,
                        offices.get(0), Role.USER),



                createEmployee("Đặng Văn Nam", "nam.dang", "nam.dang@urban.vn",
                        "123456", "0901234573", Gender.MALE,
                        "404 Đường PQR, TP.HCM", Position.STAFF,
                        offices.get(1), Role.USER),

                createEmployee("Vũ Thị Hoa", "hoa.vu", "hoa.vu@urban.vn",
                        "123456", "0901234574", Gender.FEMALE,
                        "505 Đường STU, TP.HCM", Position.STAFF,
                        offices.get(1), Role.USER),

                // Đà Nẵng Office
                createEmployee("Bùi Văn Đức", "duc.bui", "duc.bui@urban.vn",
                        "123456", "0901234575", Gender.MALE,
                        "606 Đường VWX, Đà Nẵng", Position.LEADER,
                        offices.get(2), Role.USER),

                createEmployee("Lý Thị Nga", "nga.ly", "nga.ly@urban.vn",
                        "123456", "0901234576", Gender.FEMALE,
                        "707 Đường YZ, Đà Nẵng", Position.STAFF,
                        offices.get(2), Role.USER),

                // Tokyo Office
                createEmployee("Tanaka Hiroshi", "hiroshi.tanaka", "hiroshi.tanaka@urban.vn",
                        "123456", "090-1234-5577", Gender.MALE,
                        "1-1-1 Shibuya, Tokyo", Position.MANAGER,
                        offices.get(3), Role.MANAGER),

                createEmployee("Sato Yuki", "yuki.sato", "yuki.sato@urban.vn",
                        "123456", "090-1234-5578", Gender.FEMALE,
                        "2-2-2 Shinjuku, Tokyo", Position.STAFF,
                        offices.get(3), Role.USER),



                createEmployee("Suzuki Akiko", "akiko.suzuki", "akiko.suzuki@urban.vn",
                        "123456", "090-1234-5580", Gender.FEMALE,
                        "4-4-4 Namba, Osaka", Position.STAFF,
                        offices.get(4), Role.USER)
        );

        employeeRepository.saveAll(employees);
        log.info("Đã seed {} nhân viên", employees.size());
    }

    private Employee createEmployee(String name, String username, String email,
                                    String password, String phoneNumber, Gender gender,
                                    String address, Position position, Office office, Role role) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setUsername(username);
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setPhoneNumber(phoneNumber);
        employee.setGender(gender);
        employee.setAddress(address);
        employee.setPosition(position);
        employee.setOffice(office);
        employee.setRole(role);
        return employee;
    }

    private void seedSchedules() {
        if (scheduleRepository.count() > 0) {
            log.info("Schedules đã tồn tại, bỏ qua seed schedules.");
            return;
        }

        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            log.warn("Không có employee nào để seed schedules");
            return;
        }

        Random random = new Random();
        LocalDate today = LocalDate.now();

        for (Employee employee : employees) {
            // Tạo khoảng 2-4 lịch trình ngẫu nhiên cho mỗi nhân viên trong tháng này và tháng tới
            int numberOfSchedules = 2 + random.nextInt(3); // Tạo từ 2 đến 4 lịch trình
            for (int i = 0; i < numberOfSchedules; i++) {

                WorkType workType = getRandomWorkType();

                // Tạo ngày bắt đầu ngẫu nhiên trong vòng 30 ngày tới
                LocalDate startDate = today.plusDays(random.nextInt(30));
                LocalDate endDate;

                if (workType == WorkType.BUSINESS_TRIP || workType == WorkType.VACATION) {
                    // Nếu là công tác hoặc nghỉ phép, tạo sự kiện dài ngày (2-5 ngày)
                    int durationInDays = 2 + random.nextInt(4); // Từ 2 đến 5 ngày
                    endDate = startDate.plusDays(durationInDays);
                } else {
                    // Nếu là các loại khác, tạo sự kiện trong ngày
                    endDate = startDate;
                }

                Schedule schedule = createSchedule(employee, startDate, endDate, workType);
                scheduleRepository.save(schedule);
            }
        }

        log.info("Đã seed {} lịch làm việc ngẫu nhiên.", scheduleRepository.count());
    }


    private Schedule createSchedule(Employee employee, LocalDate startDate, LocalDate endDate, WorkType workType) {
        Schedule schedule = new Schedule();
        schedule.setEmployee(employee);
        // Gán cả ngày bắt đầu và kết thúc
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        schedule.setWorkType(workType);

        switch (workType) {
            case NORMAL:
            case OVERTIME:
            case OUTSIDE:
                // Các sự kiện này vẫn có giờ bắt đầu và kết thúc cụ thể
                schedule.setStartTime(LocalTime.of(8, 30));
                schedule.setEndTime(LocalTime.of(17, 30));
                schedule.setNotes("Công việc trong ngày.");
                break;
            case BUSINESS_TRIP:
            case VACATION:
                // Sự kiện dài ngày thường không cần giờ cụ thể (all-day event)
                schedule.setStartTime(null);
                schedule.setEndTime(null);
                schedule.setNotes(workType == WorkType.BUSINESS_TRIP ? "Đi công tác dài ngày" : "Nghỉ phép theo kế hoạch");
                break;
        }

        return schedule;
    }

    private WorkType getRandomWorkType() {
        // ... (Giữ nguyên không đổi)
        WorkType[] workTypes = WorkType.values();
        return workTypes[new Random().nextInt(workTypes.length)];
    }
}


