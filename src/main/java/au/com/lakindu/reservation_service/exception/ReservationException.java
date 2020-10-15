package au.com.lakindu.reservation_service.exception;

import au.com.lakindu.reservation_service.model.ApiErrorStatus;
import lombok.Getter;

public class ReservationException extends RuntimeException {

    private static final long serialVersionUID = 299666003706812154L;

    @Getter
    private final ApiErrorStatus apiErrorStatus;

    public ReservationException(ApiErrorStatus apiErrorStatus) {
        this.apiErrorStatus = apiErrorStatus;
    }

    public ReservationException(String message, ApiErrorStatus apiErrorStatus) {
        super(message);
        this.apiErrorStatus = apiErrorStatus;
    }
}
