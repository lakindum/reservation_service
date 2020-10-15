package au.com.lakindu.reservation_service.repository;

import au.com.lakindu.reservation_service.entity.RestaurantTable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Integer> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    RestaurantTable findByName(String name);
}
