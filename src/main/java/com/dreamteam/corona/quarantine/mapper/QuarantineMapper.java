package com.dreamteam.corona.quarantine.mapper;

import com.dreamteam.corona.quarantine.dto.QuarantineDto;
import com.dreamteam.corona.quarantine.dto.TicketDto;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Ticket.class})
public interface QuarantineMapper {

    @Named("quarantineToFlatDto")
    @Mapping(target = "tickets", qualifiedByName = "ticketsToFlatDtos")
    QuarantineDto quarantineToFlatDto(Quarantine quarantine);

    @Named("quarantinesToFlatDtos")
    @IterableMapping(qualifiedByName = "quarantineToFlatDto")
    List<QuarantineDto> quarantinesToFlatDtos(List<Quarantine> quarantines);

}
