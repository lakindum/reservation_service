package au.com.lakindu.reservation_service.service.mapping;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.model.Availability;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailabilityMapper {

    public List<Availability> mapTimeslot(String date, Iterable<RestaurantTable> restaurantTables,
                                          Iterable<Timeslot> timeslots, List<Reservation> reservations) {
        List<Availability> availabilities = new ArrayList<>();
        restaurantTables.forEach(restaurantTable -> {
            timeslots.forEach(timeslot -> {
                if (!isReserved(restaurantTable.getId(), timeslot.getId(), reservations)) {
                    availabilities.add(Availability.builder()
                        .tableName(restaurantTable.getName())
                        .availableTime(timeslot.getName())
                        .availableDate(date).build());
                }
            });
        });
        return availabilities;
    }

    private boolean isReserved(int tableId, int timeslotId, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getRestaurant_table().getId() == tableId &&
                        reservation.getTimeslot().getId() == timeslotId);
    }
}
