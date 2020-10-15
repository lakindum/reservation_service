package au.com.lakindu.reservation_service.model;

import au.com.lakindu.reservation_service.validation.RegexConstantsHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class ReservationRequest implements Serializable {
    private static final long serialVersionUID = -7376098096796124748L;

    @NotNull
    private final String name;
    @NotNull
    private final String contact;
    @NotNull @Pattern(regexp = RegexConstantsHelper.DATE)
    private final String reservationDate;
    @NotNull @Pattern(regexp = RegexConstantsHelper.TIMESLOT)
    private final String reservationTime;
    @NotNull
    private final String tableName;
}
