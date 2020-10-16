package au.com.lakindu.reservation_service.service;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.exception.ReservationException;
import au.com.lakindu.reservation_service.model.*;
import au.com.lakindu.reservation_service.repository.ReservationRepository;
import au.com.lakindu.reservation_service.repository.RestaurantTableRepository;
import au.com.lakindu.reservation_service.repository.TimeslotRepository;
import au.com.lakindu.reservation_service.service.mapping.ReservationMapper;
import au.com.lakindu.reservation_service.service.mapping.AvailabilityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(timeout = 1000)
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    RestaurantTableRepository restaurantTableRepository;
    TimeslotRepository timeslotRepository;
    ReservationMapper reservationMapper;
    AvailabilityMapper availabilityMapper;

    public List<Availability> getAvailability(String date) {
        List<Reservation> reservations = reservationRepository.findByReservationDate(date);
        Iterable<Timeslot> timeslots = timeslotRepository.findAll();
        Iterable<RestaurantTable> restaurantTables = restaurantTableRepository.findAll();
        return availabilityMapper.mapAvailabilities(date, restaurantTables, timeslots, reservations);
    }

    public List<ReservationResponse> getReservations(String date) {
        List<Reservation> reservations = reservationRepository.findByReservationDate(date);
        return reservationMapper.getReservationResponseList(reservations);
    }

    public ReservationResponse getReservation(int id) {
        Reservation reservation = reservationRepository.findById(id);
        if (!Optional.ofNullable(reservation).isPresent()) {
            throw new ReservationException("Reservation not found", ApiErrorStatus.NOT_FOUND);
        }
        return reservationMapper.getReservationResponse(reservation);
    }

    public ReservationStatus addReservation(ReservationRequest reservationRequest) {
        RestaurantTable restaurantTable = getRestaurantTable(reservationRequest.getTableName());
        Timeslot timeslot = getTimeslot(reservationRequest.getReservationTime());

        Reservation existingReservation = reservationRepository.findExistingReservation(reservationRequest.getReservationDate(),
                restaurantTable.getId(), timeslot.getId());
        if (Optional.ofNullable(existingReservation).isPresent()) {
            log.info("A reservation already exists for given request parameters");
            return ReservationStatus.builder().id("0").status(Status.UNAVAILABLE).build();
        } else {
            Reservation reservation = reservationMapper.getReservation(reservationRequest, restaurantTable, timeslot);
            Reservation savedReservation = reservationRepository.saveAndFlush(reservation);
            return ReservationStatus.builder().id("" + savedReservation.getId()).status(Status.BOOKED).build();
        }
    }

    public ReservationStatus updateReservation(int id, ReservationRequest reservationRequest) {
        if (Optional.ofNullable(reservationRepository.findById(id)).isPresent()) {
            RestaurantTable restaurantTable = getRestaurantTable(reservationRequest.getTableName());
            Timeslot timeslot = getTimeslot(reservationRequest.getReservationTime());
            Reservation reservation = reservationMapper.getReservation(id, reservationRequest, restaurantTable, timeslot);
            reservation = reservationRepository.saveAndFlush(reservation);
            return ReservationStatus.builder().id("" + reservation.getId()).status(Status.BOOKED).build();
        } else {
            log.info("No reservation found for given reservation id");
            return ReservationStatus.builder().id("0").status(Status.UNAVAILABLE).build();
        }
    }

    public ReservationStatus deleteReservation(int id) {
        if (Optional.ofNullable(getReservation(id)).isPresent()) {
            reservationRepository.deleteById(id);
        }
        return ReservationStatus.builder().id("0").status(Status.UNRESERVED).build();
    }

    private RestaurantTable getRestaurantTable(String restaurantTableName) {
        RestaurantTable restaurantTable = restaurantTableRepository.findByName(restaurantTableName);
        if (!Optional.ofNullable(restaurantTable).isPresent()) {
            throw new ReservationException("Invalid restaurant table", ApiErrorStatus.BAD_REQUEST);
        }
        return restaurantTable;
    }

    private Timeslot getTimeslot(String timeslotName) {
        Timeslot timeslot = timeslotRepository.findByName(timeslotName);
        if (!Optional.ofNullable(timeslot).isPresent()) {
            throw new ReservationException("Invalid timeslot", ApiErrorStatus.BAD_REQUEST);
        }
        return timeslot;
    }

}