package com.education.rag.exception;

import java.time.Instant;
import java.util.Map;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.education.rag.exception.CustomExceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Map<String,Object>> response(HttpStatus status,String detail){
        return ResponseEntity.status(status).body(Map.of("detail",detail,"status",status.value(),"timestamp",Instant.now().toString()));
    }
    @ExceptionHandler(UserAlreadyExistsException.class) public ResponseEntity<Map<String,Object>> exists(UserAlreadyExistsException e){return response(HttpStatus.CONFLICT,e.getMessage());}
    @ExceptionHandler({InvalidCredentialsException.class,BadCredentialsException.class}) public ResponseEntity<Map<String,Object>> credentials(Exception e){return response(HttpStatus.UNAUTHORIZED,"Invalid email or password");}
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<Map<String,Object>> notFound(ResourceNotFoundException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
    @ExceptionHandler(PythonServiceException.class) public ResponseEntity<Map<String,Object>> python(PythonServiceException e){HttpStatus s=e.getStatus()>=400&&e.getStatus()<=599?HttpStatus.valueOf(e.getStatus()):HttpStatus.BAD_GATEWAY;return response(s,e.getMessage());}
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String,Object>> invalid(MethodArgumentNotValidException e){String detail=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Invalid request");return response(HttpStatus.BAD_REQUEST,detail);}
    @ExceptionHandler(ConstraintViolationException.class) public ResponseEntity<Map<String,Object>> violation(ConstraintViolationException e){return response(HttpStatus.BAD_REQUEST,e.getMessage());}
    @ExceptionHandler(MaxUploadSizeExceededException.class) public ResponseEntity<Map<String,Object>> tooLarge(MaxUploadSizeExceededException e){return response(HttpStatus.PAYLOAD_TOO_LARGE,"Uploaded file exceeds the size limit");}
    @ExceptionHandler({HttpMessageNotReadableException.class,DataIntegrityViolationException.class}) public ResponseEntity<Map<String,Object>> badData(Exception e){return response(HttpStatus.BAD_REQUEST,"Invalid request data");}
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<Map<String,Object>> badArgument(IllegalArgumentException e){return response(HttpStatus.BAD_REQUEST,e.getMessage());}
    @ExceptionHandler(AccessDeniedException.class) public ResponseEntity<Map<String,Object>> forbidden(AccessDeniedException e){return response(HttpStatus.FORBIDDEN,"Access denied");}
    @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,Object>> unexpected(Exception e){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error");}
}
