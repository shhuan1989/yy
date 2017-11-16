package com.yijia.yy.repository;

import com.yijia.yy.domain.JobTitle;

import org.springframework.data.jpa.repository.*;

import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the JobTitle entity.
 */
@SuppressWarnings("unused")
public interface JobTitleRepository extends JpaRepository<JobTitle,Long> {

    JobTitle findTopByIdIsNotNull();

    JobTitle findTopByLevel(int level);

    Set<JobTitle> findByLevel(int level);
}
