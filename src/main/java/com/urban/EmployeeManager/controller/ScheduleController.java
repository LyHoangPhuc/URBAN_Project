package com.urban.EmployeeManager.controller;

import com.urban.EmployeeManager.dto.ScheduleCreateDTO;
import com.urban.EmployeeManager.dto.ScheduleDTO;
import com.urban.EmployeeManager.dto.ScheduleUpdateDTO;
import com.urban.EmployeeManager.enums.WorkType;
import com.urban.EmployeeManager.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<ScheduleDTO>> getSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long officeId,
            @RequestParam(required = false) WorkType workType) {

        List<ScheduleDTO> schedules = scheduleService.getSchedulesByDateRange(startDate, endDate, officeId, workType);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or #employeeId == @authService.getCurrentUser().getId()")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByEmployee(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ScheduleDTO> schedules = scheduleService.getSchedulesByEmployee(employeeId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @scheduleService.getScheduleById(#id).getEmployeeId() == @authService.getCurrentUser().getId()")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        ScheduleDTO schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or #createDTO.getEmployeeId() == @authService.getCurrentUser().getId()")
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleCreateDTO createDTO) {
        ScheduleDTO schedule = scheduleService.createSchedule(createDTO);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @scheduleService.getScheduleById(#id).getEmployeeId() == @authService.getCurrentUser().getId()")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id,
                                                      @Valid @RequestBody ScheduleUpdateDTO updateDTO) {
        ScheduleDTO schedule = scheduleService.updateSchedule(id, updateDTO);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @scheduleService.getScheduleById(#id).getEmployeeId() == @authService.getCurrentUser().getId()")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}

