package au.com.lakindu.reservation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Builder(toBuilder=true)
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "reservation_date")
    private String reservationDate;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "contact_number")
    private String contact;

    @JoinColumn(name = "restaurant_table_id", foreignKey = @ForeignKey(name = "FK_RESERVED_TABLE"))
    @ManyToOne
    private RestaurantTable restaurant_table;

    @JoinColumn(name = "timeslot_id", foreignKey = @ForeignKey(name = "FK_RESERVED_TIMESLOT"))
    @ManyToOne
    private Timeslot timeslot;
}
