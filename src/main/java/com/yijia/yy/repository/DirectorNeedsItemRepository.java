package com.yijia.yy.repository;

import com.yijia.yy.domain.DirectorNeedsItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DirectorNeedsItem entity.
 */
@SuppressWarnings("unused")
public interface DirectorNeedsItemRepository extends JpaRepository<DirectorNeedsItem,Long> {

}
