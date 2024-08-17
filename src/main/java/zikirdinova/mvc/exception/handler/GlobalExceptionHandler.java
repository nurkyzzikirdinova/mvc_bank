package zikirdinova.mvc.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zikirdinova.mvc.exception.*;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException exception) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistsException(AlreadyExistsException exception) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse badRequestException(BadRequestException exception) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateCodeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleDuplicateCodeException(DuplicateCodeException exception) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }


}