package mvcrest.spring6mvcrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

// Esta es una tecnica para manejar excepciones
@Slf4j
//@ControllerAdvice
public class ExceptionController {
    //@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFoundException() {
        log.debug("we are in global not found handlerNotFoundException");

        return ResponseEntity.notFound().build();
    }
}
