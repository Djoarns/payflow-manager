package com.github.djoarns.payflow.application.handler;

import com.github.djoarns.payflow.application.bill.dto.response.ErrorResponseDTO;
import com.github.djoarns.payflow.application.bill.mapper.ErrorMapper;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillOperationException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserOperationException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest extends BaseUnitTest {

    @Mock
    private ErrorMapper errorMapper;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(errorMapper);
    }

    @Nested
    @DisplayName("Bill Exceptions")
    class BillExceptions {

        @Test
        @DisplayName("Should handle InvalidBillDataException")
        void shouldHandleInvalidBillDataException() {
            // Arrange
            var exception = new InvalidBillDataException("Invalid bill data");
            var errorDTO = new ErrorResponseDTO("Invalid bill data", "BUSINESS_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleInvalidBillData(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }

        @Test
        @DisplayName("Should handle InvalidBillStatusException")
        void shouldHandleInvalidBillStatusException() {
            // Arrange
            var exception = new InvalidBillStatusException("Invalid status");
            var errorDTO = new ErrorResponseDTO("Invalid status", "BUSINESS_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleInvalidBillStatus(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }

        @Test
        @DisplayName("Should handle InvalidBillOperationException")
        void shouldHandleInvalidBillOperationException() {
            // Arrange
            var exception = new InvalidBillOperationException("Invalid operation");
            var errorDTO = new ErrorResponseDTO("Invalid operation", "BUSINESS_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleInvalidBillOperation(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }
    }

    @Nested
    @DisplayName("User Exceptions")
    class UserExceptions {

        @Test
        @DisplayName("Should handle InvalidUserDataException")
        void shouldHandleInvalidUserDataException() {
            // Arrange
            var exception = new InvalidUserDataException("Invalid user data");
            var errorDTO = new ErrorResponseDTO("Invalid user data", "BUSINESS_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleInvalidUserData(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }

        @Test
        @DisplayName("Should handle InvalidUserOperationException")
        void shouldHandleInvalidUserOperationException() {
            // Arrange
            var exception = new InvalidUserOperationException("Invalid operation");
            var errorDTO = new ErrorResponseDTO("Invalid operation", "BUSINESS_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleInvalidUserOperation(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }
    }

    @Nested
    @DisplayName("Authentication Exceptions")
    class AuthenticationExceptions {

        @Test
        @DisplayName("Should handle BadCredentialsException")
        void shouldHandleBadCredentialsException() {
            // Arrange
            var exception = new BadCredentialsException("Bad credentials");

            // Act
            var response = handler.handleBadCredentials(exception);

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Invalid username or password", response.getBody().message());
            assertEquals("AUTHENTICATION_ERROR", response.getBody().code());
            assertNotNull(response.getBody().timestamp());
        }
    }

    @Nested
    @DisplayName("Validation Exceptions")
    class ValidationExceptions {

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with field errors")
        void shouldHandleMethodArgumentNotValidExceptionWithFieldErrors() {
            // Arrange
            var mockBindingResult = mock(BindingResult.class);
            var fieldError = new FieldError("bill", "amount", "Amount is required");
            when(mockBindingResult.getAllErrors()).thenReturn(List.of(fieldError));
            var exception = new MethodArgumentNotValidException(null, mockBindingResult);

            // Act
            var response = handler.handleValidationErrors(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("amount: Amount is required", response.getBody().message());
            assertEquals("VALIDATION_ERROR", response.getBody().code());
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with object errors")
        void shouldHandleMethodArgumentNotValidExceptionWithObjectErrors() {
            // Arrange
            var mockBindingResult = mock(BindingResult.class);
            var objectError = new ObjectError("bill", "Invalid bill");
            when(mockBindingResult.getAllErrors()).thenReturn(List.of(objectError));
            var exception = new MethodArgumentNotValidException(null, mockBindingResult);

            // Act
            var response = handler.handleValidationErrors(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("bill: Invalid bill", response.getBody().message());
            assertEquals("VALIDATION_ERROR", response.getBody().code());
        }

        @Test
        @DisplayName("Should handle MethodArgumentTypeMismatchException")
        void shouldHandleMethodArgumentTypeMismatchException() {
            // Arrange
            var exception = mock(MethodArgumentTypeMismatchException.class);
            when(exception.getName()).thenReturn("amount");
            when(exception.getValue()).thenReturn("abc");
            when(exception.getRequiredType()).thenReturn((Class) Integer.class);

            // Act
            var response = handler.handleTypeMismatch(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Parameter 'amount' with value 'abc' could not be converted to type Integer", response.getBody().message());
            assertEquals("TYPE_MISMATCH_ERROR", response.getBody().code());
        }

        @Test
        @DisplayName("Should handle MethodArgumentTypeMismatchException with null required type")
        void shouldHandleMethodArgumentTypeMismatchExceptionWithNullRequiredType() {
            // Arrange
            var exception = mock(MethodArgumentTypeMismatchException.class);
            when(exception.getName()).thenReturn("amount");
            when(exception.getValue()).thenReturn("abc");
            when(exception.getRequiredType()).thenReturn(null);

            // Act
            var response = handler.handleTypeMismatch(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Parameter 'amount' with value 'abc' could not be converted to type Unknown", response.getBody().message());
            assertEquals("TYPE_MISMATCH_ERROR", response.getBody().code());
        }
    }

    @Nested
    @DisplayName("General Exceptions")
    class GeneralExceptions {

        @Test
        @DisplayName("Should handle general Exception")
        void shouldHandleGeneralException() {
            // Arrange
            var exception = new RuntimeException("Unexpected error");
            var errorDTO = new ErrorResponseDTO("Unexpected error", "SYSTEM_ERROR", LocalDateTime.now());
            when(errorMapper.toErrorDTO(any())).thenReturn(errorDTO);

            // Act
            var response = handler.handleGeneralException(exception);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals(errorDTO, response.getBody());
            verify(errorMapper).toErrorDTO(exception);
        }
    }
}