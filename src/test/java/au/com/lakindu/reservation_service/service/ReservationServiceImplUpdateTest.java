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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReservationServiceImplUpdateTest {

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
    public void test_update_reservation_valid_reservation_request_should_pass() {
        when(reservationRepository.findById(anyInt())).thenReturn(getValidReservation());
        when(reservationRepository.saveAndFlush(any())).thenReturn(getReservation("Praveen",
                "0434012376", "2020-10-30", "3PM-5PM", 1));
        when(restaurantTableRepository.findByName(any())).thenReturn(getValidRestaurantTable(1, "Table1"));
        when(timeslotRepository.findByName(any())).thenReturn(getValidTimeslot(1, "3PM-5PM"));
        when(reservationMapper.getReservation(any(), any(), any())).thenCallRealMethod();

        ReservationStatus reservationStatus = reservationServiceImpl.updateReservation(1, getValidReservationRequest());
        assertNotNull(reservationStatus);
        assertEquals("1", reservationStatus.getId());
        assertEquals(Status.BOOKED, reservationStatus.getStatus());
    }

    @Test
    public void test_add_reservation_valid_reservation_request_but_no_reservation_found_should_pass() {
        when(reservationRepository.findById(anyInt())).thenReturn(null);

        ReservationStatus reservationStatus = reservationServiceImpl.updateReservation(1, getValidReservationRequest());
        assertNotNull(reservationStatus);
        assertEquals("0", reservationStatus.getId());
        assertEquals(Status.UNAVAILABLE, reservationStatus.getStatus());
    }

    @Test
    public void test_add_reservation_invalid_restaurant_table_should_fail() {
        when(reservationRepository.findById(anyInt())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(null);
        when(timeslotRepository.findByName(any())).thenReturn(getValidTimeslot(1, "3PM-5PM"));
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
        when(reservationRepository.findById(anyInt())).thenReturn(getValidReservation());
        when(restaurantTableRepository.findByName(any())).thenReturn(getValidRestaurantTable(1, "Table1"));
        when(timeslotRepository.findByName(any())).thenReturn(null);
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

    private Reservation getReservation(String name, String contact, String reservationDate,
                                            String strTimeslot, int tableId) {
        RestaurantTable restaurantTable = RestaurantTable.builder().id(tableId).name("Table" + tableId).build();
        Timeslot timeslot = Timeslot.builder().id(1).name(strTimeslot).build();
        return Reservation.builder().id(1).name(name).contact(contact)
                .reservationDate(reservationDate).timeslot(timeslot).restaurant_table(restaurantTable).build();
    }

    private RestaurantTable getValidRestaurantTable(int id, String name) {
        return RestaurantTable.builder().id(id).name(name).build();
    }

    private Timeslot getValidTimeslot(int id, String timeslot) {
        return Timeslot.builder().id(id).name(timeslot).build();
    }
}
