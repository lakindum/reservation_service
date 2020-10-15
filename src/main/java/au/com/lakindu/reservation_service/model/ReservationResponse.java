package au.com.lakindu.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {
    private final String id;
    private final String name;
    private final String contact;
    private final String reservationDate;
    private final String reservationTime;
    private final String tableName;
}
