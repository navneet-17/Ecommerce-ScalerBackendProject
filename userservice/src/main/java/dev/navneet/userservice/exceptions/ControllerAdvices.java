package dev.navneet.userservice.exceptions;

import dev.navneet.userservice.dtos.ExceptionDto;
import dev.navneet.userservice.dtos.UserDto;
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
                new ExceptionDto(notFoundException.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(JwtValidationException.class)
    private ResponseEntity<ExceptionDto> handleJwtValidationException(
            JwtValidationException jwtValidationException) {

        return new ResponseEntity<>(
                new ExceptionDto(jwtValidationException.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<ExceptionDto> handleInvalidPasswordResetTokenException(InvalidPasswordResetTokenException ex) {
        return new ResponseEntity<>(
                new ExceptionDto(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDto> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex) {
        return new ResponseEntity<>(
                new ExceptionDto(ex.getMessage()),
                HttpStatus.CONFLICT
        );
//        UserDto userDto = new UserDto();
//        userDto.setMessage(ex.getMessage());
//        return new ResponseEntity<>(userDto, HttpStatus.CONFLICT);  // 409 Conflict
    }

}
