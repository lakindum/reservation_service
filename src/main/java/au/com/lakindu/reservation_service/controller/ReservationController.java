package au.com.lakindu.reservation_service.controller;

import au.com.lakindu.reservation_service.model.Availability;
import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.model.ReservationStatus;
import au.com.lakindu.reservation_service.service.ReservationService;
import au.com.lakindu.reservation_service.validation.RegexConstantsHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(path = "v1/availableSlots/{date}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<Availability> getAvailability(@PathVariable("date") final String date) {
        return reservationService.getAvailability(date);
    }

    @RequestMapping(path = "v1/reservations/{date}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<ReservationResponse> getReservations(@PathVariable("date") final String date) {
        return reservationService.getReservations(date);
    }

    @RequestMapping(path = "v1/reservation/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ReservationResponse getReservation(@PathVariable("id") @Pattern(regexp = RegexConstantsHelper.RESERVATION_ID) final int id) {
        return reservationService.getReservation(id);
    }

    @RequestMapping(path = "v1/reservations", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ReservationStatus getReservation(@Valid @RequestBody @NotNull final ReservationRequest reservationRequest) {
        return reservationService.addReservation(reservationRequest);
    }

    @RequestMapping(path = "v1/reservations/{id}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ReservationStatus getReservation(@PathVariable("id") @Pattern(regexp = RegexConstantsHelper.RESERVATION_ID) final int id, @Valid @RequestBody @NotNull final ReservationRequest reservationRequest) {
        return reservationService.updateReservation(id, reservationRequest);
    }

    @RequestMapping(path = "v1/reservations/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public void deleteReservations(@PathVariable("id") @Pattern(regexp = RegexConstantsHelper.RESERVATION_ID) final int id) {
        reservationService.deleteReservation(id);
    }
}
