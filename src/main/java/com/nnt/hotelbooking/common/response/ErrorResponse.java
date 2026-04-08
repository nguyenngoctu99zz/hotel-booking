package com.nnt.hotelbooking.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.nnt.hotelbooking.common.constants.DateTimePattern.ISO_DATE_TIME;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_TIME)
    LocalDateTime timestamp;
    Integer code;
    String path;
    String error;
    String message;
}
