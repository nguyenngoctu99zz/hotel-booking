package com.nnt.hotelbooking.auth.converter;

import com.nnt.hotelbooking.auth.constants.AccountStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AccountStatusConverter
        implements AttributeConverter<AccountStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountStatus attribute) {
        if (attribute == null) {
            return 0;
        }
        return attribute.getValue();
    }

    @Override
    public AccountStatus convertToEntityAttribute(Integer dbData) {
        return AccountStatus.fromValue(dbData);
    }
}
