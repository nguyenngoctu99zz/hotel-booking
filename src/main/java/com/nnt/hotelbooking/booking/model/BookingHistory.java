package com.nnt.hotelbooking.booking.model;

import com.nnt.hotelbooking.booking.constants.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_histories", catalog = "booking_schema")
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_history_id")
    private Long bookingHistoryId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "from_status")
    private BookingStatus fromStatus;

    @Column(name = "to_status", nullable = false)
    private BookingStatus toStatus;

    @Column(name = "changed_by", nullable = false, length = 30)
    private String changedBy;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "changed_by_user_id")
    private Long changedByUserId;
}
