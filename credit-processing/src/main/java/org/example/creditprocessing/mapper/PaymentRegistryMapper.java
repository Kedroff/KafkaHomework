package org.example.creditprocessing.mapper;

import dto.creditProcessing.PaymentRegistryDto;
import org.example.creditprocessing.model.PaymentRegistry;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PaymentRegistryMapper {
    PaymentRegistry toEntity(PaymentRegistryDto dto);

    PaymentRegistryDto toDto(PaymentRegistry entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentRegistry partialUpdate(PaymentRegistryDto dto, @MappingTarget PaymentRegistry entity);
}
