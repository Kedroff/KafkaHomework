package org.example.creditprocessing.mapper;

import dto.creditProcessing.ProductRegistryDto;
import org.example.creditprocessing.model.ProductRegistry;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProductRegistryMapper {
    ProductRegistry toEntity(ProductRegistryDto dto);

    ProductRegistryDto toDto(ProductRegistry entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductRegistry partialUpdate(ProductRegistryDto dto, @MappingTarget ProductRegistry entity);
}
