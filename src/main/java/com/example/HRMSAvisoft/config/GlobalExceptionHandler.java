package com.example.HRMSAvisoft.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.exception.*;
import com.example.HRMSAvisoft.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import utils.ResponseGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> accessDeniedHandler()
    {
        return ResponseGenerator.generateResponse(HttpStatus.FORBIDDEN,false,"Access is denied for current user",null);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> entityNotFoundException()
    {
        return ResponseGenerator.generateResponse(HttpStatus.NOT_FOUND,false,"Entity does not exists",null);
    }
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> TokenExpiredException()
    {
        return ResponseGenerator.generateResponse(HttpStatus.UNAUTHORIZED,false,"Session Expired. Please login again.",null);
    }

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

    @ExceptionHandler(AttributeKeyDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity <Object> handleAttributeKeyDoesNotExistException(AttributeKeyDoesNotExistException exception)
    {
        return ResponseGenerator.generateResponse(HttpStatus.FORBIDDEN,false,"Access is denied for current user",null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String message = "File size should be less than 5MB";
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
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
        }
       else if(exception instanceof EmergencyContactNotFoundException)
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
