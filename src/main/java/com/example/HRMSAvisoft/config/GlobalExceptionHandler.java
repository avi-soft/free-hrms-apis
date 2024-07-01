package com.example.HRMSAvisoft.config;

import com.example.HRMSAvisoft.exception.*;
import com.example.HRMSAvisoft.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>>handlesValidationErrors(MethodArgumentNotValidException exception) {
        HttpStatus status;
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        Map<String,Object>responseData=new HashMap<>();
        responseData.put("message",errors);
        status= HttpStatus.BAD_REQUEST;
        responseData.put("Success",false);
        return ResponseEntity.status(status).body(responseData);
    }

    @ExceptionHandler({
           EmployeeNotFoundException.class,
            NullPointerException.class,
            AddressService.AddressNotFoundException.class,
            EmergencyContactNotFoundException.class

    })
    public ResponseEntity<Map<String,Object>> handleErrors(Exception exception){
        Map<String ,Object> responseData = new HashMap<>();
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.NOT_FOUND;
        }
       else if (exception instanceof EntityNotFoundException) {
            responseData.put("message",exception.getMessage());
            status = HttpStatus.NOT_FOUND;

        }else if(exception instanceof NullPointerException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.BAD_REQUEST;
        }else if(exception instanceof EmergencyContactNotFoundException)
        {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.BAD_REQUEST;
        }
        else{
            responseData.put("message","Something went wrong");
            status=HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseData.put("Success",false);
        return ResponseEntity.status(status).body(responseData);
    }
    @ExceptionHandler({InsufficientLeaveBalanceException.class, LeaveRequestNotFoundException.class,OverlappingLeaveRequestException.class})
    public ResponseEntity<Map<String,Object>>handlesLeaveErrors(Exception exception){
        Map<String ,Object> responseData = new HashMap<>();
        HttpStatus httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
        if(exception instanceof LeaveRequestNotFoundException){
            responseData.put("message",exception.getMessage());
            httpStatus=HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof InsufficientLeaveBalanceException ){
            responseData.put("message",exception.getMessage());
        }
        else if(exception instanceof OverlappingLeaveRequestException){
            responseData.put("message",exception.getMessage());
        }
        responseData.put("Success",false);
        return ResponseEntity.status(httpStatus).body(responseData);
    }



}
