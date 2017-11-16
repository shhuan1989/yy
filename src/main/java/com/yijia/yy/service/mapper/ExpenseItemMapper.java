package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ExpenseItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ExpenseItem and its DTO ExpenseItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExpenseItemMapper {

    @Mapping(source = "expense.id", target = "expenseId")
    ExpenseItemDTO expenseItemToExpenseItemDTO(ExpenseItem expenseItem);

    List<ExpenseItemDTO> expenseItemsToExpenseItemDTOs(List<ExpenseItem> expenseItems);

    @Mapping(source = "expenseId", target = "expense")
    ExpenseItem expenseItemDTOToExpenseItem(ExpenseItemDTO expenseItemDTO);

    List<ExpenseItem> expenseItemDTOsToExpenseItems(List<ExpenseItemDTO> expenseItemDTOs);

    default Expense expenseFromId(Long id) {
        if (id == null) {
            return null;
        }
        Expense expense = new Expense();
        expense.setId(id);
        return expense;
    }
}
