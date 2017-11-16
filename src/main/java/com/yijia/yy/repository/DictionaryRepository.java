package com.yijia.yy.repository;

import com.yijia.yy.domain.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Dictionary entity.
 */
@SuppressWarnings("unused")
public interface DictionaryRepository extends JpaRepository<Dictionary,Long> {

    Dictionary findTopByNameAndParentIsNull(String name);


}
