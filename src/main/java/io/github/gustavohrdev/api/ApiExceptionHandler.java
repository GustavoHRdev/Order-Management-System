package io.github.gustavohrdev.api;

import io.github.gustavohrdev.api.dto.response.ApiErrorResponse;
import io.github.gustavohrdev.exception.BusinessRuleException;
import io.github.gustavohrdev.exception.NotFoundException;
import io.github.gustavohrdev.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                                                      HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Dados de entrada inválidos.");
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, new HttpHeaders());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception,
                                                                                                HttpServletRequest request) {
        String message = exception.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Dados de entrada inválidos.");
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, new HttpHeaders());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException exception,
                                                                                               HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "JSON inválido.", request, new HttpHeaders());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception,
                                                                                             HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (exception.getSupportedHttpMethods() != null && !exception.getSupportedHttpMethods().isEmpty()) {
            headers.setAllow(exception.getSupportedHttpMethods());
        }
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido.", request, headers);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, NoHandlerFoundException.class})
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleBadRouteInput(Exception exception,
                                                                                          HttpServletRequest request) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            return buildResponse(HttpStatus.BAD_REQUEST, "ID inválido.", request, new HttpHeaders());
        }
        return buildResponse(HttpStatus.NOT_FOUND, "Rota não encontrada.", request, new HttpHeaders());
    }

    @ExceptionHandler(NotFoundException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException exception,
                                                                                     HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request, new HttpHeaders());
    }

    @ExceptionHandler({ValidationException.class, BusinessRuleException.class})
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleBusinessExceptions(RuntimeException exception,
                                                                                               HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request, new HttpHeaders());
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleGeneric(Exception exception,
                                                                                    HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.", request, new HttpHeaders());
    }

    private org.springframework.http.ResponseEntity<ApiErrorResponse> buildResponse(HttpStatusCode status,
                                                                                    String message,
                                                                                    HttpServletRequest request,
                                                                                    HttpHeaders headers) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now().toString(),
                status.value(),
                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return new org.springframework.http.ResponseEntity<>(body, headers, status);
    }
}

