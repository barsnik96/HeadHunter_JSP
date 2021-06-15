package by.barsnik96.HeadHunter_JSP.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = false)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime entity_value) {
        return Timestamp.valueOf(entity_value);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp database_value) {
        return database_value.toLocalDateTime();
    }
}