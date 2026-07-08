package com.ecommerce.exception;
import com.ecommerce.dto.response.ApiResponse;
import org.springframework.http.*;import org.springframework.security.access.AccessDeniedException;import org.springframework.security.authentication.BadCredentialsException;import org.springframework.validation.FieldError;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex){return ResponseEntity.status(404).body(ApiResponse.error(ex.getMessage()));}
    @ExceptionHandler(BadRequestException.class) public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex){return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage()));}
    @ExceptionHandler(UnauthorizedException.class) public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex){return ResponseEntity.status(401).body(ApiResponse.error(ex.getMessage()));}
    @ExceptionHandler(BadCredentialsException.class) public ResponseEntity<ApiResponse<?>> handleBadCreds(BadCredentialsException ex){return ResponseEntity.status(401).body(ApiResponse.error("Invalid email or password"));}
    @ExceptionHandler(AccessDeniedException.class) public ResponseEntity<ApiResponse<?>> handleAccess(AccessDeniedException ex){return ResponseEntity.status(403).body(ApiResponse.error("Access denied"));}
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex){String errors=ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));return ResponseEntity.status(400).body(ApiResponse.error(errors));}
    @ExceptionHandler(Exception.class) public ResponseEntity<ApiResponse<?>> handleAll(Exception ex){return ResponseEntity.status(500).body(ApiResponse.error("Internal server error: "+ex.getMessage()));}
}
