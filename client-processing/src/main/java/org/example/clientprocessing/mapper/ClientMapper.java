package org.example.clientprocessing.mapper;

import dto.clientProcessing.ClientDto;
import org.example.clientprocessing.model.Client;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientMapper {
    Client toEntity(ClientDto dto);

    ClientDto toDto(Client entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Client partialUpdate(ClientDto dto, @MappingTarget Client entity);
}
