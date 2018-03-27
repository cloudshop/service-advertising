package com.eyun.advertising.service;

import com.eyun.advertising.domain.Advertising;
import com.eyun.advertising.repository.AdvertisingRepository;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Advertising.
 */
@Service
@Transactional
public class AdvertisingService {

    private final Logger log = LoggerFactory.getLogger(AdvertisingService.class);

    private final AdvertisingRepository advertisingRepository;

    public AdvertisingService(AdvertisingRepository advertisingRepository) {
        this.advertisingRepository = advertisingRepository;
    }

    /**
     * Save a advertising.
     *
     * @param advertising the entity to save
     * @return the persisted entity
     */
    public Advertising save(Advertising advertising) {
        log.debug("Request to save Advertising : {}", advertising);
        return advertisingRepository.save(advertising);
    }

    /**
     * Get all the advertisings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Advertising> findAll(Pageable pageable) {
        log.debug("Request to get all Advertisings");
        return advertisingRepository.findAll(pageable);
    }

    /**
     * Get one advertising by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Advertising findOne(Long id) {
        log.debug("Request to get Advertising : {}", id);
        return advertisingRepository.findOne(id);
    }

    /**
     * Delete the advertising by id.
     * 物理删除 设置修改时间和删除属性
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Advertising : {}", id);
        Advertising advertising = advertisingRepository.getOne(id);
        advertising.setModified_time(Instant.now());
        advertising.setDeleted(true);
        advertisingRepository.save(advertising);
        return ;
    }
}
