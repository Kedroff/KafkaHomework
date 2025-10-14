package org.example.clientprocessing.mapper;

import dto.clientProcessing.BlacklistRegistryDto;
import org.example.clientprocessing.model.BlacklistRegistry;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BlacklistRegistryMapper {
    BlacklistRegistry toEntity(BlacklistRegistryDto dto);

    BlacklistRegistryDto toDto(BlacklistRegistry entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BlacklistRegistry partialUpdate(BlacklistRegistryDto dto, @MappingTarget BlacklistRegistry entity);
}
