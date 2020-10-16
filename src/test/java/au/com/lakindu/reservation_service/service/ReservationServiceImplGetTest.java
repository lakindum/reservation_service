package au.com.lakindu.reservation_service.service;

import au.com.lakindu.reservation_service.entity.Reservation;
import au.com.lakindu.reservation_service.entity.RestaurantTable;
import au.com.lakindu.reservation_service.entity.Timeslot;
import au.com.lakindu.reservation_service.exception.ReservationException;
import au.com.lakindu.reservation_service.model.Availability;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.repository.ReservationRepository;
import au.com.lakindu.reservation_service.repository.RestaurantTableRepository;
import au.com.lakindu.reservation_service.repository.TimeslotRepository;
import au.com.lakindu.reservation_service.service.mapping.AvailabilityMapper;
import au.com.lakindu.reservation_service.service.mapping.ReservationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ReservationServiceImplGetTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    RestaurantTableRepository restaurantTableRepository;

    @Mock
    TimeslotRepository timeslotRepository;

    @Mock
    ReservationMapper reservationMapper;

    @Mock
    AvailabilityMapper availabilityMapper;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void test_get_reservation_by_date_valid_reservation_request_should_pass() {
        when(reservationRepository.findByReservationDate(anyString())).thenReturn(getValidReservations());
        when(reservationMapper.getReservationResponseList(anyList())).thenCallRealMethod();
        when(reservationMapper.getReservationResponse(any())).thenCallRealMethod();
        List<ReservationResponse> reservationResponseList = reservationServiceImpl.getReservations("2020-10-01");
        assertNotNull(reservationResponseList);
        assertEquals(2, reservationResponseList.size());
        assertEquals("1", reservationResponseList.get(0).getId());
        assertEquals("Lakindu", reservationResponseList.get(0).getName());
        assertEquals("0430321394", reservationResponseList.get(0).getContact());
        assertEquals("2020-10-01", reservationResponseList.get(0).getReservationDate());
        assertEquals("3PM-5PM", reservationResponseList.get(0).getReservationTime());
        assertEquals("Table1", reservationResponseList.get(0).getTableName());
    }

    @Test
    public void test_get_reservation_by_date_unavailable_should_pass() {
        when(reservationRepository.findByReservationDate(any())).thenReturn(getEmptyReservations());
        when(reservationMapper.getReservationResponseList(getValidReservations())).thenCallRealMethod();
        when(reservationMapper.getReservationResponse(any())).thenCallRealMethod();
        List<ReservationResponse> reservationResponseList = reservationServiceImpl.getReservations("2020-10-01");
        assertNotNull(reservationResponseList);
        assertEquals(0, reservationResponseList.size());
    }

    @Test
    public void test_get_reservation_by_id_valid_reservation_request_should_pass() {
        when(reservationRepository.findById(anyInt())).thenReturn(getValidReservation());
        when(reservationMapper.getReservationResponse(any())).thenCallRealMethod();
        ReservationResponse reservationResponse = reservationServiceImpl.getReservation(1);
        assertNotNull(reservationResponse);
        assertEquals("1", reservationResponse.getId());
        assertEquals("Lakindu", reservationResponse.getName());
        assertEquals("0430321394", reservationResponse.getContact());
        assertEquals("2020-10-01", reservationResponse.getReservationDate());
        assertEquals("3PM-5PM", reservationResponse.getReservationTime());
        assertEquals("Table1", reservationResponse.getTableName());
    }

    @Test
    public void test_get_reservation_by_id_not_found_reservation_request_should_fail() {
        when(reservationRepository.findById(anyInt())).thenReturn(null);
        ReservationException thrown = assertThrows(
                ReservationException.class,
                () -> reservationServiceImpl.getReservation(1),
                "Expected getReservation(1) to throw a ReservationException, but it didn't"
        );
        assertEquals(thrown.getApiErrorStatus().getHttpStatus().value(), HttpStatus.NOT_FOUND.value());
        assertTrue(thrown.getApiErrorStatus().getMessage().contains("Not Found"));
    }

    @Test
    void test_get_availability_valid_request_should_pass() {
        when(reservationRepository.findByReservationDate(anyString())).thenReturn(getValidReservations());
        when(restaurantTableRepository.findAll()).thenReturn(getValidRestaurantTables());
        when(timeslotRepository.findAll()).thenReturn(getValidTimeslots());

        when(availabilityMapper.mapTimeslot(anyString(), any(), any(), anyList())).thenCallRealMethod();
        List<Availability> availabilityList = reservationServiceImpl.getAvailability("2020-10-01");

        assertNotNull(availabilityList);
        assertEquals(23, availabilityList.size());

        availabilityList.forEach(availability -> {
            assertNotNull(availability.getAvailableDate());
            assertNotNull(availability.getAvailableTime());
            assertNotNull(availability.getTableName());
        });
    }

    @Test
    void test_get_availability_valid_request_no_availabilities_should_pass() {
        when(reservationRepository.findByReservationDate(anyString())).thenReturn(getReservationsFullForDay("2020-10-01"));
        when(restaurantTableRepository.findAll()).thenReturn(getValidRestaurantTables());
        when(timeslotRepository.findAll()).thenReturn(getValidTimeslots());

        when(availabilityMapper.mapTimeslot(anyString(), any(), any(), anyList())).thenCallRealMethod();
        List<Availability> availabilityList = reservationServiceImpl.getAvailability("2020-10-01");
        assertNotNull(availabilityList);
        assertEquals(0, availabilityList.size());
    }

    private List<Reservation> getValidReservations() {
        RestaurantTable restaurantTable1 = RestaurantTable.builder().id(1).name("Table1").build();
        RestaurantTable restaurantTable2 = RestaurantTable.builder().id(1).name("Table2").build();

        Timeslot timeslot1 = Timeslot.builder().id(1).name("3PM-5PM").build();
        Timeslot timeslot2 = Timeslot.builder().id(1).name("5PM-7PM").build();

        Reservation reservationResult1 = Reservation.builder().id(1).name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").timeslot(timeslot1).restaurant_table(restaurantTable1).build();

        Reservation reservationResult2 = Reservation.builder().id(2).name("Thilini").contact("0400603768")
                .reservationDate("2020-10-01").timeslot(timeslot2).restaurant_table(restaurantTable2).build();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservationResult1);
        reservationList.add(reservationResult2);

        return reservationList;
    }

    private List<Reservation> getEmptyReservations() {
        return new ArrayList<>();
    }

    private Reservation getValidReservation() {
        RestaurantTable restaurantTable = RestaurantTable.builder().id(1).name("Table1").build();
        Timeslot timeslot = Timeslot.builder().id(1).name("3PM-5PM").build();
        return Reservation.builder().id(1).name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").timeslot(timeslot).restaurant_table(restaurantTable).build();
    }

    private List<RestaurantTable> getValidRestaurantTables() {
        RestaurantTable restaurantTable1 = RestaurantTable.builder().id(1).name("Table1").build();
        RestaurantTable restaurantTable2 = RestaurantTable.builder().id(2).name("Table2").build();
        RestaurantTable restaurantTable3 = RestaurantTable.builder().id(3).name("Table3").build();
        RestaurantTable restaurantTable4 = RestaurantTable.builder().id(4).name("Table4").build();
        RestaurantTable restaurantTable5 = RestaurantTable.builder().id(5).name("Table5").build();
        RestaurantTable restaurantTable6 = RestaurantTable.builder().id(6).name("Table6").build();

        List<RestaurantTable> restaurantTables = new ArrayList<>();
        restaurantTables.add(restaurantTable1);
        restaurantTables.add(restaurantTable2);
        restaurantTables.add(restaurantTable3);
        restaurantTables.add(restaurantTable4);
        restaurantTables.add(restaurantTable5);
        restaurantTables.add(restaurantTable6);
        return restaurantTables;
    }

    private List<Timeslot> getValidTimeslots() {
        Timeslot timeslot1 = Timeslot.builder().id(1).name("11AM-1PM").build();
        Timeslot timeslot2 = Timeslot.builder().id(2).name("1PM-3PM").build();
        Timeslot timeslot3 = Timeslot.builder().id(3).name("3PM-5PM").build();
        Timeslot timeslot4 = Timeslot.builder().id(4).name("5PM-7PM").build();

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot1);
        timeslots.add(timeslot2);
        timeslots.add(timeslot3);
        timeslots.add(timeslot4);
        return timeslots;
    }

    private List<Reservation> getReservationsFullForDay(String date) {
        List<RestaurantTable> restaurantTables = getValidRestaurantTables();
        List<Timeslot> timeslots = getValidTimeslots();

        int i = 1;
        List<Reservation> reservations = new ArrayList<>();
        restaurantTables.forEach(restaurantTable -> {
            timeslots.forEach(timeslot -> {
                reservations.add(Reservation.builder().id(i).name("name" + i).contact("contact" + i)
                        .restaurant_table(restaurantTable).timeslot(timeslot)
                        .reservationDate(date).build());
            });
        });
        return reservations;
    }
}