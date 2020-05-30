package com.dreamteam.corona.quarantine.mapper;

import com.dreamteam.corona.quarantine.model.Ticket;
import com.dreamteam.corona.quarantine.dto.TicketDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Named("ticketToFlatDto")
//    @Mapping(target = "quarantine", ignore = true)
    TicketDto ticketToFlatDto(Ticket ticket);

    @Named("ticketsToFlatDtos")
    @IterableMapping(qualifiedByName = "ticketToFlatDto")
    List<TicketDto> ticketsToFlatDtos(List<Ticket> tickets);
}
