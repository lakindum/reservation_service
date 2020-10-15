package au.com.lakindu.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorStatus {

    BAD_REQUEST("API-400", "Bad Request", "Invalid Request Parameters", HttpStatus.BAD_REQUEST),
    NOT_FOUND("API-404", "Not Found", "Resource Not Found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("API-500", "Internal Server Error", "Server Failure", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorId;
    private final String message;
    private final String issue;
    private final HttpStatus httpStatus;
}
