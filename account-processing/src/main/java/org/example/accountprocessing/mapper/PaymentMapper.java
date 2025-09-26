package org.example.accountprocessing.mapper;

import dto.accountProcessing.PaymentDto;
import org.example.accountprocessing.model.Payment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PaymentMapper {
    Payment toEntity(PaymentDto dto);

    PaymentDto toDto(Payment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Payment partialUpdate(PaymentDto dto, @MappingTarget Payment entity);
}
