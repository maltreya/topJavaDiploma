package com.topjava.restaurantvoiting.web;

import com.topjava.restaurantvoiting.util.ValidationUtil;
import com.topjava.restaurantvoiting.util.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static com.topjava.restaurantvoiting.util.exception.ErrorType.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@Slf4j
public class ExceptionInfoHandler {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with this name already exists";
    public static final String EXCEPTION_DUPLICATE_FOOD = "Food with this restaurant already exists for current date";
    public static final String EXCEPTION_DUPLICATE_VOTE = "Vote with this user already exists for current date";

    private static final Map<String, String> CONSTRAINTS_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_idx", EXCEPTION_DUPLICATE_RESTAURANT,
            "food_unique_restaurant_prep_date_description_idx", EXCEPTION_DUPLICATE_FOOD,
            "vote_unique_user_vote_date_idx", EXCEPTION_DUPLICATE_VOTE);

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return new ErrorInfo(req.getRequestURL(), errorType,
                details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)});
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotWritableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo handleValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(f -> String.format("[%s] %s", f.getField(), f.getDefaultMessage()))
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(OutOfTimeException.class)
    public ErrorInfo outOfTimeRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, OUT_OF_TIME);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMessage = ValidationUtil.getRootCause(e).getMessage();
        if (rootMessage != null) {
            String lowerCaseMessage = rootMessage.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS_MAP.entrySet()) {
                if (lowerCaseMessage.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, entry.getValue());
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }
}
