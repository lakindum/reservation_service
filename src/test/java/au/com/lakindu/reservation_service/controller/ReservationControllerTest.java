package au.com.lakindu.reservation_service.controller;

import au.com.lakindu.reservation_service.model.ReservationRequest;
import au.com.lakindu.reservation_service.model.ReservationResponse;
import au.com.lakindu.reservation_service.model.ReservationStatus;
import au.com.lakindu.reservation_service.model.Status;
import au.com.lakindu.reservation_service.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    private static final String baseUrl = "http://localhost:8080/";

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void test_get_reservation_by_date_valid_reservation_request_should_pass() {
        when(reservationService.getReservations(anyString())).thenReturn(getValidReservationResponseList());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                baseUrl + "v1/reservations/2020-10-01").accept(
                MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get_reservation_by_date_invalid_date_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                baseUrl + "v1/reservations/2020-10018").accept(
                MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get_reservation_by_id_invalid_reservation_request_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                baseUrl + "v1/reservation/XYZ").accept(
                MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get_availability_valid_date_should_pass() {
        when(reservationService.getReservations(anyString())).thenReturn(getValidReservationResponseList());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                baseUrl + "v1/availableSlots/2020-10-01").accept(
                MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get_availability_invalid_date_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                baseUrl + "v1/availableSlots/2020-10018").accept(
                MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_add_reservation_valid_request_booking_successful_should_pass() {
        when(reservationService.addReservation(any())).thenReturn(ReservationStatus.builder().id("1").status(Status.BOOKED).build());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(baseUrl + "v1/reservations/")
                .content(getValidReservationRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_add_reservation_invalid_date_and_time_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(baseUrl + "v1/reservations/")
            .content(getReservationRequest("Lakindu", "0430321394", "2020-10-801",
                    "3PM-5PMA", "Table2"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_update_reservation_valid_request_booking_successful_should_pass() {
        when(reservationService.updateReservation(anyInt(), any())).thenReturn(ReservationStatus.builder().id("1").status(Status.BOOKED).build());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(baseUrl + "v1/reservations/1")
                .content(getValidReservationRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_update_reservation_invalid_date_and_time_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(baseUrl + "v1/reservations/1")
                .content(getReservationRequest("Lakindu", "0430321394", "2020-10-801",
                        "3PM-5PMA", "Table2"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_delete_reservation_valid_request_booking_successful_should_pass() {
        when(reservationService.deleteReservation(anyInt())).thenReturn(ReservationStatus.builder().id("1").status(Status.UNRESERVED).build());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(baseUrl + "v1/reservations/1")
                .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_delete_reservation_invalid_id_should_fail() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(baseUrl + "v1/reservations/1rt")
                .accept(MediaType.APPLICATION_JSON);
        try {
            this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ReservationResponse> getValidReservationResponseList() {
        ReservationResponse reservationResponse1 = ReservationResponse.builder().id("1").name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").reservationTime("3PM-5PM").tableName("Table1")
                .build();

        ReservationResponse reservationResponse2 = ReservationResponse.builder().id("2").name("Praveen").contact("0430321394")
                .reservationDate("2020-10-01").reservationTime("5PM-7PM").tableName("Table2")
                .build();

        List<ReservationResponse> reservationResponses = new ArrayList<>();
        reservationResponses.add(reservationResponse1);
        reservationResponses.add(reservationResponse2);
        return reservationResponses;
    }

    private String getReservationRequest(String name, String contact,
                                                       String reservationDate, String reservationTime,
                                                       String tableName) {
        ObjectMapper mapper = new ObjectMapper();
        ReservationRequest reservationRequest = ReservationRequest.builder().name(name).contact(contact)
            .reservationDate(reservationDate).reservationTime(reservationTime).tableName(tableName)
            .build();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(reservationRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private String getValidReservationRequest() {
        ObjectMapper mapper = new ObjectMapper();
        ReservationRequest reservationRequest = ReservationRequest.builder().name("Lakindu").contact("0430321394")
                .reservationDate("2020-10-01").reservationTime("3PM-5PM").tableName("Table1").build();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(reservationRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}