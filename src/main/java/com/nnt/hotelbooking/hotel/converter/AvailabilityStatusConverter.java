package com.nnt.hotelbooking.hotel.converter;

import com.nnt.hotelbooking.hotel.constants.AvailabilityStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AvailabilityStatusConverter
        implements AttributeConverter<AvailabilityStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AvailabilityStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public AvailabilityStatus convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : AvailabilityStatus.fromCode(dbData);
    }
}