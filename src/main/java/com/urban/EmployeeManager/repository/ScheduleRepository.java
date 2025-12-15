package com.urban.EmployeeManager.repository;

import com.urban.EmployeeManager.model.Schedule;
import com.urban.EmployeeManager.enums.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     *  PHƯƠNG THỨC : Tìm các lịch trình có khả năng xung đột.
     * Logic: Tìm các lịch của một nhân viên, mà khoảng thời gian của chúng
     * giao với khoảng thời gian [startDate, endDate] đang xét.
     * Đồng thời loại trừ chính lịch đang được cập nhật (dựa vào scheduleIdToExclude).
     */
    @Query("SELECT s FROM Schedule s WHERE s.employee.id = :employeeId " +
            "AND s.id <> :scheduleIdToExclude " +
            "AND s.startDate <= :endDate AND s.endDate >= :startDate")
    List<Schedule> findConflictingSchedules(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("scheduleIdToExclude") Long scheduleIdToExclude
    );
    /**
     *  PHƯƠNG THỨC
     * Tìm các lịch trình của một nhân viên cụ thể có khoảng thời gian
     * giao với khoảng thời gian lọc [filterStartDate, filterEndDate].
     * Logic: Một lịch trình (s) giao với khoảng lọc (f) nếu
     * ngày bắt đầu của lịch trình (s.startDate) nhỏ hơn hoặc bằng ngày kết thúc của bộ lọc (f.endDate)
     * VÀ ngày kết thúc của lịch trình (s.endDate) lớn hơn hoặc bằng ngày bắt đầu của bộ lọc (f.startDate).
     */
    @Query("SELECT s FROM Schedule s WHERE s.employee.id = :employeeId " +
            "AND s.startDate <= :filterEndDate AND s.endDate >= :filterStartDate")
    List<Schedule> findSchedulesForEmployeeInDateRange(
            @Param("employeeId") Long employeeId,
            @Param("filterStartDate") LocalDate filterStartDate,
            @Param("filterEndDate") LocalDate filterEndDate);

//    List<Schedule> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

//    @Query("SELECT s FROM Schedule s WHERE s.employee.id = :employeeId " +
//            "AND s.workDate BETWEEN :startDate AND :endDate")
//    List<Schedule> findByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
//                                                @Param("startDate") LocalDate startDate,
//                                                @Param("endDate") LocalDate endDate);

//    @Query("SELECT s FROM Schedule s WHERE s.workDate BETWEEN :startDate AND :endDate " +
//            "AND (:officeId IS NULL OR s.employee.office.id = :officeId) " +
//            "AND (:workType IS NULL OR s.workType = :workType)")
//    List<Schedule> findByFilters(@Param("startDate") LocalDate startDate,
//                                 @Param("endDate") LocalDate endDate,
//                                 @Param("officeId") Long officeId,
//                                 @Param("workType") WorkType workType);

    @Query("SELECT s FROM Schedule s WHERE " +
            "s.startDate <= :filterEndDate AND s.endDate >= :filterStartDate " +
            "AND (:officeId IS NULL OR s.office.id = :officeId) " +
            "AND (:workType IS NULL OR s.workType = :workType)")
    List<Schedule> findSchedulesByDateRangeAndFilters(
            @Param("filterStartDate") LocalDate filterStartDate,
            @Param("filterEndDate") LocalDate filterEndDate,
            @Param("officeId") Long officeId,
            @Param("workType") WorkType workType);

}

