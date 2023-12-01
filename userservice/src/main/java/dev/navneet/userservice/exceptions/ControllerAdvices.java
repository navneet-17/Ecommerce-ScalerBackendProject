package dev.navneet.userservice.exceptions;

import dev.navneet.userservice.dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ExceptionDto> handleNotFoundException(
            NotFoundException notFoundException) {

        return new ResponseEntity<>(
                new ExceptionDto(HttpStatus.NOT_FOUND, notFoundException.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(JwtValidationException.class)
    private ResponseEntity<ExceptionDto> handleJwtValidationException(
            JwtValidationException jwtValidationException) {

        return new ResponseEntity<>(
                new ExceptionDto(HttpStatus.UNAUTHORIZED, jwtValidationException.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }
}
