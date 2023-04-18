package mvcrest.spring6mvcrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Esta es una tecnica para manejar excepciones
@Slf4j
//@ControllerAdvice
public class ExceptionController {
    //@ExceptionHandler(NotFoundException.class)
    public ResponseEntity handlerNotFoundException() {
        log.debug("we are in global not found handlerNotFoundException");

        return ResponseEntity.notFound().build();
    }
}
