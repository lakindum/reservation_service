package au.com.lakindu.reservation_service.repository;

import au.com.lakindu.reservation_service.entity.Reservation;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Reservation> findByReservationDate(String reservationDate);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Reservation findById(int id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT reservation " +
        "FROM Reservation reservation INNER JOIN reservation.restaurant_table " +
        "INNER JOIN reservation.timeslot WHERE reservation.reservationDate = :reservationDate " +
        "AND reservation.restaurant_table.id = :restaurantTableId AND reservation.timeslot.id = :timeslotId")
    Reservation findExistingReservation(String reservationDate, int restaurantTableId, int timeslotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Reservation saveAndFlush(Reservation reservation);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(Integer Id);
}
