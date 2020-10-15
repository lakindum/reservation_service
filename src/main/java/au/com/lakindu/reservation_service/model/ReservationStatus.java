package au.com.lakindu.reservation_service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationStatus {
    private final String id;
    private final Status status;
}
