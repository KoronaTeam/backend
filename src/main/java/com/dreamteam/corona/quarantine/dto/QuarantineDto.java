package com.dreamteam.corona.quarantine.dto;

import com.dreamteam.corona.core.dto.UserDto;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Ticket;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuarantineDto {

    private Long id;
    private UserDto user;
    private List<TicketDto> tickets = new ArrayList<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double latitude;
    private Double longitude;

}
