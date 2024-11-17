package com.github.djoarns.payflow.application.handler;

import com.github.djoarns.payflow.application.bill.dto.response.ErrorResponseDTO;
import com.github.djoarns.payflow.application.bill.mapper.ErrorMapper;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillOperationException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorMapper errorMapper;

    @ExceptionHandler(InvalidBillDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidBillData(InvalidBillDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.toErrorDTO(ex));
    }

    @ExceptionHandler(InvalidBillStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidBillStatus(InvalidBillStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMapper.toErrorDTO(ex));
    }

    @ExceptionHandler(InvalidBillOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidBillOperation(InvalidBillOperationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMapper.toErrorDTO(ex));
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidUserData(InvalidUserDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.toErrorDTO(ex));
    }

    @ExceptionHandler(InvalidUserOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidUserOperation(InvalidUserOperationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMapper.toErrorDTO(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "Invalid username or password",
                        "AUTHENTICATION_ERROR",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError fieldError ?
                    fieldError.getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        errorMessage,
                        "VALIDATION_ERROR",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Parameter '%s' with value '%s' could not be converted to type %s",
                ex.getName(),
                ex.getValue(),
                getRequiredTypeName(ex)
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        message,
                        "TYPE_MISMATCH_ERROR",
                        LocalDateTime.now()
                ));
    }

    private String getRequiredTypeName(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        return requiredType != null ? requiredType.getSimpleName() : "Unknown";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMapper.toErrorDTO(ex));
    }
}