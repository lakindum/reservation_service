package au.com.lakindu.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApiError {
    private final String errorId;
    private final String message;
    private List<ApiErrorDetails> details;

    public ApiError(HttpStatus httpStatus, Throwable exception) {
        this.errorId = String.valueOf(httpStatus.value());
        this.message = exception.getMessage();
    }

    public ApiError(HttpStatus httpStatus, Throwable exception, List<ApiErrorDetails> errorDetails) {
        this.errorId = String.valueOf(httpStatus.value());
        this.message = exception.getMessage();
        this.details = errorDetails;
    }
}
