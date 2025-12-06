package com.example.videowatchlog.presentation.exception;

import com.example.videowatchlog.domain.exception.TitleDuplicateException;
import com.example.videowatchlog.domain.exception.TitleNotFoundException;
import com.example.videowatchlog.domain.exception.InvalidWatchStatusTransitionException;
import com.example.videowatchlog.presentation.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * GlobalExceptionHandler - グローバルエラーハンドリング
 * @ControllerAdvice を使用してアプリケーション全体のエラーをハンドリング
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TitleDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDTO> handleTitleDuplicateException(
            TitleDuplicateException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                ex.getMessage(),
                "TITLE_DUPLICATE",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(TitleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleTitleNotFoundException(
            TitleNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                ex.getMessage(),
                "TITLE_NOT_FOUND",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidWatchStatusTransitionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleInvalidWatchStatusTransitionException(
            InvalidWatchStatusTransitionException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                ex.getMessage(),
                "INVALID_WATCH_STATUS_TRANSITION",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation failed");
        ErrorResponseDTO response = new ErrorResponseDTO(
                message,
                "VALIDATION_FAILED",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Constraint violation");
        ErrorResponseDTO response = new ErrorResponseDTO(
                message,
                "CONSTRAINT_VIOLATION",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                ex.getMessage(),
                "NOT_FOUND",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleIllegalStateException(
            IllegalStateException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                ex.getMessage(),
                "BAD_REQUEST",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "An unexpected error occurred",
                "INTERNAL_SERVER_ERROR",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
