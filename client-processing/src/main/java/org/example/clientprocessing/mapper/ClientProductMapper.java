package org.example.clientprocessing.mapper;

import dto.clientProcessing.ClientProductDto;
import org.example.clientprocessing.model.ClientProduct;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientProductMapper {
    ClientProduct toEntity(ClientProductDto dto);

    ClientProductDto toDto(ClientProduct entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientProduct partialUpdate(ClientProductDto dto, @MappingTarget ClientProduct entity);
}
