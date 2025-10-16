package org.example.accountprocessing.mapper;

import dto.accountProcessing.TransactionDto;
import org.example.accountprocessing.model.Transaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TransactionMapper {
    Transaction toEntity(TransactionDto dto);

    TransactionDto toDto(Transaction entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction partialUpdate(TransactionDto dto, @MappingTarget Transaction entity);
}
