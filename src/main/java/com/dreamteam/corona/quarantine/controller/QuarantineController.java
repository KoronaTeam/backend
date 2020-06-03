package com.dreamteam.corona.quarantine.controller;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.service.UserService;
import com.dreamteam.corona.quarantine.dto.*;
import com.dreamteam.corona.quarantine.mapper.QuarantineMapper;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.service.QuarantineService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class QuarantineController {

    private final QuarantineService quarantineService;
    private final UserService userService;
    private final QuarantineMapper quarantineMapper;

    @GetMapping("/quarantines/{id}")
    QuarantineDto getOneQuarantine(@PathVariable Long id) {
        Quarantine quarantine = quarantineService.findById(id);
        return quarantineMapper.quarantineToFlatDto(quarantine);
    }

    @PostMapping("/quarantines/")
    QuarantineDto addNewQuarantine(@RequestBody QuarantineAddDto inputQuarantine) {

        Quarantine quarantine = new Quarantine();
        User user = userService.getUserById(inputQuarantine.getUserId());
        quarantine.setUser(user);
        quarantine.setStartDate(inputQuarantine.getStartDate());
        quarantine.setEndDate(inputQuarantine.getEndDate());
        quarantine.setActive(true);

        quarantine = quarantineService.startQuarantineSendConfirmation(quarantine);

        return quarantineMapper.quarantineToFlatDto(quarantine);
    }

    @PostMapping("/quarantines/start")
    QuarantineDto startQuarantine(@RequestBody QuarantineStarterDto inputQuarantine) {
        User user = userService.getUserByUsername(inputQuarantine.getPhone());

        // in production should be done differently. first login to service, sent
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.encode(inputQuarantine.getPassword()) != user.getPassword() ) {

        }

        Quarantine quarantine = quarantineService.findActiveQuarantineForUser(user);
        quarantine.setLatitude(inputQuarantine.getLocation().getLatitude());
        quarantine.setLongitude(inputQuarantine.getLocation().getLongitude());
        quarantine.setPushToken(inputQuarantine.getPushToken());

        quarantine = quarantineService.save(quarantine);

        return quarantineMapper.quarantineToFlatDto(quarantine);
    }
}
