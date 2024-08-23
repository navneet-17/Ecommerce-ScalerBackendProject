package dev.navneet.productservice.exceptions;

import dev.navneet.productservice.dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


    @ControllerAdvice
    public class ControllerAdvices {
        @ExceptionHandler(NotFoundException.class)
        private ResponseEntity<ExceptionDto> handleNotFoundException(
                NotFoundException notFoundException
        ) {

            return new ResponseEntity<>(
                    new ExceptionDto(HttpStatus.NOT_FOUND, notFoundException.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }

        @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
        private ResponseEntity<ExceptionDto> handleArrayIndexOutOfBound(
                ArrayIndexOutOfBoundsException notFoundException
        ) {

            return new ResponseEntity<>(
                    new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, notFoundException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @ExceptionHandler(InvalidSortTypeParameter.class)
        private ResponseEntity<ExceptionDto> handleInvalidSortTypeParameter(
                InvalidSortTypeParameter invalidSortTypeParameter
        ){

            return new ResponseEntity<>(
                    new ExceptionDto(HttpStatus.BAD_REQUEST, invalidSortTypeParameter.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
