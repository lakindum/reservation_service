package au.com.lakindu.reservation_service.service;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.exception.ReservationException;
import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationStatus;
import au.com.lakindu.reservation_service.model.Status;
import au.com.lakindu.reservation_service.repository.ReservationRepository;
import au.com.lakindu.reservation_service.repository.RestaurantTableRepository;
import au.com.lakindu.reservation_service.repository.TimeslotRepository;
import au.com.lakindu.reservation_service.service.mapping.ReservationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReservationServiceImplCreateTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    RestaurantTableRepository restaurantTableRepository;

    @Mock
    TimeslotRepository timeslotRepository;

    @Mock
    ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void test_add_reservation_valid_reservation_request_should_pass() {
        when(reservationRepository.findExistingReservation(any(), anyInt(), anyInt())).thenReturn(null);
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(getValidRestaurantTable());
        when(timeslotRepository.findByName(any())).thenReturn(getValidTimeslot());
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(reservationMapper.getReservation(any(), any(), any())).thenCallRealMethod();

        ReservationStatus reservationStatus = reservationServiceImpl.addReservation(getValidReservationRequest());
        assertNotNull(reservationStatus);
        assertEquals("1", reservationStatus.getId());
        assertEquals(Status.BOOKED, reservationStatus.getStatus());
    }

    @Test
    public void test_add_reservation_valid_reservation_request_but_unavailable_should_pass() {
        when(reservationRepository.findExistingReservation(any(), anyInt(), anyInt())).thenReturn(getValidReservation());
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(getValidRestaurantTable());
        when(timeslotRepository.findByName(any())).thenReturn(getValidTimeslot());

        ReservationStatus reservationStatus = reservationServiceImpl.addReservation(getValidReservationRequest());
        assertNotNull(reservationStatus);
        assertEquals("0", reservationStatus.getId());
        assertEquals(Status.UNAVAILABLE, reservationStatus.getStatus());
    }

    @Test
    public void test_add_reservation_invalid_restaurant_table_should_fail() {
        when(reservationRepository.findExistingReservation(any(), anyInt(), anyInt())).thenReturn(null);
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(null);
        when(timeslotRepository.findByName(any())).thenReturn(getValidTimeslot());
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(reservationMapper.getReservation(any(), any(), any())).thenCallRealMethod();

        ReservationException thrown = assertThrows(
                ReservationException.class,
                () -> reservationServiceImpl.addReservation(getValidReservationRequest()),
                "Expected addReservation() to throw a ReservationException, but it didn't"
        );
        assertEquals(thrown.getApiErrorStatus().getHttpStatus().value(), HttpStatus.BAD_REQUEST.value());
        assertTrue(thrown.getApiErrorStatus().getMessage().contains("Bad Request"));
    }

    @Test
    public void test_add_reservation_invalid_timeslot_should_fail() {
        when(reservationRepository.findExistingReservation(any(), anyInt(), anyInt())).thenReturn(null);
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(getValidRestaurantTable());
        when(timeslotRepository.findByName(any())).thenReturn(null);
        when(reservationRepository.saveAndFlush(any())).thenReturn(getValidReservation());
        when(reservationMapper.getReservation(any(), any(), any())).thenCallRealMethod();

        ReservationException thrown = assertThrows(
                ReservationException.class,
                () -> reservationServiceImpl.addReservation(getValidReservationRequest()),
                "Expected addReservation() to throw a ReservationException, but it didn't"
        );
        assertEquals(thrown.getApiErrorStatus().getHttpStatus().value(), HttpStatus.BAD_REQUEST.value());
        assertTrue(thrown.getApiErrorStatus().getMessage().contains("Bad Request"));
    }

    private ReservationRequest getValidReservationRequest() {
        return ReservationRequest.builder().name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").reservationTime("3PM-5PM").tableName("Table1").build();
    }

    private Reservation getValidReservation() {
        RestaurantTable restaurantTable = RestaurantTable.builder().id(1).name("Table1").build();
        Timeslot timeslot = Timeslot.builder().id(1).name("3PM-5PM").build();
        return Reservation.builder().id(1).name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").timeslot(timeslot).restaurant_table(restaurantTable).build();
    }

    private RestaurantTable getValidRestaurantTable() {
        return RestaurantTable.builder().id(1).name("Table1").build();
    }

    private Timeslot getValidTimeslot() {
        return Timeslot.builder().id(1).name("3PM-5PM").build();
    }
}
