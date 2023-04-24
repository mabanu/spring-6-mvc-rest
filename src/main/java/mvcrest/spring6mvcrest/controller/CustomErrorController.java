package mvcrest.spring6mvcrest.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {

        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map( fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

                    return errorMap;
                }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler
    ResponseEntity<Object> handleJpaViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder response = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException constraintViolationException) {

            List<Map<String, String>> errors = constraintViolationException.getConstraintViolations().stream()
                    .map( constraintViolation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put( constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());

                        return errorMap;
                    }).toList();

            return response.body(errors);
        }

        return response.build();
    }
}
