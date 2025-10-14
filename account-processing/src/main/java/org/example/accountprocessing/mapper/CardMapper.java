package org.example.accountprocessing.mapper;

import dto.accountProcessing.CardDto;
import org.example.accountprocessing.model.Card;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CardMapper {
    Card toEntity(CardDto dto);

    CardDto toDto(Card entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Card partialUpdate(CardDto dto, @MappingTarget Card entity);
}
