package com.eyun.advertising.repository;

import com.eyun.advertising.domain.Advertising;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Spring Data JPA repository for the Advertising entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdvertisingRepository extends JpaRepository<Advertising, Long>,JpaSpecificationExecutor<Advertising> {
				Page<Advertising> findAll(Specification<Advertising> spec,Pageable pageable);
				 
				
}
