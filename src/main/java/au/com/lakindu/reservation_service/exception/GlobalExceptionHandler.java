package au.com.lakindu.reservation_service.exception;

import au.com.lakindu.reservation_service.model.ApiError;
import au.com.lakindu.reservation_service.model.ApiErrorBuilder;
import au.com.lakindu.reservation_service.model.ApiErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleException(Exception exception,
                                                               HttpServletRequest request) {
        log.error("message=\"Exception in reservation api\", exception=", exception);
        return new ResponseEntity<>(ApiErrorBuilder.build(exception, ApiErrorStatus.INTERNAL_SERVER_ERROR), ApiErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    @ExceptionHandler({ReservationException.class})
    public ResponseEntity<ApiError> handleReservationException(ReservationException exception,
                                                               HttpServletRequest request) {
        log.error("message=\"ReservationException in reservation api\", exception=", exception);
        return new ResponseEntity<>(ApiErrorBuilder.build(exception), ApiErrorStatus.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                               HttpServletRequest request) {
        log.error("message=\"MethodArgumentNotValidException in reservation api\", exception=", exception);
        return new ResponseEntity<>(ApiErrorBuilder.build(exception, ApiErrorStatus.BAD_REQUEST), ApiErrorStatus.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException exception,
                                                                       HttpServletRequest request) {
        log.error("message=\"MethodArgumentTypeMismatchException in reservation api\", exception=", exception);
        return new ResponseEntity<>(ApiErrorBuilder.build(exception, ApiErrorStatus.BAD_REQUEST), ApiErrorStatus.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception,
                                                                          HttpServletRequest request) {
        log.error("message=\"ConstraintViolationException in reservation api\", exception=", exception);
        return new ResponseEntity<>(ApiErrorBuilder.build(exception, ApiErrorStatus.BAD_REQUEST), ApiErrorStatus.BAD_REQUEST.getHttpStatus());
    }
}
