package au.com.lakindu.reservation_service.controller;

import au.com.lakindu.reservation_service.model.Availability;
import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.model.ReservationStatus;
import au.com.lakindu.reservation_service.service.ReservationService;
import au.com.lakindu.reservation_service.validation.RegexConstantsHelper;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(path = "v1/availableSlots/{date}", produces = APPLICATION_JSON_VALUE)
    public List<Availability> getAvailability(@PathVariable("date") @Pattern(regexp = RegexConstantsHelper.DATE) final String date) {
        return reservationService.getAvailability(date);
    }

    @GetMapping(path = "v1/reservations/{date}", produces = APPLICATION_JSON_VALUE)
    public List<ReservationResponse> getReservations(@PathVariable("date") @Pattern(regexp = RegexConstantsHelper.DATE) final String date) {
        return reservationService.getReservations(date);
    }

    @GetMapping(path = "v1/reservation/{id}", produces = APPLICATION_JSON_VALUE)
    public ReservationResponse getReservation(@PathVariable("id") final int id) {
        return reservationService.getReservation(id);
    }

    @PostMapping(path = "v1/reservations", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ReservationStatus addReservation(@Valid @RequestBody @NotNull final ReservationRequest reservationRequest) {
        return reservationService.addReservation(reservationRequest);
    }

    @PutMapping(path = "v1/reservations/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ReservationStatus getReservation(@PathVariable("id") final int id, @Valid @RequestBody @NotNull final ReservationRequest reservationRequest) {
        return reservationService.updateReservation(id, reservationRequest);
    }

    @DeleteMapping(path = "v1/reservations/{id}", produces = APPLICATION_JSON_VALUE)
    public ReservationStatus deleteReservations(@PathVariable("id") final int id) {
        return reservationService.deleteReservation(id);
    }
}
