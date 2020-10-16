package au.com.lakindu.reservation_service.service;

import au.com.lakindu.reservation_service.model.Availability;
import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.model.ReservationStatus;

import java.util.List;

public interface ReservationService {
    List<Availability> getAvailability(String date);
    List<ReservationResponse> getReservations(String date);
    ReservationResponse getReservation(int id);
    ReservationStatus addReservation(ReservationRequest reservationRequest);
    ReservationStatus updateReservation(int id, ReservationRequest reservationRequest);
    ReservationStatus deleteReservation(int id);
}
