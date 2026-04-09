package com.nnt.hotelbooking.hotel.listener;

import com.nnt.hotelbooking.booking.event.BookingCreatedEvent;
import com.nnt.hotelbooking.hotel.constants.AvailabilityStatus;
import com.nnt.hotelbooking.hotel.model.RoomAvailability;
import com.nnt.hotelbooking.hotel.repository.RoomAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j(topic = "EVENT-BOOKING_ROOM")
@RequiredArgsConstructor
public class BookingCreatedListener {

    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BookingCreatedEvent event) {

        log.info("[BOOKING_EVENT] Received | bookingId={} | roomId={} | checkin={} | checkout={}",
                event.getBookingId(),
                event.getRoomId(),
                event.getCheckinDate(),
                event.getCheckoutDate());

        List<RoomAvailability> slots =
                roomAvailabilityRepository.findForUpdate(
                        event.getRoomId(),
                        event.getCheckinDate(),
                        event.getCheckoutDate()
                );

        for (RoomAvailability slot : slots) {
            slot.setAvailabilityStatus(AvailabilityStatus.BOOKED);
            slot.setModifiedAt(LocalDateTime.now());
        }

        roomAvailabilityRepository.saveAll(slots);
    }
}