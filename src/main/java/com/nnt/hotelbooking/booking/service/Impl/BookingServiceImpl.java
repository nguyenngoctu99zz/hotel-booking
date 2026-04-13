package com.nnt.hotelbooking.booking.service.Impl;

import com.nnt.hotelbooking.booking.constants.BookingStatus;
import com.nnt.hotelbooking.booking.dto.request.CreateBookingRequest;
import com.nnt.hotelbooking.booking.dto.response.BookingResponse;
import com.nnt.hotelbooking.booking.event.BookingCreatedEvent;
import com.nnt.hotelbooking.booking.model.Booking;
import com.nnt.hotelbooking.booking.model.BookingHistory;
import com.nnt.hotelbooking.booking.repository.BookingHistoryRepository;
import com.nnt.hotelbooking.booking.repository.BookingRepository;
import com.nnt.hotelbooking.booking.service.BookingService;
import com.nnt.hotelbooking.common.currentUser.CurrentUserProvider;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import com.nnt.hotelbooking.hotel.constants.AvailabilityStatus;
import com.nnt.hotelbooking.hotel.model.RoomAvailability;
import com.nnt.hotelbooking.hotel.service.RoomAvailabilityQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "BOOKING-SERVICE")
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingHistoryRepository bookingHistoryRepository;
    private final RoomAvailabilityQueryService roomAvailabilityQueryService;
    private final CurrentUserProvider currentUserProvider;
    private final ApplicationEventPublisher eventPublisher;

//    Pessimistic Locking + State Transition Pattern
    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {

        log.info("[BOOKING] Request | roomId={} | checkin={} | checkout={} | userId={}",
                request.getRoomId(),
                request.getCheckinDate(),
                request.getCheckoutDate(),
                currentUserProvider.getAuthId());

        if (request.getCheckoutDate().isBefore(request.getCheckinDate())) {
            log.warn("[BOOKING] Invalid date | roomId={} | checkin={} | checkout={}",
                    request.getRoomId(),
                    request.getCheckinDate(),
                    request.getCheckoutDate());
            throw new AppException(ErrorCode.INVALID_BOOKING_DATE);
        }

        log.info("[BOOKING] Locking room slots | roomId={}", request.getRoomId());

        // 1. Lock slot để tránh race --> Pessimistic Locking (roomAvailabilityQueryService: RoomAvailabilityRepository)
        List<RoomAvailability> availabilities =
                roomAvailabilityQueryService.findSlotsForUpdate(
                        request.getRoomId(),
                        request.getCheckinDate(),
                        request.getCheckoutDate()
                );

        if (availabilities.isEmpty() || availabilities.stream()
                .anyMatch(slot -> slot.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE)) {

            log.warn("[BOOKING] Room not available | roomId={} | checkin={} | checkout={}",
                    request.getRoomId(),
                    request.getCheckinDate(),
                    request.getCheckoutDate());

            throw new AppException(ErrorCode.ROOM_NOT_AVAILABLE);
        }

        log.info("[BOOKING] Slots locked successfully | roomId={} | nights={}",
                request.getRoomId(),
                availabilities.size());

        // 2. Mark tạm BOOKING_IN_PROGRESS
        availabilities.forEach(slot -> slot.setAvailabilityStatus(AvailabilityStatus.BOOKING_IN_PROGRESS));
        roomAvailabilityQueryService.saveAll(availabilities);
        log.info("[BOOKING] Slots marked BOOKING_IN_PROGRESS | roomId={}", request.getRoomId());

        // 3. Tính totalAmount và totalNights
        BigDecimal totalAmount = availabilities.stream()
                .map(RoomAvailability::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalNights = availabilities.size();

        // 4. Tạo Booking
        Booking booking = bookingRepository.save(
                Booking.builder()
                        .bookingCode("BK-" + System.currentTimeMillis())
                        .roomId(request.getRoomId())
                        .userProfileId(currentUserProvider.getAuthId())
                        .checkinDate(request.getCheckinDate())
                        .checkoutDate(request.getCheckoutDate())
                        .totalNights(totalNights)
                        .totalAmount(totalAmount)
                        .bookingStatus(BookingStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );

        log.info("[BOOKING] Booking created | bookingId={} | bookingCode={}",
                booking.getBookingId(),
                booking.getBookingCode());

        // 5. Tạo Booking History
        bookingHistoryRepository.save(
                BookingHistory.builder()
                        .bookingId(booking.getBookingId())
                        .toStatus(BookingStatus.PENDING)
                        .changedBy("USER")
                        .changedByUserId(currentUserProvider.getAuthId())
                        .note("Booking created")
                        .build()
        );

        log.info("[BOOKING] Booking history saved | bookingId={}", booking.getBookingId());


        // 6. Publish event
        eventPublisher.publishEvent(
                BookingCreatedEvent.builder()
                        .bookingId(booking.getBookingId())
                        .roomId(request.getRoomId())
                        .checkinDate(request.getCheckinDate())
                        .checkoutDate(request.getCheckoutDate())
                        .build()
        );

        log.info("[BOOKING] Event published | bookingId={}", booking.getBookingId());

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .bookingCode(booking.getBookingCode())
                .roomId(booking.getRoomId())
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .totalNights(booking.getTotalNights())
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getBookingStatus())
                .build();
    }
}