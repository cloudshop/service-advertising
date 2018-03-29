package com.eyun.advertising.repository;

import com.eyun.advertising.domain.Advertising;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Advertising entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdvertisingRepository extends JpaRepository<Advertising, Long> {

}
