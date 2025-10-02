package org.example.accountprocessing.mapper;

import dto.accountProcessing.AccountDto;
import org.example.accountprocessing.model.Account;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AccountMapper {
    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account partialUpdate(AccountDto dto, @MappingTarget Account entity);
}
