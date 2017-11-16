package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.IncomeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Income and its DTO IncomeDTO.
 */
@Mapper(componentModel = "spring", uses = {ApprovalRequestMapper.class, EmployeeMapper.class})
public interface IncomeMapper {

    @Mapping(source = "incomeMethod.id", target = "incomeMethodId")
    @Mapping(source = "incomeMethod.name", target = "incomeMethodName")
    @Mapping(target = "approvalStatus", ignore = true)
    IncomeDTO incomeToIncomeDTO(Income income);

    List<IncomeDTO> incomesToIncomeDTOs(List<Income> incomes);

    @Mapping(source = "incomeMethodId", target = "incomeMethod.id")
    Income incomeDTOToIncome(IncomeDTO incomeDTO);

    List<Income> incomeDTOsToIncomes(List<IncomeDTO> incomeDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
