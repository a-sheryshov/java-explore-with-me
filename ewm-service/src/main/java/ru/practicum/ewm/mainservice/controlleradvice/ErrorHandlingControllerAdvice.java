package ru.practicum.ewm.mainservice.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.mainservice.controlleradvice.dto.ApiErrorDto;
import ru.practicum.ewm.mainservice.exception.InvalidPeriodException;
import ru.practicum.ewm.mainservice.exception.ObjectNotExistsException;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;
import ru.practicum.ewm.mainservice.exception.UnsupportedStateException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Method argument not valid",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Type mismatch",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onMissingPathVariableException(MissingPathVariableException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Missing path variable",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onConstraintViolationException(
            ConstraintViolationException e
    ) {
        final List<String> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> {
                            String name = "";
                            for (Path.Node node : violation.getPropertyPath()) {
                                name = node.getName();
                            }
                            return name + " " + violation.getMessage();
                        }
                )
                .collect(Collectors.toList());
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Constraint violation exception: ");
        violations.forEach(violation -> logMessage.append(violation).append(", "));
        log.error(logMessage.toString());
        return new ApiErrorDto(violations,
                e.getMessage(),
                "Invalid DTO field value",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Illegal argument",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(InvalidPeriodException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onInvalidPeriodException(
            InvalidPeriodException e
    ) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Invalid period",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Missing request parameter",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiErrorDto onDataIntegrityViolationException(
            DataIntegrityViolationException e
    ) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Data integrity violation",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler(ObjectNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorDto onObjectNotExistsException(ObjectNotExistsException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Object not exists",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(OperationFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiErrorDto onOperationFailedException(OperationFailedException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Operation failed",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler(UnsupportedStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto onUnsupportedStatusException(UnsupportedStateException e) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Unsupported state",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorDto onOtherException(
            Exception e
    ) {
        log.error(e.getMessage());
        return new ApiErrorDto(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Other error",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

}
