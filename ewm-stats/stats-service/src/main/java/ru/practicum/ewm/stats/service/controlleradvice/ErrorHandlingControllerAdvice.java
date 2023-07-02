package ru.practicum.ewm.stats.service.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.stats.service.exception.InvalidPeriodException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onMissingPathVariableException(MissingPathVariableException e) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onConstraintViolationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> {
                            String name = "";
                            for (Path.Node node : violation.getPropertyPath()) {
                                name = node.getName();
                            }
                            return new Violation(name,
                                    violation.getMessage()
                            );
                        }
                )
                .collect(Collectors.toList());
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Constraint violation exception: ");
        violations.forEach(violation -> logMessage.append(violation.getMessage()).append(", "));
        log.error(logMessage.toString());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onConversionFailedException(ConversionFailedException e) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(InvalidPeriodException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onInvalidPeriodException(
            InvalidPeriodException e
    ) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApplicationError onMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApplicationError onOtherException(
            Exception e
    ) {
        log.error(e.getMessage());
        return new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error");
    }

}