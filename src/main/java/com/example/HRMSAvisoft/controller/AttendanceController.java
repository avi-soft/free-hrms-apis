package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AttendanceRequestDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Attendance;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.service.AttendanceService;
import com.example.HRMSAvisoft.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startClockIn(@RequestBody AttendanceRequestDTO attendanceRequestDTO) throws IllegalArgumentException, AttendanceService.AlreadyClockedInException {
        attendanceService.startClockIn(attendanceRequestDTO.getUserId(), attendanceRequestDTO.getLatitude(), attendanceRequestDTO.getLongitude(), attendanceRequestDTO.getElevation());
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Clocked-in Successfully"));
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stopClockOut(@RequestBody AttendanceRequestDTO attendanceRequestDTO) throws EntityNotFoundException, AttendanceService.InsufficientTimeForAttendanceException {
        Attendance attendance = attendanceService.stopClockOut(attendanceRequestDTO.getUserId(), attendanceRequestDTO.getLatitude(), attendanceRequestDTO.getLongitude(), attendanceRequestDTO.getElevation());
        if (attendance.getAttendanceMarked()) {
            return ResponseEntity.status(200).body(Map.of("success", true, "message", "Clocked-out success, Attendance marked"));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Clock Out failed. Attendance not marked."));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<Attendance>> getUserAttendanceByMonth(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Attendance> attendancePage = attendanceService.getUserAttendanceByMonth(userId, month, page, size);
        return ResponseEntity.status(200).body(attendancePage);
    }

    @PreAuthorize("hasAuthority('GET_ALL_ATTENDANCE')")
    @GetMapping("")
    public ResponseEntity<Page<Attendance>> getAllAttendanceByMonth(
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Attendance> attendancePage = attendanceService.getAllAttendanceByMonth(month, page, size);

        return ResponseEntity.status(200).body(attendancePage);
    }

    @GetMapping("/userStatus/{userId}")
    public ResponseEntity<Map<String, Object>> userClockedIn(@PathVariable Long userId){
        boolean isUserClockedIn = attendanceService.userClockedIn(userId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "User status fetched", "isUserClockedIn", isUserClockedIn));
    }

    @PreAuthorize("hasAuthority('UPDATE_ATTENDANCE_RECORD')")
    @PatchMapping("/{attendanceId}")
    public ResponseEntity<Map<String, Object>> updateAttendanceRecord(@PathVariable("attendanceId") Long attendanceId, @RequestBody Attendance attendance) {
        attendanceService.updateAttendanceRecord(attendanceId, attendance);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Attendance updated successfully."));
    }


    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalArgumentException.class,
            AttendanceService.AlreadyClockedInException.class,
            AttendanceService.InsufficientTimeForAttendanceException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof AttendanceService.AlreadyClockedInException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof AttendanceService.InsufficientTimeForAttendanceException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
