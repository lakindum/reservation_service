package au.com.lakindu.reservation_service.service;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.exception.ReservationException;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.model.ReservationStatus;
import au.com.lakindu.reservation_service.model.Status;
import au.com.lakindu.reservation_service.repository.ReservationRepository;
import au.com.lakindu.reservation_service.service.mapping.ReservationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReservationServiceImplDeleteTest {
    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void test_delete_reservation_valid_reservation_request_should_pass() {
        when(reservationRepository.findById(anyInt())).thenReturn(getValidReservation());
        when(reservationMapper.getReservationResponseList(anyList())).thenCallRealMethod();
        when(reservationServiceImpl.getReservation(anyInt())).thenReturn(getValidReservationResponse());
        doNothing().when(reservationRepository).deleteById(1);

        ReservationStatus reservationStatus = reservationServiceImpl.deleteReservation(1);
        assertNotNull(reservationStatus);
        assertEquals("0", reservationStatus.getId());
        assertEquals(Status.UNRESERVED, reservationStatus.getStatus());
    }

    @Test
    public void test_delete_unavailable_reservation_should_fail() {
        when(reservationRepository.findById(anyInt())).thenReturn(null);
        ReservationException thrown = assertThrows(
                ReservationException.class,
                () -> reservationServiceImpl.deleteReservation(1),
                "Expected deleteReservation() to throw a ReservationException, but it didn't"
        );
        assertEquals(thrown.getApiErrorStatus().getHttpStatus().value(), HttpStatus.NOT_FOUND.value());
    }

    private ReservationResponse getValidReservationResponse() {
        return ReservationResponse.builder().id("1").name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").reservationTime("3PM-5PM").tableName("Table1")
                .build();
    }

    private Reservation getValidReservation() {
        RestaurantTable restaurantTable = RestaurantTable.builder().id(1).name("Table1").build();
        Timeslot timeslot = Timeslot.builder().id(1).name("3PM-5PM").build();
        return Reservation.builder().id(1).name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").timeslot(timeslot).restaurant_table(restaurantTable).build();
    }
}
