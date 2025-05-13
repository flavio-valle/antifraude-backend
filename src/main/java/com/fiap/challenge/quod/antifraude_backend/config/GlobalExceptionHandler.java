package com.fiap.challenge.quod.antifraude_backend.config;

import com.fiap.challenge.quod.antifraude_backend.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.WebRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) erros de validação @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos",
                errors
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 2) ResponseStatusException (NOT_FOUND, UNAUTHORIZED, etc)
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ApiError> handleStatusException(ResponseStatusException ex) {
        ApiError body = new ApiError(
                ex.getStatusCode().value(),
                ex.getReason(),
                List.of()
        );
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    // 3) fallback para outras exceções
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request) {
        ApiError body = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado",
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
