package au.com.lakindu.reservation_service.repository;

import au.com.lakindu.reservation_service.entity.Timeslot;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface TimeslotRepository extends CrudRepository<Timeslot, Integer> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Timeslot findByName(String name);
}
