package au.com.lakindu.reservation_service.model;

import au.com.lakindu.reservation_service.exception.ReservationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ApiErrorBuilder {

    private ApiErrorBuilder() {
    }

    public static ApiError build(Exception exception, ApiErrorStatus apiErrorStatus) {
        return ApiError.builder().errorId(apiErrorStatus.getErrorId())
            .message(apiErrorStatus.getMessage())
            .details(buildErrorDetails(exception, apiErrorStatus)).build();
    }

    public static ApiError build(ReservationException exception) {
        return ApiError.builder().errorId(exception.getApiErrorStatus().getErrorId())
            .message(exception.getApiErrorStatus().getMessage())
            .details(buildErrorDetails(exception, exception.getApiErrorStatus())).build();
    }

    private static List<ApiErrorDetails> buildErrorDetails(Exception exception, ApiErrorStatus apiErrorStatus) {
        if (exception instanceof MethodArgumentNotValidException) {
            return buildErrorDetails((MethodArgumentNotValidException) exception);
        } else if (exception instanceof ReservationException) {
            return buildErrorDetails((ReservationException) exception);
        } else if (exception instanceof ConstraintViolationException) {
            return buildErrorDetails((ConstraintViolationException) exception);
        }
        return buildErrorDetails(apiErrorStatus);
    }

    private static List<ApiErrorDetails> buildErrorDetails(ApiErrorStatus apiErrorStatus) {
        return Collections.singletonList(ApiErrorDetails.builder()
                .issue(apiErrorStatus.getIssue()).build());
    }

    private static List<ApiErrorDetails> buildErrorDetails(ReservationException exception) {
        return Collections.singletonList(ApiErrorDetails.builder()
            .issue(exception.getMessage()).build());
    }

    private static List<ApiErrorDetails> buildErrorDetails(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
            .map(constraintViolation -> ApiErrorDetails.builder()
            .field(constraintViolation.getField())
            .issue(constraintViolation.getDefaultMessage())
            .build()).collect(Collectors.toList());
    }

    private static List<ApiErrorDetails> buildErrorDetails(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
            .map(constraintViolation -> ApiErrorDetails.builder()
                .field(constraintViolation.getPropertyPath().toString())
                .value(constraintViolation.getInvalidValue().toString())
                .issue(constraintViolation.getMessage())
                .build()).collect(Collectors.toList());
    }
}
