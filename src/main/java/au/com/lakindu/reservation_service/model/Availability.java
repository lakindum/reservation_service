package au.com.lakindu.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Availability {
    private String tableName;
    private String availableDate;
    private String availableTime;
}
