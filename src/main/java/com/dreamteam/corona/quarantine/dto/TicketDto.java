package com.dreamteam.corona.quarantine.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
public class TicketDto {

    protected Long id;

    private Date created;

    private String image;

    private UUID token;

    private Double latitude;

    private Double longitude;
}
