package com.yijia.yy.repository;

import com.yijia.yy.domain.ExpenseItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExpenseItem entity.
 */
@SuppressWarnings("unused")
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem,Long> {

}
