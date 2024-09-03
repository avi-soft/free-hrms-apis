package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.AttendanceLocation;
import com.example.HRMSAvisoft.exception.AttributeKeyAlreadyExistsException;
import com.example.HRMSAvisoft.service.AttendanceLocationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendanceLocation")
public class AttendanceLocationController {

    private final AttendanceLocationService attendanceLocationService;

    public AttendanceLocationController(AttendanceLocationService attendanceLocationService){
        this.attendanceLocationService = attendanceLocationService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addAttendanceLocation(@RequestBody AttendanceLocation attendanceLocation)throws IllegalArgumentException{
        AttendanceLocation newAttendanceLocation = attendanceLocationService.addAttendanceLocation(attendanceLocation);

        return  ResponseEntity.status(201).body(Map.of("success", true, "message", "New Location added successfully", "attendanceLocation", newAttendanceLocation));
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllAttendanceLocation(){
        List<AttendanceLocation> attendanceLocationList = attendanceLocationService.getAllAttendanceLocation();

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Attendance Locations fetched successfully", "AttendanceLocations", attendanceLocationList));
    }

    @PatchMapping("/{attendanceLocationId}")
    public ResponseEntity<Map<String, Object>> updateAttendanceLocation(@PathVariable("attendanceLocationId") Long attendanceLocationId, @RequestBody AttendanceLocation attendanceLocation)throws EntityNotFoundException, IllegalArgumentException{
        AttendanceLocation updatedAttendanceLocation = attendanceLocationService.updateAttendanceLocation(attendanceLocationId, attendanceLocation);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Attendance Location updated", "updatedLocation", updatedAttendanceLocation));
    }

    @DeleteMapping("/{attendanceLocationId}")
    public ResponseEntity<Map<String, Object>> deleteAttendanceLocation(@PathVariable("attendanceLocationId") Long attendanceLocationId)throws EntityNotFoundException {
        attendanceLocationService.deleteAttendanceLocation(attendanceLocationId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Attendance Location Deleted"));
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof IllegalArgumentException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }else {
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
