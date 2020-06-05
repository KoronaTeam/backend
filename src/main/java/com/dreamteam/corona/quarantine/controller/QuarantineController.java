package com.dreamteam.corona.quarantine.controller;

import com.dreamteam.corona.core.exception.InvalidOperationException;
import com.dreamteam.corona.core.exception.NoPermitionException;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.service.UserService;
import com.dreamteam.corona.quarantine.dto.*;
import com.dreamteam.corona.quarantine.mapper.QuarantineMapper;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.repository.QuarantineRepository;
import com.dreamteam.corona.quarantine.service.QuarantineService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class QuarantineController {

    private final QuarantineService quarantineService;
    private final UserService userService;
    private final QuarantineRepository quarantineRepository;
    private final QuarantineMapper quarantineMapper;

    @GetMapping("/quarantines/{id}")
    QuarantineDto getOneQuarantine(@PathVariable Long id) {
        Quarantine quarantine = quarantineService.findById(id);
        QuarantineDto quarantineDto = quarantineMapper.quarantineToFullDto(quarantine);

        return quarantineDto;
    }

    @GetMapping("/quarantines")
    List<QuarantineDto> getAllQuarantines() {
       List<Quarantine> quarantines = quarantineRepository.findAll();
       return quarantineMapper.quarantinesToFlatDtos(quarantines);
    }


    @PostMapping("/quarantines")
    QuarantineDto addNewQuarantine(@RequestBody QuarantineAddDto inputQuarantine) {

        User user = userService.getUserById(inputQuarantine.getUserId());
        Long activeQuarantinesTotal = quarantineRepository.countByUser(user);
        if (activeQuarantinesTotal > 1) {
            throw new InvalidOperationException("This user has not finished quarantine. Cannot has more.");
        }

        Quarantine quarantine = new Quarantine();
        quarantine.setUser(user);
        quarantine.setStartDate(inputQuarantine.getStartDate());
        quarantine.setEndDate(inputQuarantine.getEndDate());
        quarantine.setActive(true);

        quarantine = quarantineService.startQuarantineSendConfirmation(quarantine);

        return quarantineMapper.quarantineToFullDto(quarantine);
    }

    @PostMapping("/quarantines/start")
    QuarantineDto startQuarantine(@RequestBody QuarantineStarterDto inputQuarantine) {
        User user = userService.getUserByUsername(inputQuarantine.getPhone());

        // in production should be done differently. first login to service, sent
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(inputQuarantine.getPassword(), user.getPassword())) {
            throw new NoPermitionException("You have no permission. Bad username or PIN.");
        }

        Quarantine quarantine = quarantineService.findActiveQuarantineForUser(user);
        quarantine.setLatitude(inputQuarantine.getLocation().getLatitude());
        quarantine.setLongitude(inputQuarantine.getLocation().getLongitude());
        quarantine.setPushToken(inputQuarantine.getPushToken());

        quarantine = quarantineService.save(quarantine);

        return quarantineMapper.quarantineToFullDto(quarantine);
    }
}
