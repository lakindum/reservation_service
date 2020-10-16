package au.com.lakindu.reservation_service.service.mapping;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationMapper {

    public Reservation getReservation(int id, ReservationRequest reservationRequest, RestaurantTable restaurantTable,
                                      Timeslot timeslot) {
        Reservation reservation = getReservation(reservationRequest, restaurantTable, timeslot);
        Reservation.ReservationBuilder reservationBuilder = reservation.toBuilder();
        reservationBuilder.id(id);
        return reservationBuilder.build();
    }

    public Reservation getReservation(ReservationRequest reservationRequest, RestaurantTable restaurantTable,
                                      Timeslot timeslot) {
        return Reservation.builder()
            .name(reservationRequest.getName())
            .contact(reservationRequest.getContact())
            .reservationDate(reservationRequest.getReservationDate())
            .restaurant_table(restaurantTable)
            .timeslot(timeslot).build();
    }

    public List<ReservationResponse> getReservationResponseList(List<Reservation> reservations) {
        List<ReservationResponse> reservationResponseList = new ArrayList<>();
        reservations.forEach(reservation -> reservationResponseList.add(getReservationResponse(reservation)));
        return reservationResponseList;
    }

    public ReservationResponse getReservationResponse(Reservation reservation) {
        return ReservationResponse.builder().id("" + reservation.getId()).name(reservation.getName())
            .contact(reservation.getContact()).reservationDate(reservation.getReservationDate())
            .reservationTime(reservation.getTimeslot().getName())
            .tableName(reservation.getRestaurant_table().getName()).build();
    }
}
