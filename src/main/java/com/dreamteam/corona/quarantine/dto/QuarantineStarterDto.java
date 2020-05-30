package com.dreamteam.corona.quarantine.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuarantineStarterDto {

    private String phone;
    private LocationDto location;
    private String password;
    private String pushToken;
}
