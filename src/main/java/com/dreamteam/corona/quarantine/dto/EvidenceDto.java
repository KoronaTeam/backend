package com.dreamteam.corona.quarantine.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EvidenceDto {
    private String photo;
    private LocationDto location;
    private UUID token;
}
