package com.dreamteam.corona.quarantine.controller;

import com.dreamteam.corona.quarantine.dto.EvidenceDto;
import com.dreamteam.corona.quarantine.mapper.TicketMapper;
import com.dreamteam.corona.quarantine.model.Ticket;
import com.dreamteam.corona.quarantine.dto.TicketDto;
import com.dreamteam.corona.core.service.FileService;
import com.dreamteam.corona.quarantine.service.TicketService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final FileService fileService;

    @GetMapping("/tickets/{token}")
    TicketDto getOneTicket(@PathVariable UUID token) {
        Ticket ticket = ticketService.findByToken(token);
        TicketDto ticketDto = ticketMapper.ticketToFlatDto(ticket);
        return ticketDto;
    }

    @GetMapping(value = "tickets/{token}/image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable UUID token) throws IOException {

        String fullDirPath = fileService.getImagesPath();
        Ticket ticket = ticketService.findByToken(token);
        String fileName = ticket.getImage();
        Path fullFilePath = Paths.get(fullDirPath + fileName);

        InputStream in = Files.newInputStream(fullFilePath);

        return IOUtils.toByteArray(in);
    }


    @PostMapping("tickets")
    TicketDto saveQuarantineEvidence(@RequestBody EvidenceDto evidence) {
        Ticket ticket = ticketService.findByToken(evidence.getToken());
        String file = fileService.saveFile(evidence.getPhoto(), evidence.getToken().toString());

        ticket.setLatitude(evidence.getLocation().getLatitude());
        ticket.setLongitude(evidence.getLocation().getLongitude());
        ticket.setImage(file);
        ticket = ticketService.save(ticket);

        double distance = ticketService.calculateDistance(ticket);
        // ToDo: calculating distance could be costly, so count it and save result in entity
        // ToDo: move logic to service layer
        // ToDo: prepare response, maybe push to admin or just email

        return ticketMapper.ticketToFlatDto(ticket);
    }



}
