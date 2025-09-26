package org.example.clientprocessing.mapper;

import dto.clientProcessing.UserDto;
import org.example.clientprocessing.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {
    User toEntity(UserDto dto);

    UserDto toDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto dto, @MappingTarget User entity);
}
