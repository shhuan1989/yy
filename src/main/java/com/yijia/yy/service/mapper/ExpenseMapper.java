package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ExpenseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Expense and its DTO ExpenseDTO.
 */
@Mapper(componentModel = "spring", uses = {ExpenseItemMapper.class, EmployeeMapper.class, ApprovalRequestMapper.class})
public interface ExpenseMapper {

    @Mapping(source = "payMethod.id", target = "payMethodId")
    @Mapping(source = "payMethod.name", target = "payMethodName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.idNumber", target = "projectIdNumber")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(target = "approvalStatus", ignore = true)
    ExpenseDTO expenseToExpenseDTO(Expense expense);

    List<ExpenseDTO> expensesToExpenseDTOs(List<Expense> expenses);

    @Mapping(source = "payMethodId", target = "payMethod.id")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(target = "actualStartTime", ignore = true)
    @Mapping(target = "actualEndTime", ignore = true)
    Expense expenseDTOToExpense(ExpenseDTO expenseDTO);

    List<Expense> expenseDTOsToExpenses(List<ExpenseDTO> expenseDTOs);

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
