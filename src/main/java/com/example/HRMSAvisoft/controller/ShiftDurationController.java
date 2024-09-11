package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.dto.ShiftDurationDTO;
import com.example.HRMSAvisoft.entity.ShiftDuration;
import com.example.HRMSAvisoft.service.ShiftDurationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shiftDuration")
public class ShiftDurationController {

    private final ShiftDurationService shiftDurationService;

    public ShiftDurationController(ShiftDurationService shiftDurationService) {
        this.shiftDurationService = shiftDurationService;
    }

    @PreAuthorize("hasAuthority('ADD_SHIFT_DURATION')")
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> saveShiftDuration(@RequestBody ShiftDurationDTO shiftDurationDTO)throws IllegalStateException{
        ShiftDuration newShiftDuration = shiftDurationService.saveShiftDuration(shiftDurationDTO);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Shift Duration added", "ShiftDuration", newShiftDuration));
    }

    @PreAuthorize("hasAuthority('GET_SHIFT_DURATION')")
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getShiftDuration() {

        List<ShiftDuration> shiftDurationList = shiftDurationService.getShiftDurations();

        return ResponseEntity.status(200).body(Map.of("message", "Shift Duration Fetched", "success", true, "ShiftDurationList", shiftDurationList));
    }

    @PreAuthorize("hasAuthority('UPDATE_SHIFT_DURATION')")
    @PatchMapping("")
    public ResponseEntity<Map<String, Object>> updateShiftDuration(@RequestBody ShiftDurationDTO shiftDurationDTO)throws EntityNotFoundException {
        ShiftDuration updatedShiftDuration = shiftDurationService.updateShiftDuration(shiftDurationDTO);

        if(updatedShiftDuration != null) {
            return ResponseEntity.status(200).body(Map.of("message", "Shift Duration updated successfully", "success", true, "UpdatedShiftDuration", updatedShiftDuration));
        }
        else{
            return ResponseEntity.status(404).body(Map.of("message", "No Duration found Create a new Duration.", "success", "false"));
        }
    }

    @PreAuthorize("hasAuthority('DELETE_SHIFT_DURATION')")
    @DeleteMapping("/{shiftDurationId}")
    public ResponseEntity<Map<String, Object>> deleteShiftDuration(@PathVariable("shiftDurationId") Long shiftDurationId){
        shiftDurationService.deleteShiftDuration(shiftDurationId);

        return ResponseEntity.status(200).body(Map.of("message","Shift Duration deleted successfully", "success", true));
    }


    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalStateException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof IllegalStateException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
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
