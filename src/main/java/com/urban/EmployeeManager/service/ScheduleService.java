package com.urban.EmployeeManager.service;

import com.urban.EmployeeManager.dto.ScheduleCreateDTO;
import com.urban.EmployeeManager.dto.ScheduleDTO;
import com.urban.EmployeeManager.dto.ScheduleUpdateDTO;
import com.urban.EmployeeManager.enums.WorkType;
import com.urban.EmployeeManager.exception.ResourceNotFoundException;
import com.urban.EmployeeManager.exception.InvalidScheduleException;
import com.urban.EmployeeManager.model.Employee;
import com.urban.EmployeeManager.model.Office;
import com.urban.EmployeeManager.model.Schedule;
import com.urban.EmployeeManager.repository.EmployeeRepository;
import com.urban.EmployeeManager.repository.OfficeRepository;
import com.urban.EmployeeManager.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository; // Thêm OfficeRepository

    // Hàm validate dùng chung
    public ScheduleDTO createSchedule(ScheduleCreateDTO createDTO) {

        validateScheduleLogic(
                createDTO.getEmployeeId(),
                createDTO.getStartDate(),
                createDTO.getEndDate(),
                createDTO.getStartTime(),
                createDTO.getEndTime(),
                createDTO.getWorkType(),
                0L
        );

        Employee employee = employeeRepository.findById(createDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + createDTO.getEmployeeId()));

        Office office = officeRepository.findById(createDTO.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy văn phòng với ID: " + createDTO.getOfficeId()));

        Schedule schedule = new Schedule();
        schedule.setEmployee(employee);
        schedule.setOffice(office);
        schedule.setStartDate(createDTO.getStartDate());
        schedule.setEndDate(createDTO.getEndDate());
        schedule.setStartTime(createDTO.getStartTime());
        schedule.setEndTime(createDTO.getEndTime());
        schedule.setWorkType(createDTO.getWorkType());
        schedule.setNotes(createDTO.getNotes());

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }

    public ScheduleDTO updateSchedule(Long id, ScheduleUpdateDTO updateDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc với ID: " + id));
        Employee oldEmployee = schedule.getEmployee();

        LocalDate newStartDate = (updateDTO.getStartDate() != null) ? updateDTO.getStartDate() : schedule.getStartDate();
        LocalDate newEndDate = (updateDTO.getEndDate() != null) ? updateDTO.getEndDate() : schedule.getEndDate();
        WorkType newWorkType = (updateDTO.getWorkType() != null) ? updateDTO.getWorkType() : schedule.getWorkType();

        LocalTime newStartTime;
        LocalTime newEndTime;

        if (newWorkType == WorkType.VACATION || newWorkType == WorkType.BUSINESS_TRIP) {
            // Nếu là sự kiện cả ngày, ÉP BUỘC startTime và endTime về null
            newStartTime = null;
            newEndTime = null;
        } else {
            // Nếu là sự kiện trong ngày, lấy giá trị từ DTO
            newStartTime = updateDTO.getStartTime();
            newEndTime = updateDTO.getEndTime();
        }
        // (1) Thực hiện tất cả các kiểm tra nghiệp vụ
        validateScheduleLogic(
                schedule.getEmployee().getId(),
                newStartDate,
                newEndDate,
                newStartTime,
                newEndTime,
                newWorkType,
                id // ID để loại trừ chính là lịch đang được sửa
        );

        // Cập nhật thông tin
        schedule.setStartDate(newStartDate);
        schedule.setEndDate(newEndDate);
        schedule.setStartTime(newStartTime);
        schedule.setEndTime(newEndTime);
        schedule.setWorkType(newWorkType);

        if (updateDTO.getOfficeId() != null) {
            Office office = officeRepository.findById(updateDTO.getOfficeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy văn phòng với ID: " + updateDTO.getOfficeId()));
            schedule.setOffice(office);
        }
        if (updateDTO.getWorkType() != null) {
            schedule.setWorkType(updateDTO.getWorkType());
        }
        if (updateDTO.getNotes() != null) {
            schedule.setNotes(updateDTO.getNotes());
        }
        if (updateDTO.getEmployeeId() != null && !updateDTO.getEmployeeId().equals(oldEmployee.getId())) {

            Employee newEmployee = employeeRepository.findById(updateDTO.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên mới với ID: " + updateDTO.getEmployeeId()));

            // Bước 1: Xóa lịch trình khỏi danh sách của nhân viên cũ (nếu danh sách được quản lý)
            // Dòng này giúp giữ cho đối tượng trong bộ nhớ nhất quán.
            oldEmployee.getSchedules().remove(schedule);

            // Bước 2: Gán nhân viên mới cho lịch trình (phía owning-side)
            schedule.setEmployee(newEmployee);

            // Bước 3: Thêm lịch trình vào danh sách của nhân viên mới (tùy chọn nhưng nên làm)
            newEmployee.getSchedules().add(schedule);
        }


        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }


    private void validateScheduleLogic(Long employeeId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, WorkType workType, Long scheduleIdToExclude) {
        // Yêu cầu 1: Kiểm tra ngày bắt đầu không trước ngày hiện tại
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidScheduleException("Không thể tạo/cập nhật lịch cho ngày trong quá khứ.");
        }

        // Yêu cầu 2: Kiểm tra ngày kết thúc >= ngày bắt đầu
        if (endDate.isBefore(startDate)) {
            throw new InvalidScheduleException("Ngày kết thúc không thể trước ngày bắt đầu.");
        }

        // Yêu cầu 3: Kiểm tra giờ kết thúc > giờ bắt đầu nếu cùng ngày
        if (startDate.isEqual(endDate)) {
            if (startTime != null && endTime != null && !endTime.isAfter(startTime)) {
                throw new InvalidScheduleException("Với sự kiện trong ngày, giờ kết thúc phải sau giờ bắt đầu.");
            }
        }

        // Yêu cầu 4: Kiểm tra xung đột lịch trình trong cơ sở dữ liệu
        List<Schedule> conflictingSchedules = scheduleRepository.findConflictingSchedules(
                employeeId,
                startDate,
                endDate,
                scheduleIdToExclude
        );
        if (!conflictingSchedules.isEmpty()) {
            throw new InvalidScheduleException("Lịch trình bị xung đột với một lịch trình khác đã tồn tại của nhân viên.");
        }
        // --- BẮT ĐẦU THÊM LOGIC VALIDATION MỚI ---
        switch (workType) {
            case NORMAL:
            case OUTSIDE:
            case OVERTIME:
                // Yêu cầu phải có giờ bắt đầu và kết thúc
                if (startTime == null || endTime == null) {
                    throw new InvalidScheduleException("Với loại công việc này, giờ bắt đầu và kết thúc là bắt buộc.");
                }
                // Yêu cầu ngày bắt đầu và kết thúc phải giống nhau
                if (!startDate.isEqual(endDate)) {
                    throw new InvalidScheduleException("Loại công việc này chỉ có thể diễn ra trong một ngày.");
                }
                break;

            case VACATION:
            case BUSINESS_TRIP:
                // Yêu cầu không được có giờ bắt đầu và kết thúc (để là sự kiện cả ngày)
                if (startTime != null || endTime != null) {
                    // Hoặc bạn có thể tự động set chúng về null thay vì báo lỗi
                    throw new InvalidScheduleException("Nghỉ phép và công tác là sự kiện cả ngày, không cần giờ cụ thể.");
                }
                break;
        }
        // --- KẾT THÚC LOGIC VALIDATION MỚI ---
    }



    public List<ScheduleDTO> getSchedulesByDateRange(LocalDate filterStartDate, LocalDate filterEndDate, Long officeId, WorkType workType) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByDateRangeAndFilters(filterStartDate, filterEndDate, officeId, workType);
        return schedules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesByEmployee(Long employeeId, LocalDate startDate, LocalDate endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId);
        }
        List<Schedule> schedules = scheduleRepository.findSchedulesForEmployeeInDateRange(employeeId, startDate, endDate);
        return schedules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc với ID: " + id));
        return convertToDTO(schedule);
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc với ID: " + id));
        if (schedule.getStartDate().isBefore(LocalDate.now())) {
            throw new InvalidScheduleException("Không thể xóa lịch làm việc của ngày đã qua");
        }
        scheduleRepository.deleteById(id);
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setEmployeeId(schedule.getEmployee().getId());
        dto.setEmployeeName(schedule.getEmployee().getName());
        if (schedule.getOffice() != null) {
            dto.setOfficeId(schedule.getOffice().getId());
            dto.setOfficeName(schedule.getOffice().getName());
        }
        dto.setStartDate(schedule.getStartDate());
        dto.setEndDate(schedule.getEndDate());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setWorkType(schedule.getWorkType());
        dto.setNotes(schedule.getNotes());
        return dto;
    }
}