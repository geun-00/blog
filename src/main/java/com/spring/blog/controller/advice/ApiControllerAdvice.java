package com.spring.blog.controller.advice;

import com.spring.blog.controller.api.ApiResponse;
import com.spring.blog.exception.EmailSendException;
import com.spring.blog.exception.ResponseStatusException;
import com.spring.blog.exception.SmsException;
import com.spring.blog.exception.VerificationException;
import com.spring.blog.exception.duplicate.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleBindException(MethodArgumentNotValidException ex,
                                                   HttpServletRequest request) {

        request.setAttribute("cleanup", true);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> errors = fieldErrors
                .stream()
                .map(fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage())
                .toList();

        log.error("errors = {}", errors);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "잘못된 입력을 받았습니다.",
                null);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ApiResponse<Object> handleDuplicateException(DuplicateException ex) {

        log.error("데이터 중복 예외 발생");

        return ApiResponse.of(
                HttpStatus.CONFLICT,
                "가입되어 있는 정보가 있습니다.",
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {

        log.error(ex.getMessage());

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "잘못된 입력을 받았습니다.",
                null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ResponseStatusException.class)
    public ApiResponse<Object> handleResponseStatusException(ResponseStatusException ex) {
        log.error(ex.getMessage());
        ex.printStackTrace(System.out);

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "오류가 발생했습니다."
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<Object> handleEntityNotFoundException(EntityNotFoundException ex,
                                                             HttpServletRequest request) {
        request.setAttribute("cleanup", true);
        log.error(ex.getMessage());

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "오류가 발생했습니다."
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VerificationException.class)
    public ApiResponse<Object> handleVerificationException(VerificationException ex) {

        log.error(ex.getMessage());

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "인증에 실패했습니다.",
                null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SmsException.class, EmailSendException.class})
    public ApiResponse<Object> handleSmsException(Exception ex) {

        log.error(ex.getMessage());

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "인증번호 전송에 실패했습니다.",
                null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ApiResponse<Object> handleException(AuthorizationDeniedException ex) {

        log.error("권한 오류, 내용 : {}", ex.getAuthorizationResult());
        ex.printStackTrace(System.out);

        return ApiResponse.of(
                HttpStatus.FORBIDDEN,
                "오류가 발생했습니다."
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(JSONException.class)
    public ApiResponse<Object> handleException(JSONException ex) {

        log.error("JSON 데이터 파싱 중 오류가 발생했습니다. 내용 : {}", ex.getMessage());

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "오류가 발생했습니다."
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception ex, HttpServletRequest request) {

        request.setAttribute("cleanup", true);
        log.error("예상하지 못한 오류가 발생했습니다. 내용 : {}", ex.getMessage());
        ex.printStackTrace(System.out);

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "오류가 발생했습니다."
        );
    }
}