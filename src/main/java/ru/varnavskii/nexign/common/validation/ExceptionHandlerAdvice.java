package ru.varnavskii.nexign.common.validation;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.varnavskii.nexign.common.exception.FileException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> handleFileException(FileException ex) {
        var errMsg = String.format("Can't generate report: %s", ex.getMessage());
        return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
    }
}
